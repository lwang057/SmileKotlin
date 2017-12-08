package com.lwang.smilekotlin

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.lwang.smilekotlin.fragment.GIfFragment
import com.lwang.smilekotlin.fragment.PicFragment
import com.lwang.smilekotlin.fragment.TextFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.jetbrains.anko.doAsync
import java.io.File

/**
 *主界面
 */
class MainActivity : AppCompatActivity() {

    var mIsMenuOpen: Boolean = false
    val mFragments: Array<Fragment> = arrayOf(TextFragment(), PicFragment(), GIfFragment())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        init()
    }


    private fun init() {

        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.nav_text -> switchFragment(0, item.title.toString(), item)
                R.id.nav_pic -> switchFragment(1, item.title.toString(), item)
                R.id.nav_gif -> switchFragment(2, item.title.toString(), item)
                R.id.nav_clear -> clearCache(item)
                R.id.nav_about -> {
                    val intent = Intent(this, KotlinActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.in_from_right, R.anim.stay)
                    true
                }
                else -> false
            }
        }

        drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {

            override fun onDrawerClosed(drawerView: View?) {
                navigationView.menu.findItem(R.id.nav_clear).title = "清理缓存"
                mIsMenuOpen = false
            }

            override fun onDrawerOpened(drawerView: View?) {
                mIsMenuOpen = true
                doAsync {

                    val glideCacheDir = Glide.getPhotoCacheDir(this@MainActivity) as File
//                    var totalSize: Long = getFolderSize(glideCacheDir)
                }
            }
        })

    }



    private fun clearCache(item: MenuItem): Boolean {

        item.title="正在清理..."
        doAsync {

        }
        return true
    }

    private fun switchFragment(index: Int, title: String, item: MenuItem): Boolean {

        return true
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return super.onKeyDown(keyCode, event)
    }


    override fun onPause() {
        super.onPause()
    }


}
