package com.ragingo.sample.tcpclient

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = CommentaryTabPagerAdapter(this.supportFragmentManager)
        NiconicoJikkyo.TvChannels.forEach {
            adapter.addPage(it)
        }

        tabPager.adapter = adapter
        tabLayout.setupWithViewPager(tabPager)
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
    }
}
