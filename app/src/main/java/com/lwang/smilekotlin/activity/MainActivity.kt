package com.lwang.smilekotlin.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.lwang.smilekotlin.App
import com.lwang.smilekotlin.R
import com.lwang.smilekotlin.fragment.GIfFragment
import com.lwang.smilekotlin.fragment.PicFragment
import com.lwang.smilekotlin.fragment.TextFragment
import com.lwang.smilekotlin.utils.ToastUtil
import com.yhao.commen.preference
import com.yhao.commen.util.FileUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.uiThread
import java.io.File
import kotlin.properties.Delegates

/**
 *主界面
 */
class MainActivity : AppCompatActivity() {

    var mIsMenuOpen: Boolean = false

    /**
     * 存储fragment的数组
     */
    val mFragments: Array<Fragment> = arrayOf(TextFragment(), PicFragment(), GIfFragment())

    /**
     * 存储最后一次打开的fragment
     */
    var mDefaultIndex: Int by preference(this@MainActivity, "sp_key_default_fragment", 0)

    /**
     * 属性代理
     * 观察者模式,当属性被赋值以后,可对相关的事件进行观察：
     */
    var mCurrentIndex: Int by Delegates.observable(0) { _, _, new ->

        navigationView.setCheckedItem(when (new) {
            0 -> R.id.nav_text
            1 -> R.id.nav_pic
            2 -> R.id.nav_gif
            else -> R.id.nav_text
        })
    }

    /**
     * 属性代理
     * 观察者模式,当属性被赋值以后,可对相关的事件进行观察：
     * 判断点击返回按钮的时间差
     */
    var mBackPressedTime by Delegates.observable(0L) { _, old, new ->

        if (new - old > 1000) {
            ToastUtil.showSnackbar(coordinatorLayout, getString(R.string.exit_message))
        }
        if (new - old in 1..1000) {
            mDefaultIndex = mCurrentIndex
            finish()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        init()
    }


    private fun init() {

        //设置toolbar并设置给drawerLayout
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // 处理点击事件
        navigationView.setNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.nav_text -> switchFragment(0, item.title.toString(), item)
                R.id.nav_pic -> switchFragment(1, item.title.toString(), item)
                R.id.nav_gif -> switchFragment(2, item.title.toString(), item)
                R.id.nav_clear -> clearCache(item)
                R.id.nav_about -> {
                    val intent = Intent(this, AboutActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.in_from_right, R.anim.stay)
                    true
                }
                else -> false
            }
        }

        // 监听drawerLayout的状态
        drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {

            override fun onDrawerClosed(drawerView: View?) {
                navigationView.menu.findItem(R.id.nav_clear).title = "清理缓存"
                mIsMenuOpen = false
            }

            override fun onDrawerOpened(drawerView: View?) {
                mIsMenuOpen = true
                doAsync {

                    //得到图片和app缓存
                    val glideCacheDir = Glide.getPhotoCacheDir(this@MainActivity) as File
                    var totalSize: Long = FileUtil.getFolderSize(glideCacheDir)
                    totalSize += FileUtil.getFolderSize(App.instance.cacheDir)

                    uiThread {
                        navigationView.menu.findItem(R.id.nav_clear).title = "清理缓存${FileUtil.getPrintSize(totalSize)}"
                    }
                }
//                (mFragments[2] as GIfFragment).pas
            }
        })

        mCurrentIndex = mDefaultIndex
        supportFragmentManager.beginTransaction()
                .replace(R.id.content, mFragments[mCurrentIndex])
                .commit()
        Glide.with(this).load(R.drawable.yhaolpz).into(navigationView.getHeaderView(0).find(R.id.avatar))
    }


    /**
     * 处理NavigationItem点击事件
     */
    private fun switchFragment(index: Int, title: String, item: MenuItem): Boolean {

        if (index != mCurrentIndex) {
            val trx = supportFragmentManager.beginTransaction()
            trx.hide(mFragments[mCurrentIndex])

            //如果这个fragment没有被添加到activity中，那么就添加次fragment
            if (!mFragments[index].isAdded) {
                trx.add(R.id.content, mFragments[index])
            }

            trx.show(mFragments[index]).commit()
            item.isChecked = true
            toolbar.title = title
            mCurrentIndex = index
        }
        drawerLayout.closeDrawers()
        return true
    }


    /**
     * 清理缓存
     */
    private fun clearCache(item: MenuItem): Boolean {

        item.title = "正在清理..."
        doAsync {

            //得到图片和app缓存
            val glideCacheDir = Glide.getPhotoCacheDir(this@MainActivity) as File
            var totalSize: Long = FileUtil.getFolderSize(glideCacheDir)
            totalSize += FileUtil.getFolderSize(App.instance.cacheDir)

            uiThread {
                if (totalSize == 0L) {
                    item.title = "清理完成"
                    return@uiThread
                }

                item.title = "正在清理${FileUtil.getPrintSize(totalSize)}..."
                doAsync {
                    FileUtil.deleteFolderFile(object : FileUtil.Companion.DeleteListener {

                        override fun onDone() {
                            uiThread {
                                item.title = "清理完成"
                            }
                        }

                        override fun onDelete(size: Long) {
                            uiThread {
                                totalSize -= size
                                item.title = "正在清理${FileUtil.getPrintSize(totalSize)}..."
                            }
                        }
                    }, glideCacheDir, App.instance.cacheDir)
                }
            }
        }
        return true
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            mBackPressedTime = if (mIsMenuOpen) {
                drawerLayout.closeDrawers()
                mBackPressedTime
            } else {
                System.currentTimeMillis()
            }
        }
        return true
    }

}


