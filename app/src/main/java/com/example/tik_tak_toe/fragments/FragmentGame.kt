import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.tik_tak_toe.R
import com.example.tik_tak_toe.databinding.FragmentGameBinding
import com.example.tik_tak_toe.fragments.GameResult
import com.example.tik_tak_toe.fragments.SeriesResultFragment
import com.example.tik_tak_toe.utils.GameSettings
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FragmentGame : Fragment() {
    private lateinit var binding: FragmentGameBinding
    private val inputsList = arrayOfNulls<Int>(9)//0 -> blue, 1 -> red
    private var turns = 0
    private var gameSettings: GameSettings? = null
    private val winningIndexList = ArrayList<ArrayList<Int>>()
    private lateinit var mContext: Context
    private var isGameOver = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGameBinding.bind(view)

        val dataInString = arguments?.getString("game_settings")
        gameSettings = Gson().fromJson(dataInString, GameSettings::class.java)

        setClickListener()
        addWinningSituations()
        showNextTurnColor(gameSettings?.startingColor == 0)
        startTimer()

    }

    private fun startTimer() {
        var timeLeft = gameSettings!!.gameTimer * 60
        CoroutineScope(Dispatchers.Main).launch {
            while (timeLeft > 0) {
                binding.txtTimer.text = timeLeft.toString()
                delay(1000)
                timeLeft--
            }
            gameOver(-1)
        }
    }

    private fun addWinningSituations() {
        //Horizontal
        winningIndexList.add(arrayListOf(0, 1, 2))
        winningIndexList.add(arrayListOf(3, 4, 5))
        winningIndexList.add(arrayListOf(6, 7, 8))

        //Vertical
        winningIndexList.add(arrayListOf(0, 3, 6))
        winningIndexList.add(arrayListOf(1, 4, 7))
        winningIndexList.add(arrayListOf(2, 5, 8))

        //Diagonal
        winningIndexList.add(arrayListOf(0, 4, 8))
        winningIndexList.add(arrayListOf(2, 4, 6))
    }

    private fun setClickListener() {
        takeActionOnClick(binding.img00)
        takeActionOnClick(binding.img01)
        takeActionOnClick(binding.img02)
        takeActionOnClick(binding.img10)
        takeActionOnClick(binding.img11)
        takeActionOnClick(binding.img12)
        takeActionOnClick(binding.img20)
        takeActionOnClick(binding.img21)
        takeActionOnClick(binding.img22)
    }

    private fun takeActionOnClick(view: ImageView) {
        view.setOnClickListener {
            when (view.id) {
                binding.img00.id ->
                    showSelectedInput(0, view)
                binding.img01.id ->
                    showSelectedInput(1, view)
                binding.img02.id ->
                    showSelectedInput(2, view)
                binding.img10.id ->
                    showSelectedInput(3, view)
                binding.img11.id ->
                    showSelectedInput(4, view)
                binding.img12.id ->
                    showSelectedInput(5, view)
                binding.img20.id ->
                    showSelectedInput(6, view)
                binding.img21.id ->
                    showSelectedInput(7, view)
                binding.img22.id ->
                    showSelectedInput(8, view)
            }
        }
    }

    private fun showSelectedInput(index: Int, view: ImageView) {
        if (inputsList[index] != null)
            return

        turns++

        //Color
        var turnsColor = gameSettings?.startingColor
        if (turns % 2 == 0) {
            turnsColor = if (turnsColor == 0)
                1
            else
                0
        }
        inputsList[index] = turnsColor

        //Type
        val isCross = (turns % 2 == 0)

        //Select
        if (isCross && turnsColor == 0) {
            view.setImageResource(R.drawable.cross_blue)
        } else if (isCross) {
            view.setImageResource(R.drawable.cross_red)
        } else if (turnsColor == 0) {
            view.setImageResource(R.drawable.o_blue)
        } else {
            view.setImageResource(R.drawable.o_red)
        }

        checkWinningSituations()
        showNextTurnColor(turnsColor == 1)
    }

    private fun showNextTurnColor(isBlue: Boolean) {
        val msg: String
        if (isBlue) {
            msg = "Blue's turn"
            binding.txtTurnColor.setTextColor(mContext.getColor(R.color.blue_game_color))
        } else {
            msg = "Red's turn"
            binding.txtTurnColor.setTextColor(mContext.getColor(R.color.red_game_color))
        }
        binding.txtTurnColor.text = msg
    }

    private fun checkWinningSituations() {
        winningIndexList.forEach {
            val val1 = inputsList[it[0]]
            val val2 = inputsList[it[1]]
            val val3 = inputsList[it[2]]

            if (val1 != null && val1 == val2 && val2 == val3) {
                gameOver(val1)
                return
            }
        }

        //Check if no one left
        inputsList.forEach {
            if (it == null)
                return
        }

        gameOver(-1)
    }

    private fun gameOver(winner: Int) {
        if (isGameOver)
            return

        isGameOver = true
        Log.i("TAG", "gameOver: Called")

        if (winner != -1) {
            if (winner == 0) {
                gameSettings?.score_of_blue = gameSettings?.score_of_blue?.plus(1)!!
            } else {
                gameSettings?.score_of_red = gameSettings?.score_of_red?.plus(1)!!
            }

            gameSettings?.whoWinsLastMatch = winner
            gameSettings?.startingColor = winner
        } else {
            gameSettings?.whoWinsLastMatch = -1
        }

        gameSettings?.matches = gameSettings?.matches?.minus(1)!!

        if (gameSettings?.matches!! > 0) {
            endGame()
        } else {
            endSeries()
        }
    }

    private fun endGame() {
        val fragment = GameResult.newInstance(gameSettings!!)
        val ac: FragmentActivity = mContext as FragmentActivity
        val fr = ac.supportFragmentManager.beginTransaction()
        fr.replace(R.id.frame_layout_main, fragment).addToBackStack(null).commit()
    }

    private fun endSeries() {
        val endSeriesFragment = SeriesResultFragment.newInstance(gameSettings!!)
        val ac: FragmentActivity = mContext as FragmentActivity
        val fr = ac.supportFragmentManager.beginTransaction()
        fr.replace(R.id.frame_layout_main, endSeriesFragment).addToBackStack(null).commit()
    }

    companion object {
        fun newInstance(yourData: GameSettings) = FragmentGame().apply {
            val dataInString = Gson().toJson(yourData).toString()
            arguments = Bundle().apply { putString("game_settings", dataInString) }
        }
    }

}