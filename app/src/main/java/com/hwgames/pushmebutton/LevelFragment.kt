package com.hwgames.pushmebutton

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by hardi on 14/01/2018.
 */
class LevelFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.level_display,container,false)
        val gameActivity = activity as GameActivity

        val txtLevel = view.findViewById<TextView>(R.id.txtLevel) as TextView
        txtLevel.text = getString(R.string.level) + gameActivity.currentLevel

        val txtStage= view.findViewById<TextView>(R.id.txtStage) as TextView
        txtStage.text = getString(R.string.stage) + gameActivity.stage

        val txtHint= view.findViewById<TextView>(R.id.hint) as TextView
        txtHint.text = gameActivity.hint


        return view
    }
}