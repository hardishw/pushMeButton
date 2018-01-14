package com.hwgames.pushmebutton

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.game.*
import kotlinx.android.synthetic.main.result.*
import java.util.*

/**
 * Created by hardi on 14/01/2018.
 */
class GameActivity : AppCompatActivity() {

    var currentLevel = 0
    var time = 0
    var winner = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        nextLevel()
    }

    internal fun nextLevel(){
        currentLevel++
        fragmentManager.beginTransaction().replace(R.id.frag,LevelFragment()).commit()

        val handler = Handler()

        handler.postDelayed({

            when(currentLevel){
                1 -> {
                    time = 10000
                    game()
                }
                2 -> {
                    time = 9000
                    game()
                }
                3 -> {
                    time = 8000
                    game()
                }
                4 -> {
                    time = 7000
                    game()
                }
                5 -> {
                    time = 6000
                    game()
                }
                6 -> {
                    time = 5000
                    game()
                }
                7 -> {
                    time = 4000
                    game()
                }
                8 -> {
                    time = 3000
                    game()
                }
                9 -> {
                    time = 2000
                    game()
                }
                10 -> {
                    time = 1000
                    game()
                }
            }

        },1500)
    }

    internal fun displayResult(winner:Boolean){
        this.winner = winner
        fragmentManager.beginTransaction().replace(R.id.frag,ResultFragment()).commit()
    }

    private fun game(){
        fragmentManager.beginTransaction().replace(R.id.frag,GameFragment()).commit()
    }
}