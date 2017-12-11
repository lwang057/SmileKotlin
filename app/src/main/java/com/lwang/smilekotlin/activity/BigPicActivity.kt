package com.lwang.smilekotlin.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.lwang.smilekotlin.R
import kotlinx.android.synthetic.main.activity_big_pic.*

class BigPicActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_big_pic)

        val url = intent.getStringExtra(EXTRA_URL)
        Glide.with(this)
                .load(url)
                .into(mImageView)

        mFrameLayout.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition()
            } else {
                finish()
            }
        }
    }


    companion object {

        val EXTRA_URL = "extra_url"

        fun launch(activity: AppCompatActivity, srcView: View, url: String) {

            val intent = Intent(activity, BigPicActivity::class.java)
            intent.putExtra(EXTRA_URL, url)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity, srcView, activity.getString(R.string.transitionName_pic))
            ActivityCompat.startActivity(activity, intent, options.toBundle())
        }

    }


    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition()
        } else {
            finish()
        }


    }

}
