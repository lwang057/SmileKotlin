package com.lwang.smilekotlin.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lwang.smilekotlin.R
import com.lwang.smilekotlin.activity.BigPicActivity
import com.lwang.smilekotlin.adapter.PicAdapter
import com.lwang.smilekotlin.api.PicApi
import com.lwang.smilekotlin.bean.Pic
import com.lwang.smilekotlin.utils.ToastUtil
import com.lwang.smilekotlin.utils.notNullSingleValue
import kotlinx.android.synthetic.main.fragment_joke_rhesis.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*
import kotlin.properties.Delegates

/**
 * Created by lwang on 17-12-11.
 */
class ClassifyPicFragment : Fragment() {

    private var mType: Int by notNullSingleValue()
    private var mData: MutableList<Pic> = ArrayList()
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

        mLoading = true
        doAsync {
            val data = PicApi.getData(mType, mPage)
            uiThread {
                mLoading = false
                if (data == null) {
                    ToastUtil.showSnackbar(view as ViewGroup, "加载失败")
                    return@uiThread
                }
                if (mRecyclerView.adapter == null) {
                    mData.addAll(data)
                    initAdapter()
                } else if (mPage > 1) {
                    val pos = mData.size
                    mData.addAll(data)
                    mRecyclerView.adapter.notifyItemRangeInserted(pos, data.size)
                } else {
                    mData.clear()
                    mData.addAll(data)
                    mRecyclerView.adapter.notifyDataSetChanged()
                }

            }
        }
    }

    private fun initAdapter() {

        mRecyclerView.adapter = PicAdapter(mData)
        (mRecyclerView.adapter as PicAdapter).setOnItemClick(object : PicAdapter.OnItemClickLister{

            override fun onClick(view: View, url: String) {
                BigPicActivity.launch(this@ClassifyPicFragment.activity as AppCompatActivity, view, url)
            }
        })
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