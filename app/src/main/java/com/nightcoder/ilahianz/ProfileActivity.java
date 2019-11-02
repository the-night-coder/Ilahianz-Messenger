package com.nightcoder.ilahianz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.nightcoder.ilahianz.ChatUI.Fragments.AccountFragments.CollegeInfoFragment;
import com.nightcoder.ilahianz.ChatUI.Fragments.AccountFragments.PersonalInfoFragment;
import com.nightcoder.ilahianz.ChatUI.Fragments.AccountFragments.PrivacyFragment;
import com.nightcoder.ilahianz.ChatUI.Fragments.AccountFragments.SettingsFragment;
import com.nightcoder.ilahianz.Supports.Network;
import com.nightcoder.ilahianz.Supports.ViewSupports;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.nightcoder.ilahianz.Literals.StringConstants.DEFAULT;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_IMAGE_URL;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_USERNAME;
import static com.nightcoder.ilahianz.Literals.StringConstants.USER_INFO_SP;

public class ProfileActivity extends AppCompatActivity {

    private Context mContext;
    private CollapsingToolbarLayout appBarLayout;
    private CircleImageView profileImage;
    private TextView name;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mContext = ProfileActivity.this;
        tabLayout = findViewById(R.id.tab_account);
        viewPager = findViewById(R.id.view_pager);
        tabLayout.setupWithViewPager(viewPager);
        appBarLayout = findViewById(R.id.collaps);
        profileImage = findViewById(R.id.profile_image);
        ImageButton closeBtn = findViewById(R.id.close_btn);
        name = findViewById(R.id.profile_name);
        viewPager.setVisibility(View.GONE);
        new CountDownTimer(200, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                ViewSupports.visibilitySlideAnimation(Gravity.BOTTOM, 600, viewPager, (ViewGroup) viewPager.getRootView(), View.VISIBLE);
            }
        }.start();


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new PersonalInfoFragment(mContext), "Personal Info");
        viewPagerAdapter.addFragment(new CollegeInfoFragment(mContext), "Academic Info");
        viewPagerAdapter.addFragment(new PrivacyFragment(mContext), "Privacy");
        viewPagerAdapter.addFragment(new SettingsFragment(mContext), "Settings");

        viewPager.setAdapter(viewPagerAdapter);


        tabLayout.setOnTabSelectedListener(tabSelectedListener);

        if (!Network.Connected(mContext)) {
            ViewSupports.showNoConnection(mContext);
        }

        name.setText(getUserInfo(KEY_USERNAME));

        if (!getUserInfo(KEY_IMAGE_URL).equals(DEFAULT)) {
            Glide.with(mContext).load(getUserInfo(KEY_IMAGE_URL)).into(profileImage);
        }
    }

    private TabLayout.OnTabSelectedListener tabSelectedListener
            = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            switch (tab.getPosition()) {
                case 0:
                    tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.blue_dark));
                    break;
                case 1:
                    tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.dd_green));
                    break;
                case 2:
                    tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));
                    break;
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    private String getUserInfo(String key) {
        SharedPreferences preferences = mContext.getSharedPreferences(USER_INFO_SP, MODE_PRIVATE);
        return preferences.getString(key, "none");
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

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

    @Override
    protected void onPause() {
        ViewSupports.visibilitySlideAnimation(Gravity.BOTTOM, 400, viewPager, (ViewGroup) viewPager.getRootView(), View.GONE);
        super.onPause();

    }
}
