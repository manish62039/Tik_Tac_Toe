package com.example.tik_tak_toe.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tik_tak_toe.R
import com.example.tik_tak_toe.fragments.StartGameFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectFragment()
    }

    private fun selectFragment() {
        val fr = supportFragmentManager.beginTransaction()
        fr.replace(R.id.frame_layout_main, StartGameFragment()).commit()
    }
}