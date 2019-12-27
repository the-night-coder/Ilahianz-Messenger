package com.nightcoder.ilahianz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.nightcoder.ilahianz.Adapters.SlideAdapter;

public class StartupActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout dotLayout;
    private TextView[] dots;
    private int currentPage;
    private Button next_btn, back_btn;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        viewPager = findViewById(R.id.view_pager);
        dotLayout = findViewById(R.id.dot_layout);
        next_btn = findViewById(R.id.next_btn);
        back_btn = findViewById(R.id.back_btn);
        back_btn.setVisibility(View.GONE);
        SlideAdapter slideAdapter = new SlideAdapter(this);
        viewPager.setAdapter(slideAdapter);
        addDotsIndicator(0);
        viewPager.addOnPageChangeListener(viewListener);
        next_btn.setText("Next");

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.equals(next_btn.getText(), "Finish"))
                    viewPager.setCurrentItem(currentPage + 1);
                else {
                    startActivity(new Intent(StartupActivity.this, SignActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                }
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(currentPage - 1);
            }
        });
    }

    public void addDotsIndicator(int position) {
        dots = new TextView[4];
        dotLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(40);
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
                next_btn.setText("Next");
                back_btn.setVisibility(View.GONE);
                back_btn.setAnimation(AnimationUtils.loadAnimation(StartupActivity.this, R.anim.fade_out));
            } else if (i == dots.length - 1) {
                next_btn.setText("Finish");
                back_btn.setVisibility(View.VISIBLE);
            } else {
                next_btn.setText("Next");
                if (back_btn.getVisibility() == View.GONE) {
                    back_btn.setVisibility(View.VISIBLE);
                    back_btn.setAnimation(AnimationUtils.loadAnimation(StartupActivity.this, R.anim.fade_in));
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
}
