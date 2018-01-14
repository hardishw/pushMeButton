package com.hwgames.pushmebutton


import android.app.Fragment
import android.os.Bundle
import android.os.CountDownTimer
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.game.*
import java.lang.Math.round
import java.util.*

/**
 * Created by hardi on 14/01/2018.
 */
class GameFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.game,container,false)
        val gameActivity = activity as GameActivity
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val pushMe = view.findViewById<Button>(R.id.pushMe)
        progressBar.progress = 0

        val timer = object: CountDownTimer(gameActivity.time.toLong(),10){
            override fun onFinish() {
                progressBar.progress = 100
                gameActivity.displayResult(false)
            }

            override fun onTick(p0: Long) {
                progressBar.progress = (round(100.0 - ((p0.toDouble()/gameActivity.time.toDouble())*100))).toInt()
            }
        }.start()


        moveButton(view)
        pushMe.setOnClickListener({
            timer.cancel()
            gameActivity.displayResult(true)
        })

        return view
    }

    private fun moveButton(view: View){
        val random = Random()
        val constraintLayout = view.findViewById<View>(R.id.constraint_layout) as ConstraintLayout
        val constraintSet = ConstraintSet()

        constraintSet.clone(constraintLayout)
        constraintSet.setVerticalBias(R.id.pushMe, random.nextFloat())
        constraintSet.setHorizontalBias(R.id.pushMe, random.nextFloat())
        constraintSet.applyTo(constraintLayout)
    }
}