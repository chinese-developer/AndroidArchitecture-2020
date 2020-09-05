package com.android.base.app.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import com.android.base.app.activity.OnBackPressListener

/**
 * `only_on_hidden_changed_callback` is used in res/menu/nav_bottom_tab.xml
 *
 * If you want to define special navigation
 * refer to the following :
 *
 *  <?xml version="1.0" encoding="utf-8"?>
 *  <menu xmlns:tools="http://schemas.android.com/tools"
 *      xmlns:android="http://schemas.android.com/apk/res/android"
 *      tools:ignore="ResourceName">
 *
 *   <item
 *      android:id="@+id/main_home"
 *      android:icon="@drawable/main_sel_tab_home"
 *      android:title="@string/main_tab_home" />
 *
 *   <item
 *      android:id="@+id/main_grow_diary"
 *      android:icon="@drawable/main_sel_tab_diary"
 *      android:title="@string/main_tab_grow_diary" />
 *
 *   <item
 *      android:id="@+id/main_mine"
 *      android:icon="@drawable/main_sel_tab_mine"
 *      android:title="@string/main_tab_mine" />
 *
 *   </menu>
 *
 *   <navigation xmlns:android="http://schemas.android.com/apk/res/android"
 *       xmlns:app="http://schemas.android.com/apk/res-auto"
 *       xmlns:tools="http://schemas.android.com/tools"
 *       android:id="@+id/nav_graph_home"
 *       app:startDestination="@id/main_fragment_dest"
 *       tools:ignore="ResourceName">
 *
 *      <argument
 *          android:name="userMessage"
 *          android:defaultValue="0" />
 *
 *      <only_on_hidden_changed_callback
 *          android:id="@+id/home_fragment_dest"
 *          android:name="me.bo.architecture.home.main.home.presentation.HomeFragment"
 *          android:label="Home page">
 *          <action
 *              android:id="@+id/action_homeFragment_to_SettingFragment"
 *              app:destination="@+id/setting_fragment_dest" />
 *          <argument
 *              android:name="userMessage"
 *              android:defaultValue="0"
 *              app:argType="integer" />
 *      </only_on_hidden_changed_callback>
 *
 *      <only_on_hidden_changed_callback
 *          android:id="@+id/diary_fragment_dest"
 *          android:name="me.bo.architecture.home.main.diary.DiaryFragment"
 *          android:label="Diary page">
 *          <action
 *              android:id="@+id/action_diaryFragment_to_writeDiaryFragment"
 *              app:destination="@+id/write_diary_fragment_dest" />
 *          <argument
 *              android:name="userMessage"
 *              android:defaultValue="0"
 *              app:argType="integer" />
 *       </only_on_hidden_changed_callback>
 *
 *       <only_on_hidden_changed_callback
 *              android:id="@+id/mine_fragment_dest"
 *              android:name="me.bo.architecture.home.main.mine.presentation.MineFragment"
 *              android:label="Mine page">
 *          <action
 *              android:id="@+id/action_mineFragment_to_settingFragment"
 *              app:destination="@+id/setting_fragment_dest" />
 *          <argument
 *              android:name="userMessage"
 *              android:defaultValue="0"
 *              app:argType="integer" />
 *       </only_on_hidden_changed_callback>
 *   </navigation>
 *
 *
 * Activity 使用示例：
 *
 *        val navController = findNavController(R.id.nav_root_fragment)
 *        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_root_fragment)!!
 *        val navigator = KeepStateNavigator(this, navHostFragment.childFragmentManager, R.id.nav_root_fragment)
 *        navController.navigatorProvider += navigator
 *        navController.setGraph(R.navigation.nav_root)
 *
 */
@Navigator.Name("only_on_hidden_changed_callback")
class KeepStateNavigator(
        private val context: Context,
        private val manager: FragmentManager, // Should pass childFragmentManager.
        private val containerId: Int
) : FragmentNavigator(context, manager, containerId) {

    override fun navigate(
            destination: Destination,
            args: Bundle?,
            navOptions: NavOptions?,
            navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        val tag = destination.id.toString()
        val transaction = manager.beginTransaction()

        var initialNavigate = false
        val currentFragment = manager.primaryNavigationFragment
        if (currentFragment != null) {
            transaction.hide(currentFragment)
        } else {
            initialNavigate = true
        }

        var fragment = manager.findFragmentByTag(tag)
        if (fragment == null) {
            val className = destination.className
            fragment = manager.fragmentFactory.instantiate(context.classLoader, className)
            transaction.add(containerId, fragment, tag)
        } else {
            transaction.show(fragment)
        }

        transaction.setPrimaryNavigationFragment(fragment)
        transaction.setReorderingAllowed(true)
        transaction.commitNow()

        return if (initialNavigate) {
            destination
        } else {
            null
        }
    }
}