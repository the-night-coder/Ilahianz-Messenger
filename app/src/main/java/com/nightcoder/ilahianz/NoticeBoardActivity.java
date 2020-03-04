package com.nightcoder.ilahianz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.nightcoder.ilahianz.NoticeBoardFragments.AllFragment;
import com.nightcoder.ilahianz.NoticeBoardFragments.RefershListener;

import java.util.ArrayList;

public class NoticeBoardActivity extends AppCompatActivity {

    private TabLayout tab;
    private ViewPager viewPager;
    private ImageButton composeButton;
    private ImageButton refreshButtn;
    RefershListener refershListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board);
        init();

        tab.setupWithViewPager(viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new AllFragment(this), "All");
        viewPagerAdapter.addFragment(new AllFragment(this), "Your Notice");
        viewPagerAdapter.addFragment(new AllFragment(this), "College");
        refershListener = (RefershListener) viewPagerAdapter.getFragment(1);

        viewPager.setAdapter(viewPagerAdapter);

        composeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NoticeBoardActivity.this, ComposeNoticeActivity.class));
            }
        });

    }

    private void init() {
        tab = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        composeButton = findViewById(R.id.compose_button);
        refreshButtn = findViewById(R.id.refresh_button);
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
