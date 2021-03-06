package com.android.base.app.activity;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

/**
 * <pre>
 *        1.Fragment需要自己处理BackPress事件，如果不处理，就交给子Fragment处理。都不处理则由Activity处理
 *        2.BackPress的传递由低层往深层传递，同一层级的中外层中的 Fragment优先处理。
 *        3.在Fragment中嵌套使用Fragment时，请使用getSupportChildFragmentManager
 * </pre>
 */
public class BackHandlerHelper {

    /**
     * 将back事件分发给 FragmentManager 中管理的子Fragment，如果该 FragmentManager 中的所有Fragment都
     * 没有处理back事件，则尝试 FragmentManager.popBackStack()
     *
     * @return 如果处理了back键则返回 <b>true</b>
     * @see #handleBackPress(Fragment)
     * @see #handleBackPress(FragmentActivity)
     */
    public static boolean handleBackPress(FragmentManager fragmentManager) {
        List<Fragment> fragments = fragmentManager.getFragments();
        for (int i = fragments.size() - 1; i >= 0; i--) {
            Fragment child = fragments.get(i);
            if (isFragmentBackHandled(child)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将back事件分发给Fragment中的子Fragment,
     * 该方法调用了 {@link #handleBackPress(FragmentManager)}
     *
     * @return 如果处理了back键则返回 <b>true</b>
     */
    public static boolean handleBackPress(Fragment fragment) {
        return handleBackPress(fragment.getChildFragmentManager());
    }

    /**
     * 将back事件分发给Activity中的子Fragment,
     * 该方法调用了 {@link #handleBackPress(FragmentManager)}
     *
     * @return 如果处理了back键则返回 <b>true</b>
     */
    public static boolean handleBackPress(FragmentActivity fragmentActivity) {
        return handleBackPress(fragmentActivity.getSupportFragmentManager());
    }

    /**
     * 判断Fragment是否处理了Back键
     *
     * @return 如果处理了back键则返回 <b>true</b>
     */
    @SuppressWarnings("WeakerAccess")
    public static boolean isFragmentBackHandled(Fragment fragment) {
        return fragment != null
                && fragment.isVisible()
                && fragment.getUserVisibleHint() // getUserVisibleHint默认情况下为true，在ViewPager中会被使用到。
                && fragment instanceof OnBackPressListener
                && ((OnBackPressListener) fragment).onBackPressed();
    }

}