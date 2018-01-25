package com.hwgames.pushmebutton

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log

/**
 * Created by hardi on 14/01/2018.
 */
class GameActivity : AppCompatActivity() {

    var currentLevel = 0
    var time = 0
    val MAX_LEVEL = 30
    var winner = false
    var stage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        currentLevel = sharedPref.getInt("level",1) - 1
        Log.w("activity", "level " + currentLevel)
        if (currentLevel + 1 >= MAX_LEVEL) {
            displayResult(true)
        } else {
            nextLevel()
        }
    }

    internal fun nextLevel() {
        currentLevel++
        if (!isFinishing) fragmentManager.beginTransaction().replace(R.id.frag, LevelFragment()).commit()

        val handler = Handler()

        handler.postDelayed({

            time = when {
                currentLevel % 10 == 1 -> 10000
                currentLevel % 10 == 0 -> 1000
                else -> 10000 - ((currentLevel % 10) * 1000)
            }

            when {
                currentLevel < 11 -> game()
                currentLevel < 21 -> {
                    stage = 2
                    game()
                }
                currentLevel < 31 -> {
                    stage = 3
                    game()
                }
            }

        }, 1500)
    }

    internal fun displayResult(winner: Boolean) {
        this.winner = winner
        if (!isFinishing) fragmentManager.beginTransaction().replace(R.id.frag, ResultFragment()).commit()
    }

    private fun game() {
        if (!isFinishing) fragmentManager.beginTransaction().replace(R.id.frag, GameFragment()).commit()
    }
}