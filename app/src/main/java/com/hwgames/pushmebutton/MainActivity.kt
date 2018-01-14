package com.hwgames.pushmebutton

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar.progress = 0

        val timer = object:CountDownTimer(10000,100){
            var progress = 0
            override fun onFinish() {
                progressBar.progress = 100
                Thread.sleep(100)
                setContentView(R.layout.activity_loser)
            }

            override fun onTick(p0: Long) {
                progress += 1
                progressBar.progress = progress
            }

        }.start()


        moveButton()
        pushMe.setOnClickListener({
            timer.cancel()
            setContentView(R.layout.activity_winner)
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
}
