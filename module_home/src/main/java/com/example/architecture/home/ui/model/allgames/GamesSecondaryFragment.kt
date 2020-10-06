/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.example.architecture.home.ui.model.allgames

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import com.android.base.app.fragment.BaseFragment
import com.android.base.widget.adapter.BindingAdapter
import com.android.base.widget.adapter.animation.SlideScaleYItemAnimation
import com.android.base.widget.adapter.utils.linear
import com.android.base.widget.adapter.utils.setup
import com.app.base.AppContext
import com.app.base.utils.animations.*
import com.example.architecture.home.R
import com.example.architecture.home.databinding.FragmentGamesSecondaryBinding
import kotlin.properties.Delegates

class GamesSecondaryFragment : BaseFragment() {

    private lateinit var binding: FragmentGamesSecondaryBinding
    private lateinit var adapter: BindingAdapter

    private var whoAmI by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGamesSecondaryBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        whoAmI = arguments?.getString(KEY_FOR_WHICH_PAGE)?.toInt() ?: 0

        initAdapter()

        AppContext.get().appDataSource.eventBus().mayStartAnim.observe(viewLifecycleOwner, {
            if (whoAmI == it && lifecycle.currentState == Lifecycle.State.STARTED) {
                adapter.notifyDataSetChanged()
            }
        })
    }

    private fun initAdapter() {
        binding.apply {
            adapter = list.linear(scrollEnabled = false).setup {
                setDuration(370)
                isFirstOnly(false)
                when (whoAmI) {
                    0 -> addType<Model>(R.layout.item_cover_count_1)
                    1 -> addType<DoubleItemModel>(R.layout.item_cover_count_2)
                    2 -> addType<ThreeItemModel>(R.layout.item_cover_count_3)
                    3 -> addType<FourItemModel>(R.layout.item_cover_count_4)
                    4 -> addType<FiveItemModel>(R.layout.item_cover_count_5)
                    5 -> addType<SixItemModel>(R.layout.item_cover_count_6)
                }

                onBind {
                    setAnimation(SlideScaleYItemAnimation())

                    when (itemViewType) {
                        R.layout.item_cover_count_1 -> {

                        }
                        R.layout.item_cover_count_2 -> {

                        }
                        R.layout.item_cover_count_3 -> {

                        }
                    }
                }

                addClickable(*clickableIds)
                addLongClickable(*clickableIds)
                onLongClick { startAnimation(it) }
                onClick { startAnimation(it) }
            }

            val model = when (whoAmI) {
                0 -> Model("")
                1 -> DoubleItemModel("")
                2 -> ThreeItemModel("")
                3 -> FourItemModel("")
                4 -> FiveItemModel("")
                5 -> SixItemModel("")
                else -> Model("")
            }

            adapter.models = listOf(model)
        }
    }

    private fun BindingAdapter.BindingViewHolder.startAnimation(id: Int) {
        when (id) {
//            R.id.animTopLayout -> findView<FrameLayout>(R.id.animTopLayout).buttonPressedScaleXY().start()
//            R.id.animBottomLayout -> findView<FrameLayout>(R.id.animBottomLayout).buttonPressedScaleXY().start()
//            R.id.animMiddleLayout -> findView<FrameLayout>(R.id.animMiddleLayout).buttonPressedScaleXY().start()
            R.id.cl_top_left_container -> findView<ConstraintLayout>(R.id.cl_top_left_container).buttonPressedRubberBand()
                .start()
            R.id.cl_top_right_container -> findView<ConstraintLayout>(R.id.cl_top_right_container).buttonPressedScaleXY()
                .start()
            R.id.cl_mid_left_container -> findView<ConstraintLayout>(R.id.cl_mid_left_container).pagerEnter()
                .start()
            R.id.cl_mid_right_container -> findView<ConstraintLayout>(R.id.cl_mid_right_container).jelly()
                .start()
            R.id.cl_bottom_left_container -> findView<ConstraintLayout>(R.id.cl_bottom_left_container).bounceEnter()
                .start()
            R.id.cl_bottom_right_container -> findView<ConstraintLayout>(R.id.cl_bottom_right_container).swing()
                .start()
        }
    }

    private val clickableIds = intArrayOf(
//        R.id.animTopLayout,
//        R.id.animMiddleLayout,
//        R.id.animBottomLayout,
        R.id.cl_top_left_container,
        R.id.cl_top_right_container,
        R.id.cl_mid_left_container,
        R.id.cl_mid_right_container,
        R.id.cl_bottom_left_container,
        R.id.cl_bottom_right_container
    )

    companion object {
        const val KEY_FOR_WHICH_PAGE = "key_for_data"
    }

}