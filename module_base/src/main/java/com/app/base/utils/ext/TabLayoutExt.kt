package com.app.base.utils.ext

import com.google.android.material.tabs.TabLayout
import android.widget.LinearLayout
import java.lang.reflect.Field

/* https://stackoverflow.com/questions/45715737/android-tablayout-custom-indicator-width */
fun TabLayout.setIndicator(setMarginAction: (index: Int, LinearLayout.LayoutParams) -> Unit) {
    val tabLayout = javaClass
    var tabStrip: Field? = null

    try {
        tabStrip = tabLayout.getDeclaredField("mTabStrip")
    } catch (e: NoSuchFieldException) {
        e.printStackTrace()
    }

    if (tabStrip == null) {
        return
    }

    tabStrip.isAccessible = true
    var llTab: LinearLayout? = null

    try {
        llTab = tabStrip.get(this) as LinearLayout?
    } catch (e: IllegalAccessException) {
        e.printStackTrace()
    }

    if (llTab == null) {
        return
    }

    for (i in 0 until llTab.childCount) {
        val child = llTab.getChildAt(i)
        child.setPadding(0, 0, 0, 0)
        val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        setMarginAction(i, params)
        child.layoutParams = params
        child.invalidate()
    }

}