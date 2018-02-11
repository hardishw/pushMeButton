package com.hwgames.pushmebutton

import android.app.Fragment
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.ads.*
import kotlinx.android.synthetic.main.result.*

/**
 * Created by hardi on 14/01/2018.
 */
class ResultFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val handler = Handler()
        val view = inflater!!.inflate(R.layout.result,container,false)
        view.findViewById<AdView>(R.id.adView2).loadAd(AdRequest.Builder().build())
        val gameActivity = activity as GameActivity
        val result = view.findViewById<TextView>(R.id.result)
        val btnNextLevel = view.findViewById<Button>(R.id.btnNextLevel)
        val btnRetry = view.findViewById<Button>(R.id.btnRetry)
        val backToMenu = view.findViewById<Button>(R.id.backToMenu)
        val score = view.findViewById<TextView>(R.id.score)
        val sharedPref = gameActivity.getSharedPreferences("game",0)
        score.text = getString(R.string.score) + sharedPref.getInt("score",0)
        btnNextLevel.isEnabled = false

        if (gameActivity.currentLevel % 5 == 0) {
            val mInterstitial = gameActivity.mInterstitial
            while (!mInterstitial.isLoaded && !gameActivity.failedAd) {
                continue
            }
            mInterstitial.show()
        }

            if (gameActivity.winner){
                result.text = getString(R.string.winner)
                result.setTextColor(Color.GREEN)
                btnNextLevel.setOnClickListener({
                    gameActivity.nextLevel()
                })
                handler.postDelayed({
                    btnNextLevel.isEnabled = (gameActivity.currentLevel < gameActivity.maxLevel)
                },1000)

            } else {
                result.text = getString(R.string.loser)
                result.setTextColor(Color.RED)
                btnNextLevel.visibility = View.INVISIBLE
            }

            backToMenu.setOnClickListener({
                gameActivity.finish()
            })


            btnRetry.setOnClickListener({
                gameActivity.retry()
            })



        return view
    }
}