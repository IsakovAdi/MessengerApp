package com.example.messengerapp.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.messengerapp.R;
import com.example.messengerapp.fragments.ChatsFragment;
import com.example.messengerapp.fragments.PeopleFragment;
import com.example.messengerapp.fragments.ProfileFragment;

public class SectionPagerAdapterMain extends FragmentPagerAdapter {
    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.people, R.string.chats, R.string.profile};
    private Context context;

    public SectionPagerAdapterMain(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new PeopleFragment();
                break;
            case 1:
                fragment = new ChatsFragment();
                break;
            case 2:
                fragment = new ProfileFragment();
                break;
        }
        return fragment;
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position){
        return context.getResources().getString(TAB_TITLES[position]);
    }
    @Override
    public int getCount() {
        return 3;
    }
}
