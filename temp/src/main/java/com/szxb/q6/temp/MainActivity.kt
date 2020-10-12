package com.szxb.q6.temp

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.yl.recyclerview.listener.OnScrollListener
import com.yl.recyclerview.wrapper.LoadMoreWrapper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mDataList = ArrayList<String>()
        for (i in 0..30) {
            mDataList.add("Item${i + 1}")
        }

        val commonAdapter = CommonAdapter(mDataList)
        val mLoadMoreWrapper = LoadMoreWrapper(commonAdapter)

//        val linearLayoutManager = LinearLayoutManager(this)
        val linearLayoutManager = GridLayoutManager(this,2)
        //线性布局
        recycler_view.layoutManager = linearLayoutManager
        recycler_view.adapter = mLoadMoreWrapper

        recycler_view.addOnScrollListener(object : OnScrollListener() {
            override fun onLoadMore() {
                mLoadMoreWrapper.setLoadStateNotify(mLoadMoreWrapper.LOADING)
                Handler().postDelayed({
                    mDataList.add(System.currentTimeMillis().toString())
                    mLoadMoreWrapper.setLoadStateNotify(mLoadMoreWrapper.LOADING_COMPLETE)
                }, 1500)
            }

        })

    }
}
