package com.hwgames.pushmebutton


import android.annotation.SuppressLint
import android.app.Fragment
import android.os.Bundle
import android.os.CountDownTimer
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import java.lang.Math.round
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


/**
 * Created by hardi on 14/01/2018.
 */
class GameFragment : Fragment() {
    val buttons = ArrayList<GameButton>()
    val area = HashMap<ImageView,Int>()
    val collisions = HashMap<GameButton,Boolean>()
    var stage = 0
    var switchColours:CountDownTimer? = null
    var timer:CountDownTimer? = null
    var moveButtons:CountDownTimer? = null
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
                        for (button in buttons){
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
                        for (button in buttons){
                            switchColour(button)
                        }
                    }
                }.start()

                moveButtons = object: CountDownTimer(gameActivity.time.toLong(),1200){
                    override fun onFinish() {

                    }

                    override fun onTick(p0: Long) {
                        for (button in buttons){
                            button.move()
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
//                        for (button in buttons.keys){
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
                        for (button in buttons){
                            val colour = random.nextInt(colours.size)
                            button.color = colours[colour]
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
                    onClick((finish as GameButton?)!!,gameActivity)
                })
                for (button in collisions.keys){
                    for (areas in area.keys){
                        while(collision(areas,button)){
                            button.move()
                        }
                    }
                }
            }
        }

        return view
    }

    private fun createButton(view: View, activity: GameActivity, colour: Int):GameButton {
        val layout = view.findViewById<ConstraintLayout>(R.id.constraint_layout)
        val button = GameButton(activity,layout,colour)
        button.setOnClickListener({
            if (timer != null){
                timer!!.cancel()
            }
            if (switchColours != null){
                switchColours!!.cancel()
            }
            onClick(button,activity)
        })

        buttons.add(button)

        return button
    }

    private fun switchColour(button: GameButton){
        if (button.color == R.color.green){
            button.color = (R.color.red)
        } else {
            button.color = R.color.green
        }
    }

    @SuppressLint("ApplySharedPref")
    private fun onClick(button: GameButton, gameActivity: GameActivity){
        var colour = gameActivity.colour
        if (colour == null) colour = R.color.green
        if (button.color == colour || stage == 4){
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
                        if (collision(areas,v as GameButton)){
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

    private fun collision(view1:ImageView, view2:GameButton):Boolean{
        val x = view1.x + view1.width
        val y = view1.y + view1.height

        if (area[view1] == view2.color){
            if (view2.x > view1.x && view2.x < x){
                if (view2.y > view1.y && view2.y < y){
                    return true
                }
            }
        }

        return false
    }
}