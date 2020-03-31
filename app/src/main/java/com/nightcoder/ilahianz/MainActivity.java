package com.nightcoder.ilahianz;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nightcoder.ilahianz.ChatUI.Fragments.ChatFragment;
import com.nightcoder.ilahianz.MainActivityFragments.EventsFragment;
import com.nightcoder.ilahianz.MainActivityFragments.NotificationFragment;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    protected MyApp myApp;
    private BottomNavigationView navigationView;
    private Context mContext;
    private TextView heading;
    private CircleImageView profileImage;
    private ImageButton searchButton;
    private ViewPager viewPager;

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.profile_photo:
                    startActivity(new Intent(mContext, ProfileActivity.class));
                    break;
                case R.id.scan_button:
                    startActivity(new Intent(mContext, ScanProfileActivity.class));
                    break;
                case R.id.search_button:
                    openSearch();
            }
        }
    };

    private void openSearch() {
        startActivity(new Intent(mContext, SearchActivity.class));
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    heading.setText(R.string.chats);
                    navigationView.setSelectedItemId(R.id.nav_chats);
                    break;
                case 1:
                    heading.setText(R.string.search);
                    navigationView.setSelectedItemId(R.id.nav_search);
                    break;
                case 2:
                    heading.setText(R.string.notifications);
                    navigationView.setSelectedItemId(R.id.nav_favorite);
                    break;
            }
            int colorFrom = getResources().getColor(R.color.white);
            int colorTo = getResources().getColor(R.color.black);
            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
            colorAnimation.setDuration(500);
            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    heading.setTextColor((int) animation.getAnimatedValue());
                }
            });
            colorAnimation.start();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private BottomNavigationView.OnNavigationItemSelectedListener navListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.nav_chats:
                    heading.setText(R.string.chats);
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.nav_search:
                    heading.setText(R.string.search);
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.nav_favorite:
                    heading.setText(R.string.notifications);
                    viewPager.setCurrentItem(2);
                    break;
            }

            int colorFrom = getResources().getColor(R.color.white);
            int colorTo = getResources().getColor(R.color.black);
            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
            colorAnimation.setDuration(500);
            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    heading.setTextColor((int) animation.getAnimatedValue());
                }
            });
            colorAnimation.start();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        initViews();
        initInterface();
        init();
        setViewPager();
    }

    private void init() {
        myApp = (MyApp) this.getApplicationContext();
        navigationView.setOnNavigationItemSelectedListener(navListener);
        EmojiManager.destroy();
        EmojiManager.install(new GoogleEmojiProvider());
    }

    private void setViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new ChatFragment(), "Chat Fragment");
        viewPagerAdapter.addFragment(new EventsFragment(mContext), "Search Fragment");
        viewPagerAdapter.addFragment(new NotificationFragment(mContext), "Notice Fragment");
        viewPager.setAdapter(viewPagerAdapter);
    }

    private void initInterface() {
        searchButton.setOnClickListener(clickListener);
        profileImage.setOnClickListener(clickListener);
        viewPager.setOnPageChangeListener(pageChangeListener);
    }

    private void initViews() {
        navigationView = findViewById(R.id.bottom_nav);
        mContext = MainActivity.this;
        heading = findViewById(R.id.heading);
        profileImage = findViewById(R.id.profile_photo);
        searchButton = findViewById(R.id.search_button);
        viewPager = findViewById(R.id.view_pager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    @Override
    protected void onResume() {
        myApp.setCurrentActivity(this);
        myApp.setOnline();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        clearReference();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        clearReference();
        super.onPause();
    }

    private void clearReference() {
        Activity activity = myApp.getCurrentActivity();
        if (this.equals(activity)) {
            myApp.setCurrentActivity(this);
        }
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

        Fragment getFragment(int pos) {
            return fragments.get(pos);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

    }
}


