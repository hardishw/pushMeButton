package com.hwgames.pushmebutton

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this,"ca-app-pub-3940256099942544~3347511713")
        val mInterstitial = InterstitialAd(this)
        mInterstitial.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitial.loadAd(AdRequest.Builder().build())
        mInterstitial.adListener = object : AdListener(){
            override fun onAdLoaded() {
                mInterstitial.show()
            }
        }
        createMenu()

        reset.setOnClickListener {
            val sharedPref = getSharedPreferences("level", 0)
            sharedPref.edit().putInt("level", 1).commit()
        }
    }

    private fun createMenu(){
        setContentView(R.layout.activity_main)

        startGame.setOnClickListener({
            val intent = Intent(this,GameActivity::class.java)
            startActivity(intent)
        })
    }
}
