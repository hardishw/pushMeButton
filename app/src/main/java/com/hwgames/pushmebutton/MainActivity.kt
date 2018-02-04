package com.hwgames.pushmebutton

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var sharedPref:SharedPreferences

    @SuppressLint("ApplySharedPref")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = getSharedPreferences("game", 0)
        MobileAds.initialize(this,"ca-app-pub-7101862270937969~3612522125")
        val mInterstitial = InterstitialAd(this)
        mInterstitial.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitial.loadAd(AdRequest.Builder().build())
        mInterstitial.adListener = object : AdListener(){
            override fun onAdLoaded() {
                mInterstitial.show()
            }
        }
        createMenu()
        updateScore()

        reset.setOnClickListener {
            sharedPref.edit().putInt("level", 1).commit()
            sharedPref.edit().putInt("stage", 1).commit()
            sharedPref.edit().putInt("score", 0).commit()
            updateScore()
        }
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        updateScore()
    }

    override fun onResume() {
        super.onResume()
        updateScore()
    }

    private fun createMenu(){
        setContentView(R.layout.activity_main)

        startGame.setOnClickListener({
            val intent = Intent(this,GameActivity::class.java)
            startActivity(intent)
        })
    }

    private fun updateScore(){
        val score = this.findViewById<TextView>(R.id.score)
        score.text = getString(R.string.score) + sharedPref.getInt("score",0)
    }
}
