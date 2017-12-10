package com.lwang.smilekotlin.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.lwang.smilekotlin.R
import com.lwang.smilekotlin.bean.Rhesis
import kotlinx.android.synthetic.main.fragment_joke_rhesis.*
import kotlin.properties.Delegates


/**
 * Created by lwang on 17-12-9.
 */
class RhesisFragment : Fragment() {

    private var mData: MutableList<Rhesis> = ArrayList()

    private var mPage: Int = 1

    private var mLoading by Delegates.observable(true) { _, _, new ->
        mSwipeRefreshLayout.isRefreshing = new
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_joke_rhesis, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initEvent()
    }

    private fun initView() {

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
    }


    private fun initEvent() {
        mSwipeRefreshLayout.setOnRefreshListener {
            mPage = 1
            loadData()
        }

        mRecyclerView.setOnTouchListener { _, _ ->
            if (!mLoading && !mRecyclerView.canScrollVertically(1)) {
                mPage++
                loadData()
            }
            false
        }
    }

    private fun loadData() {

    }


}