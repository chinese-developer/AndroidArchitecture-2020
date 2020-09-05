package com.example.architecture.home.ui.lyrics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.base.imageloader.GlideImageLoader
import com.app.base.app.AppBaseFragment
import com.example.architecture.home.R
import com.example.architecture.home.databinding.FragmentLyricBinding
import com.example.architecture.home.MainFragment
import com.example.architecture.home.repository.pojo.LyricPojo
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * 首页-播放器
 */
@ExperimentalCoroutinesApi class LyricsFragment : AppBaseFragment() {

    private lateinit var binding: FragmentLyricBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLyricBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listeners()
    }

    private fun listeners() {
        binding.apply {

            lrcView.setOnSeekToListener { progress ->
                (parentFragment as MainFragment).onLyricsSeekToListener(progress)
            }

            lrcView.setOnLrcClickListener {

            }
        }
    }

    fun seekTo(progress: Int, fromUser: Boolean) {
        if (::binding.isInitialized) {
            binding.lrcView.seekTo(progress, true, fromUser)
        }
    }

    fun setLrcRows(needReset: Boolean, lyricPojo: LyricPojo) {
        if (::binding.isInitialized) {
            binding.lrcView.setLrcRows(getLrcRows(lyricPojo), needReset)
            setLrcCoverImgUrl(lyricPojo.albumCoverImageUrl)
        }
    }

    private fun setLrcCoverImgUrl(albumCoverImgUrl: String?) {
        if (albumCoverImgUrl.isNullOrBlank()) {
            binding.ivDiscHalo.setImageResource(R.mipmap.ic_play_disc_halo)
        } else {
            GlideImageLoader().display(binding.ivDiscHalo, albumCoverImgUrl)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.lrcView.reset()
    }
}