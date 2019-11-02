package com.nightcoder.ilahianz;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nightcoder.ilahianz.ChatUI.Fragments.ChatFragment;
import com.nightcoder.ilahianz.MainActivityFragments.HelpFragment;
import com.nightcoder.ilahianz.MainActivityFragments.SearchFragment;
import com.nightcoder.ilahianz.Supports.ViewSupports;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.nightcoder.ilahianz.Literals.StringConstants.CHAT_FRAGMENT_TAG;
import static com.nightcoder.ilahianz.Literals.StringConstants.HELP_FRAGMENT_TAG;
import static com.nightcoder.ilahianz.Literals.StringConstants.SEARCH_FRAGMENT_TAG;

public class MainActivity extends AppCompatActivity implements FragmentCallbackListener {

    private BottomNavigationView navigationView;
    private Context mContext;
    private TextView heading;
    private AppBarLayout appBarLayout;
    private String currentFragment;
    private FragmentCallbackListener listener;
    private CircleImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        init();

        navigationView.setOnNavigationItemSelectedListener(navListener);
        profileImage.setOnClickListener(clickListener);
        Fragment fragment = new ChatFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, fragment, CHAT_FRAGMENT_TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
        currentFragment = CHAT_FRAGMENT_TAG;
        listener = this;
    }

    private void init(){
        navigationView = findViewById(R.id.bottom_nav);
        mContext = MainActivity.this;
        heading = findViewById(R.id.heading);
        appBarLayout = findViewById(R.id.appbar);
        profileImage = findViewById(R.id.profile_photo);

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.profile_photo:
                    startActivity(new Intent(mContext, ProfileActivity.class));
                    break;
                case R.id.scan_button:
                    startActivity(new Intent(mContext, ScanProfileActivity.class));
                    break;
            }
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener navListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.nav_chats:
                    changeFragment(new ChatFragment(), CHAT_FRAGMENT_TAG);
                    heading.setText(R.string.chats);
                    openAppBarAnimation();
                    break;
                case R.id.nav_search:
                    changeFragment(new SearchFragment(mContext), SEARCH_FRAGMENT_TAG);
                    heading.setText(R.string.search);
                    hideAppBarAnimation();
                    break;
                case R.id.nav_favorite:
                    changeFragment(new HelpFragment(), HELP_FRAGMENT_TAG);
                    heading.setText(R.string.notifications);
                    openAppBarAnimation();
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
    public void onBackPressed() {
        if (!currentFragment.equals(CHAT_FRAGMENT_TAG)) {
            changeFragment(new ChatFragment(), CHAT_FRAGMENT_TAG);
        } else {
            MainActivity.this.moveTaskToBack(true);
        }
    }

    private void hideAppBarAnimation() {
        ViewSupports.visibilitySlideAnimation(Gravity.TOP, 600,
                appBarLayout, (ViewGroup) appBarLayout.getRootView(), View.GONE);
    }

    private void openAppBarAnimation() {
        ViewSupports.visibilitySlideAnimation(Gravity.TOP, 600,
                appBarLayout, (ViewGroup) appBarLayout.getRootView(), View.VISIBLE);
    }

    private void changeFragment(Fragment fragment, String tag) {
        if (getFragment(tag) != getFragment(currentFragment)) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame, fragment, tag)
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .remove(getFragment(currentFragment))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
            currentFragment = tag;
            listener.onFragmentChanged();
        }
    }

    private Fragment getFragment(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
    }



    @Override
    public void onFragmentChanged() {
        if (CHAT_FRAGMENT_TAG.equals(currentFragment)) {
            navigationView.setSelectedItemId(R.id.nav_chats);
        }
    }

    @Override
    protected void onPause() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM yyyy", Locale.US);
        Log.d("Time", simpleDateFormat.format(new Date()));
        super.onPause();
    }
}

interface FragmentCallbackListener {
    void onFragmentChanged();
}
