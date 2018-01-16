package com.hwgames.pushmebutton


import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import java.lang.Math.round
import java.util.*
import kotlin.collections.HashMap


/**
 * Created by hardi on 14/01/2018.
 */
class GameFragment : Fragment() {
    val buttonMap = HashMap<Button,Int>()
    var stage = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.game,container,false)
        val gameActivity = activity as GameActivity
        stage = gameActivity.stage
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val pushMe1 = view.findViewById<Button>(R.id.pushMe1)
        val pushMe2 = view.findViewById<Button>(R.id.pushMe2)

        var switchColours:CountDownTimer? = null

        progressBar.progress = 0
        buttonMap.put(pushMe1,R.color.green)
        buttonMap.put(pushMe2,R.color.red)
        pushMe1.setBackgroundResource(R.color.green)

        if (stage != 2){
            pushMe2.visibility = View.INVISIBLE
        } else{
            pushMe2.setBackgroundResource(R.color.red)
            moveButton(view,pushMe2.id)
            switchColours = object: CountDownTimer(gameActivity.time.toLong(),600){
                override fun onFinish() {

                }

                override fun onTick(p0: Long) {
                    for (button in buttonMap.keys){
                        switchColour(button)
                    }
                }
            }.start()
        }
        moveButton(view,pushMe1.id)



        val timer = object: CountDownTimer(gameActivity.time.toLong(),10){
            override fun onFinish() {
                progressBar.progress = 100
                gameActivity.displayResult(false)
            }

            override fun onTick(p0: Long) {
                progressBar.progress = (round(100.0 - ((p0.toDouble()/gameActivity.time.toDouble())*100))).toInt()
            }
        }.start()



        pushMe1.setOnClickListener({
            timer.cancel()
            if (switchColours != null){
                switchColours!!.cancel()
            }
            onClick(pushMe1,gameActivity)
        })

        pushMe2.setOnClickListener({
            timer.cancel()
            if (switchColours != null){
                switchColours!!.cancel()
            }
            onClick(pushMe2,gameActivity)
        })

        return view
    }

    private fun moveButton(view: View, button: Int){
        val random = Random()
        val constraintLayout = view.findViewById<View>(R.id.constraint_layout) as ConstraintLayout
        val constraintSet = ConstraintSet()

        constraintSet.clone(constraintLayout)
        constraintSet.setVerticalBias(button, random.nextFloat())
        constraintSet.setHorizontalBias(button, random.nextFloat())
        constraintSet.applyTo(constraintLayout)
    }

    private fun switchColour(button: Button){
        if (buttonMap[button] == R.color.green){
            button.setBackgroundResource(R.color.red)
            buttonMap.put(button,R.color.red)
        } else {
            button.setBackgroundResource(R.color.green)
            buttonMap.put(button,R.color.green)
        }
    }

    private fun onClick(button: Button, gameActivity: GameActivity){
        if (buttonMap[button] == R.color.green || stage == 1){
            gameActivity.displayResult(true)
            val sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return
            sharedPref.edit().putInt("level", (activity as GameActivity).currentLevel + 1).commit()
        } else {
            gameActivity.displayResult(false)
        }
    }
}