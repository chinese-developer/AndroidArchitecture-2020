package com.example.architecture.home.ui.home

import android.app.SharedElementCallback
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.app.base.app.AppBaseActivity
import com.bumptech.glide.Glide
import com.example.architecture.home.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.test.*

@AndroidEntryPoint
class TestActivity : AppBaseActivity() {

    private val mViewModel: HomeViewModel by viewModels()

    var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.test)

//        binding = DataBindingUtil.setContentView(this, R.layout.activity_test)
//        binding.lifecycleOwner = this

//        or

//        binding = ActivityTestBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        binding.lifecycleOwner = this

        id = intent.extras?.getInt("key" ) ?: 0

        Glide.with(this)
            .load(R.mipmap.ic_empty_music)
            .into(album_art)

        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: List<String>,
                sharedElements: MutableMap<String, View>
            ) {
                sharedElements.clear()
                sharedElements[id.toString()] = album_art
            }
        })
    }
}