package com.example.instagramclone;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {
    //Constructor calling the constructor of super class that is FragmentPagerAdapter
    public TabAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int fragmentPosition) {
        switch(fragmentPosition){
            case 0:
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;
            case 1:
                UserFragment userFragment = new UserFragment();
                return userFragment;
            case 2:
                PictureFragment pictureFragment = new PictureFragment();
                return pictureFragment;
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0:
                return "Profile";
            case 1:
                return "Users";
            case 2:
                return "Picture";
            default:
                return null;
        }
    }
}
