package com.lwang.smilekotlin.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lwang.smilekotlin.R
import com.lwang.smilekotlin.bean.Huaban
import com.yhao.commen.notNullSingleValue
import kotlinx.android.synthetic.main.fragment_joke_rhesis.*
import java.util.*
import kotlin.properties.Delegates

/**
 * Created by lwang on 17-12-11.
 */
class ClassifyPicFragment : Fragment() {

    private var mType: Int by notNullSingleValue()
    private var mData: MutableList<Huaban> = ArrayList()
    private var mPage: Int = 1
    private var mLoading by Delegates.observable(true) { _, _, new ->
        mSwipeRefreshLayout.isRefreshing = new
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pic_classify, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mType = arguments.getInt(EXTRA_TYPE)
        initView()
        initEvent()
        loadData()
    }

    private fun initView() {

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        (mRecyclerView.layoutManager as StaggeredGridLayoutManager).gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
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


    companion object {

        private val EXTRA_TYPE: String = "extra_type"
        fun newInstance(type: Int): ClassifyPicFragment {

            val mFragment = ClassifyPicFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_TYPE, type)
            mFragment.arguments = bundle
            return mFragment
        }

    }

}