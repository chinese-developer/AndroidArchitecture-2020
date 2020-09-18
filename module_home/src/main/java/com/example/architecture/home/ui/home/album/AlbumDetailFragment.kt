package com.example.architecture.home.ui.home.album

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.viewModels
import androidx.palette.graphics.Palette
import androidx.transition.TransitionInflater
import com.android.base.app.fragment.BaseFragment
import com.android.base.utils.android.compat.setStatusBarColor
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.example.architecture.home.MainActivity.Companion.allowedToExitApp
import com.example.architecture.home.R
import com.example.architecture.home.databinding.FragmentAlbumDetailBinding
import com.example.architecture.home.repository.dataloaders.AlbumLoader
import com.example.architecture.home.utils.GlideOptions.albumOptions
import com.example.architecture.home.utils.MediaTimberUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AlbumDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentAlbumDetailBinding

    private val model by viewModels<ArtistDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.enter_transform)
//        sharedElementEnterTransition = ChangeBounds().apply {
//            duration = 750
//        }
//        sharedElementReturnTransition= ChangeBounds().apply {
//            duration = 750
//        }

        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: List<String>,
                sharedElements: MutableMap<String, View>
            ) {
                sharedElements.clear()
                val key = arguments?.getString(TRANSITION_NAME) ?: ""
                sharedElements[key] = binding.albumArt
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setAlbumDetails()
        listeners()
    }

    private fun listeners() {
        binding.apply {

        }
    }

    private fun setupToolbar() {
        binding.apply {
            albumArt.transitionName = arguments?.getString(TRANSITION_NAME)

            val activity = activity as AppCompatActivity
            activity.setSupportActionBar(toolbar)
            activity.supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)
                setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setAlbumDetails() {

        val albumID = arguments?.getString(ALBUM_ID)?.toLong() ?: 0
        val album = AlbumLoader.getAlbum(activity, albumID)

        val songCount: String = MediaTimberUtils.makeLabel(
            activity,
            R.plurals.Nsongs,
            album.songCount
        )

        val year = if (album.year != 0) " - ${album.year}" else ""

        binding.apply {
            collapsingToolbar.title = album.title
            albumTitle.text = album.title
            albumDetails.text = "${album.artistName} - $songCount$year"
        }

        Glide.with(this)
            .asBitmap()
            .load(MediaTimberUtils.getAlbumArtUri(albumID).toString())
            .apply(albumOptions)
            .into(object : BitmapImageViewTarget(binding.albumArt) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.Builder(resource).generate {
                        it?.apply {
                            if (vibrantSwatch != null) {
                                setColor(vibrantSwatch!!.rgb)
                            } else {
                                setColor(mutedSwatch?.rgb)
                            }
                        }
                    }

                    super.onResourceReady(resource, transition)
                }

                private fun setColor(@ColorInt primaryColor: Int?) {
                    binding.collapsingToolbar.setContentScrimColor(primaryColor ?: 0)
                    activity?.setStatusBarColor(primaryColor ?: 0)
                    parentFragment?.startPostponedEnterTransition()
                }
            })
    }

    override fun handleBackPress(): Boolean {
        parentFragmentManager.popBackStack()
        allowedToExitApp = true
        return true
    }


    companion object {
        const val ALBUM_ID = "album_id"
        const val TRANSITION_NAME = "transition_name"
    }
}