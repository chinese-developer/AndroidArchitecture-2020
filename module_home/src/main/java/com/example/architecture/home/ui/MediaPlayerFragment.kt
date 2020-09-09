package com.example.architecture.home.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.architecture.home.databinding.FragmentMediaPlayerBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_media_player.*

class MediaPlayerFragment : BottomSheetDialogFragment(), LifecycleObserver {

    private lateinit var binding: FragmentMediaPlayerBinding
    private lateinit var dialog: BottomSheetDialog
    private lateinit var behavior: BottomSheetBehavior<View>

    val title = ObservableField<String>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        binding =
            FragmentMediaPlayerBinding.inflate(LayoutInflater.from(context), dialog.root, false)
        dialog.setContentView(binding.root)
        binding.host = this
        behavior = BottomSheetBehavior.from(binding.root.parent as View)

        behavior.skipCollapsed = true
        behavior.isHideable = true
        behavior.state = BottomSheetBehavior.STATE_EXPANDED

        lifecycle.addObserver(this)

        return dialog
    }

    override fun onResume() {
        super.onResume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun dismiss() {
        if (::dialog.isInitialized && dialog.isShowing) {
            dialog.dismiss()
        }
    }

}