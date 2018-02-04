package com.hwgames.pushmebutton

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.result.*
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

        val sharedPref = getSharedPreferences("game",0)
        currentLevel = sharedPref.getInt("level",1) - 1
        stage = sharedPref.getInt("stage",1)
        Log.w("activity", "level " + currentLevel)
        if (currentLevel + 1 >= maxLevel) {
            displayResult(true)
        } else {
            nextLevel()
        }
    }

    internal fun nextLevel() {
        currentLevel++






            time = when {
                currentLevel % 10 == 1 -> 10000
                currentLevel % 10 == 0 -> 1000
                else -> 10000 - ((currentLevel % 10) * 1000)
            }

            when {
                currentLevel < 11 -> {
                    showLevel()
                    game(1500)
                }
                currentLevel < 21 -> {
                    stage = 2
                    showLevel()
                    game(1500)
                }
                currentLevel < 31 -> {
                    stage = 3
                    showLevel()
                    game(1500)
                }
                currentLevel < 41 -> {
                    stage = 4
                    val handler = Handler()
                    showLevel()
                    time /= 2
                    time +=100
                    colour = colours[random.nextInt(colours.size)]
                    handler.postDelayed({
                        fragmentManager.beginTransaction().replace(R.id.frag, FlashFragment()).commit()
                    },1500)
                    game(1600)
                }
            }


    }

    internal fun displayResult(winner: Boolean) {
        this.winner = winner
        if (!isFinishing) fragmentManager.beginTransaction().replace(R.id.frag, ResultFragment()).commit()
    }

    private fun game(delay:Long) {
        val handler = Handler()
        handler.postDelayed({
        if (!isFinishing) fragmentManager.beginTransaction().replace(R.id.frag, GameFragment()).commit()
        }, delay)
    }

    private fun showLevel(){
        if (!isFinishing) fragmentManager.beginTransaction().replace(R.id.frag, LevelFragment()).commit()
    }

    fun retry() {
        currentLevel -= 1
        nextLevel()
    }
}