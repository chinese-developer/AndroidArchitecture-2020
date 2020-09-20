package com.example.architecture.home.ui.home.square

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_DRAGGING
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE
import com.android.base.app.fragment.BaseFragment
import com.android.base.utils.android.views.newFragment
import com.android.base.widget.adapter.BindingAdapter
import com.android.base.widget.adapter.utils.setup
import com.app.base.AppContext
import com.app.base.widget.dialog.mdstyle.util.MDUtil.waitForHeight
import com.example.architecture.home.R
import com.example.architecture.home.databinding.FragmentSquareWithVpBinding
import com.example.architecture.home.ui.home.square.ScreenSlidePageFragment.Companion.KEY_FOR_WHICH_PAGE
import com.example.architecture.home.ui.model.home.ItemTitle

class SquareWithViewPagerFragment : BaseFragment() {

    private lateinit var binding: FragmentSquareWithVpBinding
    private lateinit var primaryAdapter: BindingAdapter

    private var lastVisibleItemPosition: Int = 0
    private var isDragging = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSquareWithVpBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPrimaryAdapter()
        initSecondaryAdapter()
    }

    private fun initPrimaryAdapter() {
        binding.apply {
            footer.waitForHeight {
                rvPrimary.itemAnimator = null
                primaryAdapter = rvPrimary.setup {
                    setHasStableIds(true)
                    setSelectItemPosition(0)
                    addType<ItemTitle>(R.layout.item_group)
                    onBind {
                        val model = getModel() as ItemTitle
                        model.checked = isSelected

                        findView<CardView>(R.id.container).adjustAndAverageHeight(
                            position = modelPosition,
                            itemCount = itemCount,
                            newHeight = measuredHeight
                        )
                    }

                    addFastClickable(R.id.container)
                    onClick {
                        notifyItemChangedSelectedPosition(
                            currentSelectedPosition = modelPosition,
                            lastSelectedPosition = lastVisibleItemPosition
                        )
                        lastVisibleItemPosition = modelPosition
                        vpSecondary.currentItem = modelPosition
                        AppContext.get().appDataSource.eventBus().startAnim(modelPosition)
                    }
                }

                primaryAdapter.models = fetchPrimaryItems()
            }
        }
    }

    private fun initSecondaryAdapter() {
        binding.apply {
            val itemCount = fetchPrimaryItems().size
            vpSecondary.offscreenPageLimit = itemCount
            vpSecondary.setOverScrollModeNever()
            vpSecondary.adapter = object : FragmentStateAdapter(childFragmentManager, lifecycle) {
                override fun getItemCount(): Int = itemCount

                override fun createFragment(position: Int): Fragment {
                    return newFragment<ScreenSlidePageFragment>(Pair(KEY_FOR_WHICH_PAGE, position.toString()))
                }
            }

            vpSecondary.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    if (isDragging) {
                        primaryAdapter.notifyItemChangedSelectedPosition(
                            currentSelectedPosition = position,
                            lastSelectedPosition = lastVisibleItemPosition
                        )
                        lastVisibleItemPosition = position
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {
                    if (state == SCROLL_STATE_DRAGGING) {
                        isDragging = true
                    } else if (state == SCROLL_STATE_IDLE) {
                        isDragging = false
                    }
                }
            })
        }
    }

}