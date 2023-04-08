package com.example.tik_tak_toe.fragments

import FragmentGame
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import com.example.tik_tak_toe.R
import com.example.tik_tak_toe.databinding.ActivityMainBinding
import com.example.tik_tak_toe.databinding.FragmentStartGameBinding
import com.example.tik_tak_toe.utils.GameSettings
import com.example.tik_tak_toe.utils.GameSettingsSaver


class StartGameFragment : Fragment() {
    private lateinit var binding: FragmentStartGameBinding
    private var gameSettings = GameSettings()
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
        return inflater.inflate(R.layout.fragment_start_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStartGameBinding.bind(view)

        settingChangeListeners()
        binding.startBtn.setOnClickListener {
            selectFragment()
        }

        getGameSettings()
    }

    private fun selectFragment() {
        saveGameSettings()
//        val bundle: Bundle = bundleOf()
//        bundle.putSerializable("game_settings", gameSettings)
//        val ac: FragmentActivity = context as FragmentActivity
//        val fr = ac.supportFragmentManager.beginTransaction()
//        fr.replace(R.id.frame_layout_main, FragmentGame::class.java, bundle).commit()

        val ac: FragmentActivity = context as FragmentActivity
        val fr = ac.supportFragmentManager.beginTransaction()
        val fragmentGame = FragmentGame.newInstance(gameSettings)
        fr.replace(R.id.frame_layout_main, fragmentGame)
            .addToBackStack(null)
            .commit()
    }

    private fun settingChangeListeners() {
        Log.i("TAG", "GameSettings: $gameSettings")

        binding.rgMatches.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.rbMatch3.id ->
                    gameSettings.matches = 3
                binding.rbMatch5.id ->
                    gameSettings.matches = 5
                binding.rbMatch7.id ->
                    gameSettings.matches = 7
            }
            Log.i("TAG", "GameSettings: $gameSettings")
        }

        binding.rgColor.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.rbColorBlue.id ->
                    gameSettings.startingColor = 0
                binding.rbColorRed.id ->
                    gameSettings.startingColor = 1
            }
            Log.i("TAG", "GameSettings: $gameSettings")
        }

        binding.rgTimer.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.rbTimer15.id ->
                    gameSettings.gameTimer = 1
                binding.rbTimer30.id ->
                    gameSettings.gameTimer = 2
                binding.rbTimer45.id ->
                    gameSettings.gameTimer = 5
            }
            Log.i("TAG", "GameSettings: $gameSettings")
        }
    }

    private fun saveGameSettings() {
        GameSettingsSaver.getInstance(mContext).saveGameSettings(gameSettings)
    }

    private fun getGameSettings() {
        gameSettings = GameSettingsSaver.getInstance(mContext).getSavedGameSettings()

        when (gameSettings.matches) {
            3 ->
                binding.rgMatches.check(binding.rbMatch3.id)
            5 ->
                binding.rgMatches.check(binding.rbMatch5.id)
            7 ->
                binding.rgMatches.check(binding.rbMatch7.id)
        }


        when (gameSettings.startingColor) {
            0 ->
                binding.rgColor.check(binding.rbColorBlue.id)
            1 ->
                binding.rgColor.check(binding.rbColorRed.id)
        }

        when (gameSettings.gameTimer) {
            1 ->
                binding.rgTimer.check(binding.rbTimer15.id)
            2 ->
                binding.rgTimer.check(binding.rbTimer30.id)
            5 ->
                binding.rgTimer.check(binding.rbTimer45.id)
        }

    }

}