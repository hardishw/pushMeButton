package com.hwgames.pushmebutton

import android.content.Context
import android.content.res.Configuration
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Button
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by hardi on 11/02/2018.
 */
class GameButton : Button{

    companion object {
        private val idGenerator = AtomicInteger(0)
    }

    private val normal = 40f
    private val large = 60f
    private val xlarge = 80f
    lateinit var layout: ConstraintLayout
    var color: Int? = null
        set(value) {
            if (value != null) {
                field = value
                setBackgroundResource(value)
            }
        }

    constructor(activity: GameActivity, layout: ConstraintLayout, color: Int):super(activity){
        this.color = color
        this.layout = layout
        id = idGenerator.addAndGet(1)
        layout.addView(this)
        val constraintSet = ConstraintSet()
        constraintSet.clone(layout)
        constraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        constraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        constraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSet.connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        constraintSet.constrainHeight(id,size())
        constraintSet.constrainWidth(id,size())
        constraintSet.applyTo(layout)
        move()
    }

    constructor(context: Context):super(context)

    constructor(context: Context, attributeSet: AttributeSet):super(context,attributeSet)

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int):super(context,attributeSet,defStyle)

    private fun size():Int{
        return when ((resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK)){
             Configuration.SCREENLAYOUT_SIZE_XLARGE -> TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, xlarge, resources
                    .displayMetrics).toInt()
            Configuration.SCREENLAYOUT_SIZE_LARGE -> TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, large, resources
                    .displayMetrics).toInt()
            else -> TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, normal, resources
                    .displayMetrics).toInt()
        }
    }

    fun move(){
        val random = Random()
        val constraintSet = ConstraintSet()

        constraintSet.clone(layout)
        constraintSet.setVerticalBias(id, random.nextFloat())
        constraintSet.setHorizontalBias(id, random.nextFloat())
        constraintSet.applyTo(layout)
    }
}