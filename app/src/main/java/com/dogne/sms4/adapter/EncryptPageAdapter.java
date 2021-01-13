package com.dogne.sms4.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.dogne.sms4.activity.EncryptActivity;
import com.dogne.sms4.fragment.EncryptContactFragment;

public class EncryptPageAdapter extends FragmentStatePagerAdapter
{
    private int tabCount;
    private EncryptActivity encryptActivity;

    @SuppressWarnings("deprecation")
    public EncryptPageAdapter(@NonNull FragmentManager fm, int tabCount, EncryptActivity encryptActivity)
    {
        super(fm);
        this.tabCount = tabCount;
        this.encryptActivity = encryptActivity;
    }


    @NonNull
    @Override
    @SuppressWarnings("ConstantConditions")
    public Fragment getItem(int position)
    {
        //Returning the current tabs
        if (position == 0)
            return new EncryptContactFragment(encryptActivity);
        return null;
    }

    @Override
    public int getCount()
    {
        return tabCount;
    }
}
