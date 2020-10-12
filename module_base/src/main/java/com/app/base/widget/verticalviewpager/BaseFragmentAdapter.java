package com.app.base.widget.verticalviewpager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class BaseFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private List<String> titles;

    public BaseFragmentAdapter(@Nonnull FragmentManager fm, @Nonnull List<Fragment> fragments, @Nullable List<String> titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null && titles.size() > position) {
            return titles.get(position).toString();
        }
        return "";
    }

    public static class Holder {
        private final List<Fragment> fragments = new ArrayList<>();
        private FragmentManager manager;
        private List<String> titles;

        public Holder(FragmentManager manager) {
            this.manager = manager;
        }

        public Holder(FragmentManager manager, List<String> titles) {
            this.manager = manager;
            this.titles = titles;
        }

        public Holder add(Fragment f) {
            fragments.add(f);
            return this;
        }

        public BaseFragmentAdapter set() {
            return new BaseFragmentAdapter(manager, fragments, titles);
        }
    }

}
