package com.example.architecture.home.ui.home.square

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.base.app.fragment.BaseFragment
import com.android.base.widget.adapter.BindingAdapter
import com.android.base.widget.adapter.animation.SlideInTopItemAnimation
import com.android.base.widget.adapter.utils.linear
import com.android.base.widget.adapter.utils.setup
import com.example.architecture.home.R
import com.example.architecture.home.databinding.FragmentScreenSlidePageBinding
import com.example.architecture.home.ui.model.*


class ScreenSlidePageFragment : BaseFragment() {

    private lateinit var binding: FragmentScreenSlidePageBinding

    private lateinit var adapter: BindingAdapter

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

        initAdapter()
    }

    private fun initAdapter() {
        binding.apply {
            val position = arguments?.getString(KEY_FOR_DATA)?.toInt() ?: 0
            adapter = list.linear(scrollEnabled = false).setup {
                setHasStableIds(true)
                setAnimation(SlideInTopItemAnimation(10f))
                setDuration(1000)
                isFirstOnly(false)

                when (position) {
                    0 -> addType<Model>(R.layout.item_cover_count_1)
                    1 -> addType<DoubleItemModel>(R.layout.item_cover_count_2)
                    2 -> addType<ThreeItemModel>(R.layout.item_cover_count_3)
                    3 -> addType<FourItemModel>(R.layout.item_cover_count_4)
                    4 -> addType<FiveItemModel>(R.layout.item_cover_count_5)
                    5 -> addType<SixItemModel>(R.layout.item_cover_count_6)
                }

                onBind {
                    when (itemViewType) {
                        R.layout.item_cover_count_2 -> {

                        }
                        R.layout.item_cover_count_3 -> {

                        }
                    }
                }
            }

            val model = when (position) {
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
        const val KEY_FOR_DATA = "key_for_data"
    }
}