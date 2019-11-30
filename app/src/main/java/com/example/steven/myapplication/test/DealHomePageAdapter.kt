package com.example.steven.myapplication.test

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter


/**
 * by wyn at 19/5/20
 */

class DealHomePageAdapter(var fragments: MutableList<out Fragment>, var titles: Array<String>, fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    override fun getItem(p0: Int): Fragment = fragments[p0]

    override fun getCount(): Int = fragments.size

    override fun getPageTitle(position: Int): CharSequence? = titles[position]
}