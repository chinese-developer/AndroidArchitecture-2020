/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

@file:Suppress("SpellCheckingInspection")

package com.example.architecture.home.ui.allgames

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.android.base.app.fragment.BaseFragment
import com.android.base.utils.android.views.newFragment
import com.android.base.widget.adapter.BindingAdapter
import com.app.base.utils.ext.setOverScrollModeNever
import com.example.architecture.home.R
import com.example.architecture.home.databinding.FragmentAllGamesBinding
import com.example.architecture.home.ui.model.allgames.GamesSecondaryFragment
import com.example.architecture.home.ui.model.allgames.GamesSecondaryFragment.Companion.KEY_FOR_WHICH_PAGE

class AllGamesFragment : BaseFragment() {

    internal lateinit var adapter: BindingAdapter

    internal var lastVisibleItemPosition: Int = 0
    internal var isDragging = false

    private lateinit var binding: FragmentAllGamesBinding

    private var prevSelectedBgAnimatorView: View? = null
    private var prevSelectedIconAnimatorView: View? = null
    private var prevNormalBgAnimatorView: View? = null
    private var prevNormalIconAnimatorView: View? = null
    private var prevTextView: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllGamesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        BannerFactory.build(requireContext(), binding.banner)
        initViewPager()
        listeners()
    }

    private fun listeners() {
        binding.apply {
            startAnimation(
                homeTabSportsSelectedBg,
                homeSportsSelectedIcon,
                homeTabSportsNormalBg,
                homeSportsNormalIcon,
                tvHomeSportsText
            )
            homeTabSportsNormalBg.setOnClickListener {
                startAnimation(
                    homeTabSportsSelectedBg,
                    homeSportsSelectedIcon,
                    homeTabSportsNormalBg,
                    homeSportsNormalIcon,
                    tvHomeSportsText
                )
            }
            homeTabLiveNormalBg.setOnClickListener {
                startAnimation(
                    homeTabLiveSelectedBg,
                    homeLiveSelectedIcon,
                    homeTabLiveNormalBg,
                    homeLiveNormalIcon,
                    tvHomeLiveText
                )
            }
            homeTabChessNormalBg.setOnClickListener {
                startAnimation(
                    homeTabChessSelectedBg,
                    homeChessSelectedIcon,
                    homeTabChessNormalBg,
                    homeChessNormalIcon,
                    tvHomeChessText
                )
            }
            homeTabESportsNormalBg.setOnClickListener {
                startAnimation(
                    homeTabESportsSelectedBg,
                    homeESportsSelectedIcon,
                    homeTabESportsNormalBg,
                    homeESportsNormalIcon,
                    tvHomeESportsText
                )
            }
            homeTabLotteryNormalBg.setOnClickListener {
                startAnimation(
                    homeTabLotterySelectedBg,
                    homeLotterySelectedIcon,
                    homeTabLotteryNormalBg,
                    homeLotteryNormalIcon,
                    tvHomeLotteryText
                )
            }
            homeTabFishesNormalBg.setOnClickListener {
                startAnimation(
                    homeTabFishesSelectedBg,
                    homeFishesSelectedIcon,
                    homeTabFishesNormalBg,
                    homeFishesNormalIcon,
                    tvHomeFishesText
                )
            }
        }
    }

    private fun initViewPager() {
        binding.apply {
            val itemCount = fetchGamesPrimaryItems().size
            homeGameVp.offscreenPageLimit = itemCount
            homeGameVp.setOverScrollModeNever()
            homeGameVp.adapter = object : FragmentStateAdapter(childFragmentManager, lifecycle) {
                override fun getItemCount(): Int = itemCount

                override fun createFragment(position: Int): Fragment {
                    return newFragment<GamesSecondaryFragment>(Pair(KEY_FOR_WHICH_PAGE, position.toString()))
                }
            }

            homeGameVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    if (isDragging) {
                        /*adapter.notifyItemChangedSelectedPosition(
                            currentSelectedPosition = position,
                            lastSelectedPosition = lastVisibleItemPosition
                        )*/
                        lastVisibleItemPosition = position
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {
                    if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                        isDragging = true
                    } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                        isDragging = false
                    }
                }
            })
        }
    }

    private fun startAnimation(
        selectedBg: View,
        selectedIcon: View,
        normalBg: View,
        normalIcon: View,
        textView: TextView
    ) {
        if (selectedBg != prevSelectedBgAnimatorView) {
            startGoneAnimation(
                prevSelectedBgAnimatorView,
                prevSelectedIconAnimatorView,
                prevNormalBgAnimatorView,
                prevNormalIconAnimatorView,
                prevTextView
            )
            prevSelectedBgAnimatorView = selectedBg
            prevSelectedIconAnimatorView = selectedIcon
            prevNormalBgAnimatorView = normalBg
            prevNormalIconAnimatorView = normalIcon
            prevTextView = textView
            startShowAnimation(selectedBg, selectedIcon, normalBg, normalIcon, textView)
        }
    }

    private fun startGoneAnimation(
        selectedBg: View?,
        selectedIcon: View?,
        normalBg: View?,
        normalIcon: View?,
        textView: TextView?
    ) {
        val animatorSet = AnimatorSet()
        val normalAnimator =
            AnimatorInflater.loadAnimator(activity, R.animator.set_home_tab_icon_golden_2_gray)
        val alphaFrom0To1Animator =
            AnimatorInflater.loadAnimator(activity, R.animator.anim_alpha_0_1)
        val alphaFrom1To0Animator =
            AnimatorInflater.loadAnimator(activity, R.animator.anim_alpha_1_0)
        val alphaFrom0To0Animator =
            AnimatorInflater.loadAnimator(activity, R.animator.anim_alpha_0_0)
        normalAnimator.setTarget(normalIcon)
        alphaFrom0To1Animator.setTarget(normalBg)
        alphaFrom1To0Animator.setTarget(selectedBg)
        alphaFrom0To0Animator.setTarget(selectedIcon)
        animatorSet.playTogether(
            normalAnimator,
            alphaFrom0To1Animator,
            alphaFrom1To0Animator,
            alphaFrom0To0Animator
        )
        textView?.setTextColor(ContextCompat.getColor(textView.context, R.color.core_9aa4c2))
        animatorSet.start()
    }

    private fun startShowAnimation(
        selectedBg: View,
        selectedIcon: View,
        normalBg: View,
        normalIcon: View,
        textView: TextView
    ) {
        val animatorSet = AnimatorSet()
        val selectedAnimator =
            AnimatorInflater.loadAnimator(activity, R.animator.set_home_tab_icon_gray_2_golden)
        val alphaFrom0To1Animator =
            AnimatorInflater.loadAnimator(activity, R.animator.anim_alpha_0_1)
        val alphaFrom1To0Animator =
            AnimatorInflater.loadAnimator(activity, R.animator.anim_alpha_1_0)
        val alphaFrom0To0Animator =
            AnimatorInflater.loadAnimator(activity, R.animator.anim_alpha_0_0)
        selectedAnimator.setTarget(selectedIcon)
        alphaFrom0To1Animator.setTarget(selectedBg)
        alphaFrom1To0Animator.setTarget(normalBg)
        alphaFrom0To0Animator.setTarget(normalIcon)
        animatorSet.playTogether(
            selectedAnimator,
            alphaFrom0To1Animator,
            alphaFrom1To0Animator,
            alphaFrom0To0Animator
        )
        textView.setTextColor(ContextCompat.getColor(textView.context, R.color.white))
        animatorSet.start()
    }

}