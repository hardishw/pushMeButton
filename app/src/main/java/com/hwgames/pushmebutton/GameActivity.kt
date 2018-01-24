package com.hwgames.pushmebutton

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity

/**
 * Created by hardi on 14/01/2018.
 */
class GameActivity : AppCompatActivity() {

    var currentLevel = 0
    var time = 0
    val MAX_LEVEL = 20
    var winner = false
    var stage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        currentLevel = sharedPref.getInt("level",1) - 1
        if (currentLevel == 1) currentLevel = 0
        nextLevel()
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

            if (currentLevel < 11) {
                game()
            } else if (currentLevel < 21) {
                stage = 2
                game()
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