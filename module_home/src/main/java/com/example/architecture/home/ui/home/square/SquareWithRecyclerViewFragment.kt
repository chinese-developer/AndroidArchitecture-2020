package com.example.architecture.home.ui.home.square

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.android.base.app.fragment.BaseFragment
import com.android.base.utils.adapter.RecyclerViewScrollHelper
import com.android.base.widget.adapter.BindingAdapter
import com.android.base.widget.adapter.animation.SlideInTopItemAnimation
import com.android.base.widget.adapter.utils.setup
import com.app.base.widget.dialog.mdstyle.util.MDUtil.waitForHeight
import com.example.architecture.home.R
import com.example.architecture.home.databinding.FragmentSquareWithRvBinding
import com.example.architecture.home.ui.model.*
import com.example.architecture.home.ui.model.allgames.*
import com.example.architecture.home.ui.model.home.ItemTitle

class SquareWithRecyclerViewFragment : BaseFragment() {

    private lateinit var binding: FragmentSquareWithRvBinding

    private var isDragging = false
    private var lastVisibleItemPosition: Int = 0
    private lateinit var primaryAdapter: BindingAdapter
    private lateinit var secondaryAdapter: BindingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSquareWithRvBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPrimaryAdapter()
        initSecondaryAdapter()
        listeners()

    }

    private fun listeners() {
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
                        RecyclerViewScrollHelper.smoothScrollToPosition(
                            rvSecondary,
                            LinearSmoothScroller.SNAP_TO_START,
                            modelPosition
                        )
                    }
                }

                primaryAdapter.models = fetchPrimaryItems()
            }
        }
    }

    private fun initSecondaryAdapter() {
        binding.apply {
            val secondaryItems = secondaryItems()
            secondaryAdapter = rvSecondary.setup {
                setHasStableIds(true)
                setAnimation(SlideInTopItemAnimation(10f))
                setDuration(1000)
                isFirstOnly(false)

                addType<Model>(R.layout.item_cover_count_1)
                addType<DoubleItemModel>(R.layout.item_cover_count_2)
                addType<ThreeItemModel>(R.layout.item_cover_count_3)
                addType<FourItemModel>(R.layout.item_cover_count_4)
                addType<FiveItemModel>(R.layout.item_cover_count_5)
                addType<SixItemModel>(R.layout.item_cover_count_6)

                onBind {
                    when (itemViewType) {
                        R.layout.item_cover_count_2 -> {

                        }
                        R.layout.item_cover_count_3 -> {

                        }
                    }
                }
            }

            secondaryAdapter.models = secondaryItems

            val secondaryLayoutManager = rvSecondary.layoutManager!! as LinearLayoutManager

            rvSecondary.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    when (newState) {
                        RecyclerView.SCROLL_STATE_DRAGGING -> isDragging = true
                        RecyclerView.SCROLL_STATE_IDLE -> isDragging = false
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (isDragging) {
                        val firstVisibleItemPosition =
                            secondaryLayoutManager.findFirstVisibleItemPosition()
                        if (lastVisibleItemPosition != firstVisibleItemPosition && firstVisibleItemPosition >= 0) {
                            primaryAdapter.notifyItemChangedSelectedPosition(
                                currentSelectedPosition = firstVisibleItemPosition,
                                lastSelectedPosition = lastVisibleItemPosition
                            )
                            lastVisibleItemPosition = firstVisibleItemPosition
                            rvPrimary.smoothScrollToPosition(firstVisibleItemPosition)
                        }
                    }
                }
            })
        }
    }

}