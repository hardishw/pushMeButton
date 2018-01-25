package com.hwgames.pushmebutton

import android.app.Fragment
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

/**
 * Created by hardi on 14/01/2018.
 */
class ResultFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.result,container,false)
        val gameActivity = activity as GameActivity
        val result = view.findViewById<TextView>(R.id.result)
        val btnNextLevel = view.findViewById<Button>(R.id.btnNextLevel)
        val backToMenu = view.findViewById<Button>(R.id.backToMenu)

        if (gameActivity.winner){
            result.text = getString(R.string.winner)
            result.setTextColor(Color.GREEN)
            btnNextLevel.text = getString(R.string.next_level)
            btnNextLevel.setOnClickListener({
                gameActivity.nextLevel()
            })
            btnNextLevel.isEnabled = (gameActivity.currentLevel < gameActivity.maxLevel)
        } else {
            result.text = getString(R.string.loser)
            result.setTextColor(Color.RED)
            btnNextLevel.visibility = View.INVISIBLE
        }

        backToMenu.text = getString(R.string.menu)
        backToMenu.setOnClickListener({
            gameActivity.finish()
        })
        return view
    }
}