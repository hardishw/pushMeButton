package com.hwgames.pushmebutton

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createMenu()
    }

    private fun createMenu(){
        setContentView(R.layout.activity_main)

        startGame.text = "Start Game"
        startGame.setOnClickListener({
            val intent = Intent(this,GameActivity::class.java)
            startActivity(intent)
        })
    }
}
