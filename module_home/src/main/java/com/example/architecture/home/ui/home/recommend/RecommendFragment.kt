package com.example.architecture.home.ui.home.recommend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.android.base.app.fragment.BaseFragment
import com.app.base.utils.scope
import com.drake.brv.utils.grid
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.drake.tooltip.toast
import com.example.architecture.home.R
import com.example.architecture.home.databinding.FragmentRecommendBinding
import com.example.architecture.home.ui.model.home.AlbumModel
import kotlinx.coroutines.delay

class RecommendFragment : BaseFragment() {

    private lateinit var binding: FragmentRecommendBinding
    private val model by viewModels<RecommendViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecommendBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

    }

    private fun initAdapter() {
        binding.apply {
            list.grid(3).setup {
                addType<AlbumModel>(R.layout.item_album_preview)
                onClick(R.id.common_container_id) {
                    toast((getModel() as AlbumModel).name + modelPosition)
                }
            }.models = model.albumDefaultItems

            swipeRefresh.onRefresh {
                scope {
                    delay(1300)
                    list.models = model.albumDefaultItems
                }
            }
        }
    }

}