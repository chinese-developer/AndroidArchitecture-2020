/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.core.viewpagers

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.app.base.R

internal class DialogPagerAdapter : PagerAdapter() {

    override fun instantiateItem(
      container: ViewGroup,
      position: Int
    ): Any {
        var resId = 0
        when (position) {
          0 -> resId = R.id.itemPreChooserGrid
          1 -> resId = R.id.customView
        }
        return container.findViewById(resId)
    }

    override fun getCount(): Int = 2

    override fun isViewFromObject(
      view: View,
      `object`: Any
    ): Boolean = view == `object` as View

    override fun destroyItem(
      container: ViewGroup,
      position: Int,
      `object`: Any
    ) = Unit
}