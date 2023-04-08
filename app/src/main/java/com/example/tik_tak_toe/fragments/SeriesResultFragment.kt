package com.example.tik_tak_toe.fragments

import FragmentGame
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import com.example.tik_tak_toe.R
import com.example.tik_tak_toe.databinding.FragmentSeriesResultBinding
import com.example.tik_tak_toe.utils.GameSettings
import com.example.tik_tak_toe.utils.GameSettingsSaver
import com.google.gson.Gson


class SeriesResultFragment : Fragment() {
    private lateinit var binding: FragmentSeriesResultBinding
    private lateinit var mContext: Context
    private var gameSettings: GameSettings? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_series_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSeriesResultBinding.bind(view)

        val dataInString = arguments?.getString("game_settings")
        gameSettings = Gson().fromJson(dataInString, GameSettings::class.java)

        if (gameSettings != null) {
            val redScore = gameSettings!!.score_of_red
            val blueScore = gameSettings!!.score_of_blue

            if (redScore > blueScore) {
                binding.txtSeriesResult.text = getString(R.string.red_wins_the_series)
                binding.txtSeriesResult.setTextColor(mContext.getColor(R.color.red_game_color))
            } else if (blueScore > redScore) {
                binding.txtSeriesResult.text = getString(R.string.blue_wins_the_series)
                binding.txtSeriesResult.setTextColor(mContext.getColor(R.color.blue_game_color))
            }

            val redScoreText = "Red : $redScore"
            val blueScoreText = "Blue : $blueScore"
            binding.txtBlueScore.text = blueScoreText
            binding.txtRedScore.text = redScoreText

            binding.btnSettings.setOnClickListener {
                startSettings()
            }

            binding.btnPlayAgain.setOnClickListener {
                //Reset Game Settings
                gameSettings = GameSettingsSaver.getInstance(mContext).getSavedGameSettings()
                restartSeries()
            }
        }

    }

    private fun restartSeries() {
        val fragmentGame = FragmentGame.newInstance(gameSettings!!)
        val ac: FragmentActivity = context as FragmentActivity
        val fr = ac.supportFragmentManager.beginTransaction()
        fr.replace(R.id.frame_layout_main, fragmentGame).addToBackStack(null).commit()
    }

    private fun startSettings() {
        val ac: FragmentActivity = mContext as FragmentActivity
        val fr = ac.supportFragmentManager.beginTransaction()
        fr.replace(R.id.frame_layout_main, StartGameFragment()).commit()
    }

    companion object {
        fun newInstance(yourData: GameSettings) = SeriesResultFragment().apply {
            val dataInString = Gson().toJson(yourData).toString()
            arguments = Bundle().apply { putString("game_settings", dataInString) }
        }
    }

}