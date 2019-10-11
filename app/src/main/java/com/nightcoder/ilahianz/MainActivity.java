package com.nightcoder.ilahianz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nightcoder.ilahianz.ChatUI.BlankFragment;
import com.nightcoder.ilahianz.ChatUI.Fragments.AccountFragment;
import com.nightcoder.ilahianz.ChatUI.Fragments.ChatFragment;
import com.nightcoder.ilahianz.ChatUI.Fragments.HelpFragment;
import com.nightcoder.ilahianz.ChatUI.Fragments.SearchFragment;

import static com.nightcoder.ilahianz.Literals.StringConstants.ACCOUNT_FRAGMENT_TAG;
import static com.nightcoder.ilahianz.Literals.StringConstants.CHAT_FRAGMENT_TAG;
import static com.nightcoder.ilahianz.Literals.StringConstants.HELP_FRAGMENT_TAG;
import static com.nightcoder.ilahianz.Literals.StringConstants.LOADING_FRAGMENT_TAG;
import static com.nightcoder.ilahianz.Literals.StringConstants.SEARCH_FRAGMENT_TAG;
import static com.nightcoder.ilahianz.Literals.StringConstants.SIGN_FRAGMENT_TAG;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigationView;
    private Context mContext;
    private TextView heading;
    private AppBarLayout appBarLayout;
    private String currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView = findViewById(R.id.bottom_nav);
        mContext = MainActivity.this;
        heading = findViewById(R.id.heading);
        appBarLayout = findViewById(R.id.appbar);
        navigationView.setOnNavigationItemSelectedListener(navListener);
        Fragment fragment = new ChatFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, fragment, CHAT_FRAGMENT_TAG)
                .setCustomAnimations(R.anim.slide_left, R.anim.slide_right, R.anim.slide_left, R.anim.slide_right)
                .attach(fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
        currentFragment = CHAT_FRAGMENT_TAG;
    }

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
                case R.id.nav_account:
                    changeFragment(new AccountFragment(mContext), ACCOUNT_FRAGMENT_TAG);
                    heading.setText(R.string.account);
                    hideAppBarAnimation();
                    break;
                case R.id.nav_help:
                    changeFragment(new HelpFragment(), HELP_FRAGMENT_TAG);
                    heading.setText(R.string.help);
                    openAppBarAnimation();
                    break;
                case R.id.nav_search:
                    changeFragment(new SearchFragment(), SEARCH_FRAGMENT_TAG);
                    heading.setText(R.string.search);
                    openAppBarAnimation();
                    break;
                case R.id.nav_favorite:
                    changeFragment(new HelpFragment(), HELP_FRAGMENT_TAG);
                    heading.setText(R.string.notifications);
                    openAppBarAnimation();
                    break;
            }

            int colorFrom = getResources().getColor(R.color.white);
            int colorTo = getResources().getColor(R.color.blue_dark);
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
        MainActivity.this.moveTaskToBack(true);
    }

    private void hideAppBarAnimation() {
        appBarLayout.setVisibility(View.GONE);
    }

    private void openAppBarAnimation() {
        appBarLayout.setVisibility(View.VISIBLE);
    }

    private void changeFragment(Fragment fragment, String tag) {
        if (getFragment(tag) != getFragment(currentFragment)) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame, fragment, tag)
                    .setCustomAnimations(R.anim.slide_left, R.anim.slide_right, R.anim.slide_left, R.anim.slide_right)
                    .attach(fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
            currentFragment = tag;
        }
    }

    private Fragment getFragment(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
    }
}
