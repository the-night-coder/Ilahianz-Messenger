package com.nightcoder.ilahianz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.nightcoder.ilahianz.ChatUI.Fragments.AccountFragments.CollegeInfoFragment;
import com.nightcoder.ilahianz.ChatUI.Fragments.AccountFragments.PersonalInfoFragment;
import com.nightcoder.ilahianz.ChatUI.Fragments.AccountFragments.PrivacyFragment;
import com.nightcoder.ilahianz.ChatUI.Fragments.AccountFragments.SettingsFragment;
import com.nightcoder.ilahianz.Supports.Graphics;
import com.nightcoder.ilahianz.Supports.MemorySupports;
import com.nightcoder.ilahianz.Supports.ViewSupports;

import java.util.ArrayList;

import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;
import static com.nightcoder.ilahianz.MessagingActivity.USER_ID_BUFFER;

public class UserProfileActivity extends AppCompatActivity {

    private String userId;
    private TextView name, category;
    private TabLayout tabLayout;
    private LinearLayout appbarContainer;
    private LinearLayout profilePictureBanner;
    private AppBarLayout appBarLayout;
    protected Context mContext;
    private ImageButton closeBtn, closeBtnToolbar;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private LinearLayout container;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    public static final String TAG = "UserProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mContext = UserProfileActivity.this;
        initViews();
        initViewParams();
        init();
    }

    private void initViews() {
        name = findViewById(R.id.profile_name);
        category = findViewById(R.id.profile_category);
        appbarContainer = findViewById(R.id.appbar_container);
        profilePictureBanner = findViewById(R.id.profile_image_container);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        container = findViewById(R.id.container);
        tabLayout = findViewById(R.id.tab_profile);
        viewPager = findViewById(R.id.view_pager);
        toolbar = findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.appbar);
        closeBtn = findViewById(R.id.close_btn);
        closeBtnToolbar = findViewById(R.id.close_btn_toolbar);

        closeBtnToolbar.setOnClickListener(clickListener);
        closeBtn.setOnClickListener(clickListener);

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.close_btn:
                    finish();
                case R.id.close_btn_toolbar:
                    finish();
            }
        }
    };

    private void initViewParams() {
        int[] dimen = Graphics.getResolution(mContext);
        appbarContainer.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dimen[0] - 200
        ));
        toolbar.setVisibility(View.VISIBLE);
        tabLayout.setupWithViewPager(viewPager);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (i == -collapsingToolbarLayout.getHeight()) {
                    Log.d(TAG, "Collapsed");
                    ViewSupports.visibilitySlideAnimation(Gravity.BOTTOM, 600, toolbar, collapsingToolbarLayout, View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        final Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        int colorFrom = getResources().getColor(R.color.black);
                        int colorTo = getResources().getColor(R.color.blue_dark);
                        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                        colorAnimation.setDuration(500);
                        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                window.setStatusBarColor((int) animation.getAnimatedValue());
                            }
                        });
                        colorAnimation.start();

                    }
                } else {
                    toolbar.setVisibility(View.GONE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        final Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(getResources().getColor(R.color.black));
                    }
                }
            }
        });
    }

    private void init() {
        try {
            MemorySupports.setUserInfo(this, USER_ID_BUFFER, getIntent().getStringExtra(KEY_ID));
        } catch (Exception e) {
            Log.d(TAG, "No Extras");
        }

//        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(viewPagerAdapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
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
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        init();
    }
}
