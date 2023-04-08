package com.example.tik_tak_toe.utils

import android.os.Parcelable

data class GameSettings(
    var matches: Int = 3,
    var startingColor: Int = 0,
    var gameTimer: Int = 1,
    var score_of_blue: Int = 0,
    var score_of_red: Int = 0,
    var whoWinsLastMatch: Int = 0
)
