package com.example.user.tvfood.UI;


import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.user.tvfood.Common.Common;
import com.example.user.tvfood.PageTransformer_ViewPager.DepthPageTransformer;
import com.example.user.tvfood.R;
import com.example.user.tvfood.SlidingTabLayout;
import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_TrangChu_Nav extends Fragment {


    private String[] TITLES;

    public Fragment_TrangChu_Nav() {
        // Required empty public constructor
    }

    public static ViewPager mPager;
    private NavigationAdapter mPagerAdapter;
    private SmartTabLayout slidingTabLayout;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment__trang_chu__nav, container, false);

        TITLES = new String[]{getContext().getResources().getString(R.string.danhmuc), getContext().getResources().getString(R.string.trangchu), getContext().getResources().getString(R.string.diadiem)};
        initView(v);
        mPager.setOffscreenPageLimit(2);
        mPager.setPageTransformer(true, new DepthPageTransformer());

        mPager.setAdapter(mPagerAdapter);


        //slidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        //slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.SelectedIndicatorColor));
        //slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(mPager);


        mPager.setCurrentItem(1);

        setOnPageChange_SlidingTabLayout();
        // Inflate the layout for this fragment

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            slidingTabLayout.getChildAt(0);
        }


        return v;
    }

    private void initView(View v) {
        mPager = (ViewPager) v.findViewById(R.id.viewPager);
        slidingTabLayout = (SmartTabLayout) v.findViewById(R.id.sliding_tabs);
        mPagerAdapter = new NavigationAdapter(getChildFragmentManager(), TITLES, TITLES.length);


    }

    private void makeText(String text) {
        Toast.makeText(getContext(), text + "", Toast.LENGTH_SHORT).show();
    }

    private void setOnPageChange_SlidingTabLayout() {
        slidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0 || position == 2) {
                    MainActivity.myAppbar.setExpanded(true, true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public Fragment getCurrentFragment() {
        return mPagerAdapter.getItemAt(mPager.getCurrentItem());
    }

    private static class NavigationAdapter extends CacheFragmentStatePagerAdapter {

        public String[] TITLES;
        private int mScrollY;
        public int n;

        public NavigationAdapter(FragmentManager fm, String[] TITLES, int n) {

            super(fm);
            this.TITLES = TITLES;
            this.n = n;
        }

        public void setScrollY(int scrollY) {
            mScrollY = scrollY;
        }

        @Override
        protected Fragment createItem(int position) {
            // Initialize fragments.
            // Please be sure to pass scroll position to each fragments using setArguments.
            Fragment f;
            final int pattern = position % n;
            switch (pattern) {
                case 0: {
                    f = new Tab_DanhMuc();
                    break;
                }
                case 1: {
                    f = new Tab_TrangChu();
                    break;
                }
                case 2: {
                    f = new Tab_DiaChi();
                    break;
                }
                default: {
                    f = new Tab_TrangChu();
                    break;
                }
            }
            return f;
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }


}
