package com.ragingo.sample.tcpclient

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class CommentaryTabPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val fragments: MutableList<NiconicoJikkyo.ChannelInfo> = mutableListOf()

    fun addPage(channelInfo: NiconicoJikkyo.ChannelInfo) {
        fragments.add(channelInfo)
    }

    override fun getItem(position: Int): Fragment {
        val item = CommentaryListFragment()
        item.channelId = fragments[position].id
        return item
    }

    override fun getPageTitle(position: Int): CharSequence? = fragments[position].name

    override fun getCount(): Int = fragments.count()
}
