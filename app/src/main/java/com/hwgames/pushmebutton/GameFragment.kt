package com.hwgames.pushmebutton


import android.annotation.SuppressLint
import android.app.Fragment
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Math.round
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet


/**
 * Created by hardi on 14/01/2018.
 */
class GameFragment : Fragment() {
    val buttonMap = HashMap<Button,Int>()
    val area = HashMap<ImageView,Int>()
    val collisions = HashMap<Button,Boolean>()
    var stage = 0
    var switchColours:CountDownTimer? = null
    var timer:CountDownTimer? = null
    var moveButtons:CountDownTimer? = null
    var buttonId = 0
    var progress = 0
    lateinit var gameActivity:GameActivity
    var dx = 0f
    var dy = 0f
    var finish:Button? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view:View
        gameActivity = activity as GameActivity
        stage = gameActivity.stage
        if (stage == 4){
            if (gameActivity.currentLevel < 36){
                view = inflater!!.inflate(R.layout.game2,container,false)
                area[view.findViewById(R.id.greenbox)] = R.color.green
                area[view.findViewById(R.id.redbox)] = R.color.red
            } else {
                view = inflater!!.inflate(R.layout.game3,container,false)
                area[view.findViewById(R.id.greenbox)] = R.color.green
                area[view.findViewById(R.id.redbox)] = R.color.red
                area[view.findViewById(R.id.purplebox)] = R.color.purple
                area[view.findViewById(R.id.bluebox)] = R.color.blue
            }
            finish = view.findViewById(R.id.finish)
            finish!!.isEnabled = false
        } else {
            view = inflater!!.inflate(R.layout.game,container,false)
        }
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)



        progressBar.progress = 0
        timer = object: CountDownTimer(gameActivity.time.toLong(),10){
            override fun onFinish() {
                progressBar.progress = 100
                gameActivity.displayResult(false)
            }

            override fun onTick(p0: Long) {
                progress = (round(100.0 - ((p0.toDouble()/gameActivity.time.toDouble())*100))).toInt()
                progressBar.progress = progress
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
            5 -> {
                createButton(view,gameActivity,R.color.green)
                createButton(view,gameActivity,R.color.red)
                createButton(view,gameActivity,R.color.purple)
                createButton(view,gameActivity,R.color.orange)
                createButton(view,gameActivity,R.color.blue)
                createButton(view,gameActivity,R.color.yellow)
            }
            6 -> {
                createButton(view,gameActivity,R.color.green)
                createButton(view,gameActivity,R.color.red)
                createButton(view,gameActivity,R.color.purple)
                createButton(view,gameActivity,R.color.orange)
                createButton(view,gameActivity,R.color.blue)
                createButton(view,gameActivity,R.color.yellow)

//                moveButtons = object: CountDownTimer(gameActivity.time.toLong(),1200){
//                    override fun onFinish() {
//
//                    }
//
//                    override fun onTick(p0: Long) {
//                        for (button in buttonMap.keys){
//                            moveButton(view,button.id)
//                        }
//                    }
//                }.start()

                switchColours = object: CountDownTimer(gameActivity.time.toLong(),600){
                    val random = Random()

                    override fun onFinish() {

                    }

                    override fun onTick(p0: Long) {
                        val colours = ArrayList<Int>(listOf(R.color.red,R.color.green,R.color.orange,R.color.blue,R.color.yellow,R.color.purple))
                        for (button in buttonMap.keys){
                            val colour = random.nextInt(colours.size)
                            button.setBackgroundResource(colours[colour])
                            colours.removeAt(colour)
                        }
                    }
                }.start()
            }
            4 -> {
                if (gameActivity.currentLevel < 36){
                    collisions[createButton(view,gameActivity,R.color.green)] = false
                    collisions[createButton(view,gameActivity,R.color.red)] = false
                } else {
                    collisions[createButton(view,gameActivity,R.color.green)] = false
                    collisions[createButton(view,gameActivity,R.color.red)] = false
                    collisions[createButton(view,gameActivity,R.color.purple)] = false
                    collisions[createButton(view,gameActivity,R.color.blue)] = false
                }


                for (button in collisions.keys){
                    button.setOnTouchListener(drag())
                }
                finish!!.setOnClickListener({
                    if (timer != null){
                        timer!!.cancel()
                    }
                    if (switchColours != null){
                        switchColours!!.cancel()
                    }
                    onClick(finish!!,gameActivity)
                })
                for (button in collisions.keys){
                    for (areas in area.keys){
                        while(collision(areas,button)){
                            moveButton(view,button.id)
                        }
                    }
                }
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
        val size = if ((resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE){
            TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 80f, resources
                    .displayMetrics).toInt()
        } else {
            TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 40f, resources
                    .displayMetrics).toInt()
        }

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


        buttonMap[button] = colour

        return button
    }

    private fun switchColour(button: Button){
        if (buttonMap[button] == R.color.green){
            button.setBackgroundResource(R.color.red)
            buttonMap[button] = R.color.red
        } else {
            button.setBackgroundResource(R.color.green)
            buttonMap[button] = R.color.green
        }
    }

    @SuppressLint("ApplySharedPref")
    private fun onClick(button: Button, gameActivity: GameActivity){
        var colour = gameActivity.colour
        if (colour == null) colour = R.color.green
        if (buttonMap[button] == colour || stage == 1 || stage == 4){
            gameActivity.displayResult(true)
            val sharedPref = activity.getSharedPreferences("game",0)
            val currentScore = sharedPref.getInt("score",0)
            sharedPref.edit().putInt("level", (activity as GameActivity).currentLevel + 1).commit()
            sharedPref.edit().putInt("stage", stage).commit()
            sharedPref.edit().putInt("score", currentScore + ((100 - progress) * gameActivity.currentLevel)).commit()
        } else {
            gameActivity.displayResult(false)
        }
    }

    private fun drag():View.OnTouchListener{
        return View.OnTouchListener { v, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    dx = v.x - event.rawX
                    dy = v.y - event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    v.x = event.rawX + dx
                    v.y = event.rawY + dy
                }
                MotionEvent.ACTION_UP -> {
                    for (areas in area.keys){
                        if (collision(areas,v as Button)){
                            collisions[v] = true
                            break
                        } else {
                            collisions[v]= false
                        }
                    }

                    for (button in collisions.keys){
                        if (!collisions[button]!!){
                            finish!!.isEnabled = false
                            break
                        } else {
                            finish!!.isEnabled = true
                        }
                    }
                }
                else -> {
                    return@OnTouchListener false
                }
            }
            return@OnTouchListener true
        }
    }

    private fun collision(view1:ImageView, view2:Button):Boolean{
        val x = view1.x + view1.width
        val y = view1.y + view1.height

        if (area[view1] == buttonMap[view2]){
            if (view2.x > view1.x && view2.x < x){
                if (view2.y > view1.y && view2.y < y){
                    return true
                }
            }
        }

        return false
    }
}