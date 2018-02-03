package com.hwgames.pushmebutton


import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import java.lang.Math.round
import java.util.*
import kotlin.collections.HashMap
import android.util.TypedValue


/**
 * Created by hardi on 14/01/2018.
 */
class GameFragment : Fragment() {
    val buttonMap = HashMap<Button,Int>()
    var stage = 0
    var switchColours:CountDownTimer? = null
    var timer:CountDownTimer? = null
    var moveButtons:CountDownTimer? = null
    var buttonId = 0


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.game,container,false)
        val gameActivity = activity as GameActivity
        stage = gameActivity.stage
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)



        progressBar.progress = 0
        timer = object: CountDownTimer(gameActivity.time.toLong(),10){
            override fun onFinish() {
                progressBar.progress = 100
                gameActivity.displayResult(false)
            }

            override fun onTick(p0: Long) {
                progressBar.progress = (round(100.0 - ((p0.toDouble()/gameActivity.time.toDouble())*100))).toInt()
            }
        }.start()

        when(stage){
            1 ->{
                createButton(view,gameActivity,R.color.green)
            }
            2 -> {
                createButton(view,gameActivity,R.color.green)
                createButton(view,gameActivity,R.color.red)

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
            3 -> {
                createButton(view,gameActivity,R.color.green)
                createButton(view,gameActivity,R.color.red)

                switchColours = object: CountDownTimer(gameActivity.time.toLong(),600){
                    override fun onFinish() {

                    }

                    override fun onTick(p0: Long) {
                        for (button in buttonMap.keys){
                            switchColour(button)
                        }
                    }
                }.start()

                moveButtons = object: CountDownTimer(gameActivity.time.toLong(),1200){
                    override fun onFinish() {

                    }

                    override fun onTick(p0: Long) {
                        for (button in buttonMap.keys){
                            moveButton(view,button.id)
                        }
                    }
                }.start()
            }
            4 -> {

                createButton(view,gameActivity,R.color.green)
                createButton(view,gameActivity,R.color.red)
                createButton(view,gameActivity,R.color.purple)
                createButton(view,gameActivity,R.color.orange)
                createButton(view,gameActivity,R.color.blue)
                createButton(view,gameActivity,R.color.yellow)
            }
        }

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

    private fun createButton(view: View, activity: GameActivity, colour: Int):Button {
        val random = Random()
        val layout = view.findViewById<ConstraintLayout>(R.id.constraint_layout)
        val button = Button(activity)
        button.id = buttonId++
        Log.w("gamefragment","button "+button.id)
        button.setOnClickListener({
            if (timer != null){
                timer!!.cancel()
            }
            if (switchColours != null){
                switchColours!!.cancel()
            }
            onClick(button,activity)
        })
        layout.addView(button)
        button.setBackgroundResource(colour)
        val constraintSet = ConstraintSet()
        val size = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 40f, resources
                .displayMetrics).toInt()
        constraintSet.clone(layout)
        constraintSet.connect(button.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        constraintSet.connect(button.id,ConstraintSet.START,ConstraintSet.PARENT_ID,ConstraintSet.START)
        constraintSet.connect(button.id,ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP)
        constraintSet.connect(button.id,ConstraintSet.END,ConstraintSet.PARENT_ID,ConstraintSet.END)
        constraintSet.constrainHeight(button.id,size)
        constraintSet.constrainWidth(button.id,size)
        constraintSet.setVerticalBias(button.id, random.nextFloat())
        constraintSet.setHorizontalBias(button.id, random.nextFloat())
        constraintSet.applyTo(layout)


        buttonMap.put(button,colour)

        return button
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
        var colour = gameActivity.colour
        if (colour == null) colour = R.color.green
        if (buttonMap[button] == colour || stage == 1){
            gameActivity.displayResult(true)
            val sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return
            sharedPref.edit().putInt("level", (activity as GameActivity).currentLevel + 1).commit()
        } else {
            gameActivity.displayResult(false)
        }
    }
}