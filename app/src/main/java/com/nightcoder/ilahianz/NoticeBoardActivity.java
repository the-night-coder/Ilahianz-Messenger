package com.nightcoder.ilahianz;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.nightcoder.ilahianz.Adapters.SlideAdapter;
import com.nightcoder.ilahianz.NoticeBoardFragments.AllFragment;
import com.nightcoder.ilahianz.NoticeBoardFragments.MyNoticeFragment;
import com.nightcoder.ilahianz.Supports.ViewSupports;

import java.util.ArrayList;

public class NoticeBoardActivity extends AppCompatActivity {

    protected MyApp myApp;
    private ViewPager viewPager;
    private ImageButton composeButton;
    private ImageButton starredButton;
    private TextView title;
    private int currentPage;
    private LinearLayout dotLayout;
    private TextView[] dots;
    private ImageButton back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board);
        init();
        //startupDialog();
        setTab();
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        composeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NoticeBoardActivity.this, ComposeNoticeActivity.class));
            }
        });

        starredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NoticeBoardActivity.this, NoticeStarredActivity.class));
            }
        });

    }

    private void setTab() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new AllFragment(NoticeBoardActivity.this), "All");
        viewPagerAdapter.addFragment(new MyNoticeFragment(NoticeBoardActivity.this), "My Notice");
        //viewPagerAdapter.addFragment(new AllFragment(this), "College");

        viewPager.setAdapter(viewPagerAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    changeTitle("Notice Board");
                } else {
                    changeTitle("Your Notice");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void changeTitle(String title) {
        this.title.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));
        this.title.setVisibility(View.GONE);
        this.title.setText(title);
        this.title.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        this.title.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myApp.setCurrentActivity(this);
        myApp.setOnline();
    }

    @Override
    protected void onPause() {
        super.onPause();
        clearReference();
    }

    private void clearReference() {
        Activity activity = myApp.getCurrentActivity();
        if (this.equals(activity)) {
            myApp.setCurrentActivity(this);
        }
    }
    private void init() {
        viewPager = findViewById(R.id.view_pager);
        composeButton = findViewById(R.id.compose_button);
        starredButton = findViewById(R.id.refresh_button);
        CoordinatorLayout container = findViewById(R.id.id_container);
        title = findViewById(R.id.title);
        container.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        container.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        myApp = (MyApp) this.getApplicationContext();
    }

    @SuppressLint("SetTextI18n")
    private void startupDialog() {
        final Dialog dialog = ViewSupports.materialDialog(this, R.layout.guide_notice_board);
        dialog.setCancelable(false);
        final ViewPager viewPager_slide = dialog.findViewById(R.id.view_pager);
        dotLayout = dialog.findViewById(R.id.dot_layout);
        ImageButton next_btn = dialog.findViewById(R.id.next_btn);
        back_btn = dialog.findViewById(R.id.back_btn);
        final int[] slideImages = {
                R.mipmap.leaflet,
                R.mipmap.heart_thanks,
                R.mipmap.comment,
                R.mipmap.star,
                R.mipmap.notepad,
                R.mipmap.shield,
                R.mipmap.swipe_left
        };
        String[] SlideHeading = {
                "Welcome", "Thanks", "Comments", "Starring", "Compose", "Shield", "Swipe Left"
        };
        String[] slide_contents = {
                "Welcome to Virtual Noticeboard",
                "Express your love by thanking to the notice owner",
                "Deploy your comments about the notice",
                "Starring make you easy to find wanted notice",
                "This feature only for Shielded Users",
                "Shield is a security for prevent fake notices, also students don't have the permission to compose notice",
                "Swipe Left to see your notice filtered for you"
        };
        back_btn.setVisibility(View.GONE);
        SlideAdapter slideAdapter = new SlideAdapter(this, SlideHeading, slide_contents, slideImages);
        viewPager_slide.setAdapter(slideAdapter);
        addDotsIndicator(0);
        viewPager_slide.addOnPageChangeListener(viewListener);

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage < slideImages.length - 1)
                    viewPager_slide.setCurrentItem(currentPage + 1);
                else {
                    dialog.cancel();
                    viewPager.setCurrentItem(1);
                }
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager_slide.setCurrentItem(currentPage - 1);
            }
        });
        dialog.show();
    }

    public void addDotsIndicator(int position) {
        dots = new TextView[7];
        dotLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dots[i].setPadding(5, 0, 5, 0);
            dots[i].setTextColor(getResources().getColor(R.color.lightBlue));

            dotLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.darkBlue));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            currentPage = i;
            if (i == 0) {
                back_btn.setVisibility(View.GONE);
                back_btn.setAnimation(AnimationUtils.loadAnimation(NoticeBoardActivity.this, R.anim.fade_out));
            } else if (i == dots.length - 1) {
                back_btn.setVisibility(View.VISIBLE);
            } else {
                if (back_btn.getVisibility() == View.GONE) {
                    back_btn.setVisibility(View.VISIBLE);
                    back_btn.setAnimation(AnimationUtils.loadAnimation(NoticeBoardActivity.this, R.anim.fade_in));
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

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
