package com.example.architecture.home.ui.home.square

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.observe
import com.android.base.app.fragment.BaseFragment
import com.android.base.widget.adapter.BindingAdapter
import com.android.base.widget.adapter.animation.SlideScaleYItemAnimation
import com.android.base.widget.adapter.utils.linear
import com.android.base.widget.adapter.utils.setup
import com.app.base.AppContext
import com.app.base.utils.animations.*
import com.example.architecture.home.R
import com.example.architecture.home.databinding.FragmentScreenSlidePageBinding
import com.example.architecture.home.ui.model.*
import kotlin.properties.Delegates


class ScreenSlidePageFragment : BaseFragment() {

    private lateinit var binding: FragmentScreenSlidePageBinding
    private lateinit var adapter: BindingAdapter

    private var whoAmI by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScreenSlidePageBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        whoAmI = arguments?.getString(KEY_FOR_WHICH_PAGE)?.toInt() ?: 0

        initAdapter()

        AppContext.get().appDataSource.eventBus().mayStartAnim.observe(viewLifecycleOwner) {
            if (whoAmI == it && lifecycle.currentState == Lifecycle.State.STARTED) {
                adapter.notifyDataSetChanged()
            }
        }
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
                        R.layout.item_cover_count_2 -> {

                        }
                        R.layout.item_cover_count_3 -> {

                        }
                    }
                }

                addClickable(
                    R.id.cl_top_left_container,
                    R.id.cl_top_right_container,
                    R.id.cl_mid_left_container,
                    R.id.cl_mid_right_container,
                    R.id.cl_bottom_left_container,
                    R.id.cl_bottom_right_container
                )
                onClick {
                    when (it) {
                        R.id.cl_top_left_container -> findView<ConstraintLayout>(R.id.cl_top_left_container).buttonPressedRubberBand()
                            .start()
                        R.id.cl_top_right_container -> findView<ConstraintLayout>(R.id.cl_top_right_container).buttonPressedScaleXY()
                            .start()
                        R.id.cl_mid_left_container -> findView<ConstraintLayout>(R.id.cl_mid_left_container).fadeExit()
                            .start()
                        R.id.cl_mid_right_container -> findView<ConstraintLayout>(R.id.cl_mid_right_container).slideRightEnter()
                            .start()
                        R.id.cl_bottom_left_container -> findView<ConstraintLayout>(R.id.cl_bottom_left_container).zoomOutLeftExit()
                            .start()
                        R.id.cl_bottom_right_container -> findView<ConstraintLayout>(R.id.cl_bottom_right_container).zoomOutExit()
                            .start()
                    }
                }
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

    companion object {
        const val KEY_FOR_WHICH_PAGE = "key_for_data"
    }

}