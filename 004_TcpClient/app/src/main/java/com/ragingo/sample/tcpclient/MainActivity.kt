package com.ragingo.sample.tcpclient

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

object NiconicoJikkyo {

    val TvChannels = arrayOf(
        "jk1" to "NHK総合",
        "jk2" to "Eテレ",
        "jk4" to "日テレ",
        "jk5" to "テレ朝日",
        "jk6" to "TBS",
        "jk7" to "テレ東",
        "jk8" to "フジテレビ",
        "jk9" to "MX",
        "jk10" to "テレ玉",
        "jk11" to "tvk",
        "jk12" to "チバテレビ"
    )

}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = TabPagerAdapter(this.supportFragmentManager)
        NiconicoJikkyo.TvChannels.forEach {
            adapter.addPage(it.second, CommentaryListFragment::class)
        }

        tabPager.adapter = adapter
        tabLayout.setupWithViewPager(tabPager)
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
    }
}
