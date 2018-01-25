package com.hwgames.pushmebutton

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import java.util.*

/**
 * Created by hardi on 14/01/2018.
 */
class GameActivity : AppCompatActivity() {

    var currentLevel = 0
    var time = 0
    val maxLevel = 40
    var winner = false
    var stage = 1
    private val colours = intArrayOf(R.color.red,R.color.green,R.color.orange,R.color.blue,R.color.yellow,R.color.purple)
    var colour:Int? = null
    private val random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        currentLevel = sharedPref.getInt("level",1) - 1
        Log.w("activity", "level " + currentLevel)
        if (currentLevel + 1 >= maxLevel) {
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
                currentLevel < 41 -> {
                    time /= 2
                    Log.w("gameactivity",time.toString())
                    colour = colours[random.nextInt(colours.size)]
                    fragmentManager.beginTransaction().replace(R.id.frag, FlashFragment()).commit()
                    stage = 4
                    handler.postDelayed({
                        game()
                    },100)
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