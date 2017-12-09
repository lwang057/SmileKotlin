package com.lwang.smilekotlin.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lwang.smilekotlin.R
import kotlinx.android.synthetic.main.fragment_pic_text.*

/**
 * Created by lwang on 17-12-8.
 */
class TextFragment : Fragment() {

    val tabs: Array<String> = arrayOf("搞笑段子", "励志鸡汤")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pic_text, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewPager.offscreenPageLimit = tabs.size

        mViewPager.adapter = TextPageAdapter(childFragmentManager)
        mTabLayout.tabMode = TabLayout.MODE_FIXED
        mTabLayout.tabGravity = TabLayout.GRAVITY_FILL
        mTabLayout.setupWithViewPager(mViewPager)
    }


    private inner class TextPageAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> JokeFragment()
                1 -> RhesisFragment()
                else -> RhesisFragment()
            }
        }

        override fun getPageTitle(position: Int): CharSequence {
            return tabs[position]
        }

        override fun getCount(): Int {
            return tabs.size
        }

    }


}