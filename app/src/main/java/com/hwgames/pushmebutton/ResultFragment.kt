package com.hwgames.pushmebutton

import android.app.Fragment
import android.graphics.Color
import android.os.Bundle
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

        val view = inflater!!.inflate(R.layout.result,container,false)
        val gameActivity = activity as GameActivity
        val result = view.findViewById<TextView>(R.id.result)
        val btnNextLevel = view.findViewById<Button>(R.id.btnNextLevel)
        val btnRetry = view.findViewById<Button>(R.id.btnRetry)
        val backToMenu = view.findViewById<Button>(R.id.backToMenu)
        backToMenu.text = getString(R.string.menu)
        backToMenu.isEnabled = false
        btnRetry.text = getString(R.string.retry)
        btnRetry.isEnabled = false
        btnNextLevel.text = getString(R.string.next_level)
        btnNextLevel.isEnabled = false

        if (gameActivity.currentLevel % 5 == 0){
            MobileAds.initialize(gameActivity,"ca-app-pub-7101862270937969~3612522125")
            val mInterstitial = InterstitialAd(gameActivity)
            mInterstitial.adUnitId = "ca-app-pub-7101862270937969/6771724858"
            mInterstitial.loadAd(AdRequest.Builder().build())
            mInterstitial.adListener = object : AdListener(){
                override fun onAdLoaded() {
                    mInterstitial.show()
                    backToMenu.isEnabled = true
                    btnRetry.isEnabled = true

                    if (gameActivity.winner){
                        result.text = getString(R.string.winner)
                        result.setTextColor(Color.GREEN)
                        btnNextLevel.setOnClickListener({
                            gameActivity.nextLevel()
                        })
                        btnNextLevel.isEnabled = (gameActivity.currentLevel < gameActivity.maxLevel)
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
                }

                override fun onAdFailedToLoad(p0: Int) {
                    mInterstitial.show()
                    backToMenu.isEnabled = true
                    btnRetry.isEnabled = true

                    if (gameActivity.winner){
                        result.text = getString(R.string.winner)
                        result.setTextColor(Color.GREEN)
                        btnNextLevel.setOnClickListener({
                            gameActivity.nextLevel()
                        })
                        btnNextLevel.isEnabled = (gameActivity.currentLevel < gameActivity.maxLevel)
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
                }
            }
        } else {
            backToMenu.isEnabled = true
            btnRetry.isEnabled = true

            if (gameActivity.winner){
                result.text = getString(R.string.winner)
                result.setTextColor(Color.GREEN)
                btnNextLevel.setOnClickListener({
                    gameActivity.nextLevel()
                })
                btnNextLevel.isEnabled = (gameActivity.currentLevel < gameActivity.maxLevel)
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
        }

        return view
    }
}