package com.example.tik_tak_toe.utils

import android.content.Context
import android.content.SharedPreferences


class GameSettingsSaver {
    fun saveGameSettings(gameSettings: GameSettings) {
        val editor = sharedPreferences?.edit()

        editor?.putInt("matches", gameSettings.matches)?.apply()
        editor?.putInt("startingColor", gameSettings.startingColor)?.apply()
        editor?.putInt("gameTimer", gameSettings.gameTimer)?.apply()
    }

    fun getSavedGameSettings(): GameSettings {
        val matches = sharedPreferences?.getInt("matches", 3)
        val startingColor = sharedPreferences?.getInt("startingColor", 0)
        val gameTimer = sharedPreferences?.getInt("gameTimer", 1)

        return GameSettings(matches!!, startingColor!!, gameTimer!!)
    }

    companion object {
        private var instance: GameSettingsSaver? = null
        private var sharedPreferences: SharedPreferences? = null

        fun getInstance(context: Context): GameSettingsSaver {
            if (instance == null) {
                sharedPreferences =
                    context.getSharedPreferences("tic_tak_toe_prefs", Context.MODE_PRIVATE)
                instance = GameSettingsSaver()
            }

            return GameSettingsSaver()
        }
    }
}