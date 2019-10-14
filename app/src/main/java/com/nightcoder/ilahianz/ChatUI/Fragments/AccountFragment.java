package com.nightcoder.ilahianz.ChatUI.Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.nightcoder.ilahianz.ChatUI.Fragments.AccountFragments.PersonalInfoFragment;
import com.nightcoder.ilahianz.ChatUI.Fragments.AccountFragments.PrivacyFragment;
import com.nightcoder.ilahianz.ChatUI.Fragments.AccountFragments.SettingsFragment;
import com.nightcoder.ilahianz.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {

    private Context mContext;
    private CollapsingToolbarLayout appBarLayout;
    private CircleImageView profileImage;

    public AccountFragment(Context mContext) {
        this.mContext = mContext;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        TabLayout tabLayout = view.findViewById(R.id.tab_account);
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        tabLayout.setupWithViewPager(viewPager);
        appBarLayout = view.findViewById(R.id.collaps);
        profileImage = view.findViewById(R.id.profile_image);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());

        viewPagerAdapter.addFragment(new PersonalInfoFragment(mContext), "Personal Info");
        viewPagerAdapter.addFragment(new PrivacyFragment(), "Privacy");
        viewPagerAdapter.addFragment(new SettingsFragment(), "Settings");

        viewPager.setAdapter(viewPagerAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            appBarLayout.setOnScrollChangeListener(scrollChangeListener);
        }

        return view;
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private CollapsingToolbarLayout.OnScrollChangeListener scrollChangeListener = new View.OnScrollChangeListener() {
        @Override
        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            if (scrollX == 0 && scrollY == 0) {
                Log.d("ScrollX:", String.valueOf(scrollX));
                Log.d("ScrollY:", String.valueOf(scrollY));
            }
        }
    };
    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

    }
}
