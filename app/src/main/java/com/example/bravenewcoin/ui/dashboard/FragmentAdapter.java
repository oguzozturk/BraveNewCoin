package com.example.bravenewcoin.ui.dashboard;

import android.content.Context;

import com.example.bravenewcoin.ui.dashboard.asset.AssetFragment;
import com.example.bravenewcoin.ui.dashboard.market.MarketFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public FragmentAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MarketFragment marketFragment = new MarketFragment();
                return marketFragment;
            case 1:
                AssetFragment assetFragment = new AssetFragment();
                return assetFragment;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}