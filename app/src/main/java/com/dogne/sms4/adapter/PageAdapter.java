package com.dogne.sms4.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.dogne.sms4.fragment.ContactFragment;
import com.dogne.sms4.fragment.InboxFragment;
import com.dogne.sms4.fragment.OutboxFragment;

public class PageAdapter extends FragmentStatePagerAdapter
{
    private int tabCount;

    @SuppressWarnings("deprecation")
    public PageAdapter(@NonNull FragmentManager fm, int tabCount)
    {
        super(fm);
        this.tabCount = tabCount;
    }

    @NonNull
    @Override
    @SuppressWarnings("ConstantConditions")
    public Fragment getItem(int position)
    {
        //Returning the current tabs
        switch (position)
        {
            case 0: //
                return new InboxFragment();
            case 1:
                return new OutboxFragment();
            case 2:
                return new ContactFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount()
    {
        return tabCount;
    }
}
