package com.hwgames.pushmebutton

import android.app.Fragment
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by HWILKHU on 25/01/2018.
 */
class FlashFragment : Fragment() {



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var view = inflater!!.inflate(R.layout.colour,container,false)
        val gameActivity = activity as GameActivity
        view.findViewById<ConstraintLayout>(R.id.background).setBackgroundResource(gameActivity.colour!!)


        return view
    }
}