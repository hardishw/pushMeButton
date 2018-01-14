package com.hwgames.pushmebutton

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.result.*
import java.util.*

/**
 * Created by hardi on 14/01/2018.
 */
class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        progressBar.progress = 0

        val timer = object: CountDownTimer(10000,100){
            var progress = 0
            override fun onFinish() {
                progressBar.progress = 100
                Thread.sleep(100)
                displayResult(false)
            }

            override fun onTick(p0: Long) {
                progress += 1
                progressBar.progress = progress
            }

        }.start()


        moveButton()
        pushMe.setOnClickListener({
            timer.cancel()
            displayResult(true)
        })
    }

    private fun moveButton(){
        val random = Random()
        val constraintLayout = findViewById<View>(R.id.constraint_layout) as ConstraintLayout
        val constraintSet = ConstraintSet()

        constraintSet.clone(constraintLayout)
        constraintSet.setVerticalBias(R.id.pushMe, random.nextFloat())
        constraintSet.setHorizontalBias(R.id.pushMe, random.nextFloat())
        constraintSet.applyTo(constraintLayout)
    }

    private fun displayResult(winner:Boolean){
        setContentView(R.layout.result)
        if (winner){
            result.text = "WINNER"
            result.setTextColor(Color.GREEN)
        } else {
            result.text = "LOSER"
            result.setTextColor(Color.RED)
        }

        backToMenu.text = "Back To Menu"
        backToMenu.setOnClickListener({
            finish()
        })
    }
}