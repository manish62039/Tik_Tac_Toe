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
import com.example.tik_tak_toe.databinding.FragmentGameResultBinding
import com.example.tik_tak_toe.utils.GameSettings


class GameResult : Fragment() {
    private lateinit var binding: FragmentGameResultBinding
    private var gameSettings: GameSettings? = null
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGameResultBinding.bind(view)

        gameSettings = arguments?.get("game_settings") as? GameSettings

        if (gameSettings != null) {
            var whoWins = "Draw"
            if (gameSettings!!.whoWinsLastMatch == 1) {
                whoWins = "Red Wins"
                binding.txtWhoWins.setTextColor(mContext.getColor(R.color.red_game_color))
            } else if (gameSettings!!.whoWinsLastMatch == 0) {
                whoWins = "Blue Wins"
                binding.txtWhoWins.setTextColor(mContext.getColor(R.color.blue_game_color))
            }

            binding.txtWhoWins.text = whoWins

            val blueScore = "Blue : ${gameSettings!!.score_of_blue}"
            val redScore = "Red : ${gameSettings!!.score_of_red}"

            binding.txtBlueScore.text = blueScore
            binding.txtRedScore.text = redScore
        }

        binding.btnNextMatch.setOnClickListener {
            selectFragment()
        }
    }

    private fun selectFragment() {
        val bundle: Bundle = bundleOf()
        bundle.putSerializable("game_settings", gameSettings)
        val ac: FragmentActivity = mContext as FragmentActivity
        val fr = ac.supportFragmentManager.beginTransaction()
        fr.replace(R.id.frame_layout_main, FragmentGame::class.java, bundle).commit()
    }

}