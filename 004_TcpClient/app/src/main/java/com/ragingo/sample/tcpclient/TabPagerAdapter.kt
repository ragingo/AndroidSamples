package com.ragingo.sample.tcpclient

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import kotlin.reflect.KClass

class TabPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val fragments: MutableList<Pair<String, KClass<out Fragment>>> = mutableListOf()

    fun addPage(title: String, pageClass: KClass<out Fragment>) {
        fragments.add(Pair(title, pageClass))
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position].second.java.newInstance()
    }

    override fun getPageTitle(position: Int): CharSequence? = fragments[position].first

    override fun getCount(): Int = fragments.count()
}
