package com.ragingo.sample.tcpclient

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class CommentaryTabPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    private val fragments: MutableList<NiconicoJikkyo.ChannelInfo> = mutableListOf()

    fun addPage(channelInfo: NiconicoJikkyo.ChannelInfo) {
        fragments.add(channelInfo)
    }

    override fun getItem(position: Int): Fragment? {
        val item = CommentaryListFragment()
        item.channelId = fragments[position].id
        return item
    }

    override fun getPageTitle(position: Int): CharSequence? = fragments[position].name

    override fun getCount(): Int = fragments.count()
}
