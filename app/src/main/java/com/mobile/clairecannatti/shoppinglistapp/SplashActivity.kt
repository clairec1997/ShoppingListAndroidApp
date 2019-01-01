package com.mobile.clairecannatti.shoppinglistapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val anim = AnimationUtils.loadAnimation(this, R.anim.anim_splash)

        var intentStart = Intent()
        intentStart.setClass(SplashActivity@this, ScrollingActivity::class.java)

        anim.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                startActivity(intentStart)
            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })

        ivSplash.startAnimation(anim)

    }
}
