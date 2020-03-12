package com.nightcoder.ilahianz;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nightcoder.ilahianz.ChatUI.Fragments.ChatFragment;
import com.nightcoder.ilahianz.MainActivityFragments.EventsFragment;
import com.nightcoder.ilahianz.MainActivityFragments.NotificationFragment;
import com.nightcoder.ilahianz.MainActivityFragments.SearchFragment;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.nightcoder.ilahianz.Literals.StringConstants.CHAT_FRAGMENT_TAG;
import static com.nightcoder.ilahianz.Literals.StringConstants.HELP_FRAGMENT_TAG;
import static com.nightcoder.ilahianz.Literals.StringConstants.SEARCH_FRAGMENT_TAG;
import static com.nightcoder.ilahianz.Literals.StringConstants.SEARCH_USER_FRAGMENT_TAG;

public class MainActivity extends AppCompatActivity implements FragmentCallbackListener {

    protected MyApp myApp;
    private BottomNavigationView navigationView;
    private Context mContext;
    private TextView heading;
    private AppBarLayout appBarLayout;
    private String currentFragment;
    private FragmentCallbackListener listener;
    private CircleImageView profileImage;
    private ImageButton searchButton;
    private SearchFragment searchFragment;
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
    private BottomNavigationView.OnNavigationItemSelectedListener navListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.nav_chats:
                    changeFragment(new ChatFragment(), CHAT_FRAGMENT_TAG);
                    heading.setText(R.string.chats);
                    break;
                case R.id.nav_search:
                    changeFragment(new EventsFragment(mContext), SEARCH_FRAGMENT_TAG);
                    heading.setText(R.string.search);
                    break;
                case R.id.nav_favorite:
                    changeFragment(new NotificationFragment(mContext), HELP_FRAGMENT_TAG);
                    heading.setText(R.string.notifications);
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
    }

    private void init() {
        myApp = (MyApp) this.getApplicationContext();
        navigationView.setOnNavigationItemSelectedListener(navListener);
        Fragment fragment = new ChatFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, fragment, CHAT_FRAGMENT_TAG)
                .commit();
        currentFragment = CHAT_FRAGMENT_TAG;
        EmojiManager.destroy();
        EmojiManager.install(new GoogleEmojiProvider());
        listener = this;
    }

    private void initInterface() {
        searchButton.setOnClickListener(clickListener);
        profileImage.setOnClickListener(clickListener);
    }

    private void initViews() {
        navigationView = findViewById(R.id.bottom_nav);
        mContext = MainActivity.this;
        heading = findViewById(R.id.heading);
        appBarLayout = findViewById(R.id.appbar);
        profileImage = findViewById(R.id.profile_photo);
        searchButton = findViewById(R.id.search_button);
    }

    private void openSearch() {
        searchFragment = new SearchFragment(mContext);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_up, R.anim.slide_down, R.anim.slide_up, R.anim.slide_down)
                .add(R.id.frame, searchFragment, SEARCH_USER_FRAGMENT_TAG)
                .addToBackStack(CHAT_FRAGMENT_TAG)
                .commit();
        currentFragment = SEARCH_USER_FRAGMENT_TAG;

        hideAppBarAnimation();
    }

    @Override
    public void onBackPressed() {
        if (!currentFragment.equals(CHAT_FRAGMENT_TAG)) {
            if (getFragment(SEARCH_USER_FRAGMENT_TAG) == searchFragment && searchFragment != null) {
                Fragment fragment = getFragment(SEARCH_USER_FRAGMENT_TAG);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_up, R.anim.slide_down, R.anim.slide_up, R.anim.slide_down)
                        .detach(fragment)
                        .commit();
                changeFragment(new ChatFragment(), CHAT_FRAGMENT_TAG);
                openAppBarAnimation();
            } else {
                changeFragment(new ChatFragment(), CHAT_FRAGMENT_TAG);
                openAppBarAnimation();
            }
        } else {
            MainActivity.this.moveTaskToBack(true);
        }
    }

    private void hideAppBarAnimation() {
        appBarLayout.setVisibility(View.GONE);
        navigationView.setVisibility(View.GONE);
        appBarLayout.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.appbar_exit_anim));
        navigationView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.snackbar_exit_animation));
    }

    private void openAppBarAnimation() {
        appBarLayout.setVisibility(View.VISIBLE);
        navigationView.setVisibility(View.VISIBLE);
        appBarLayout.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.appbar_enter_anim));
        navigationView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.snackbar_enter_animation));
    }

    private void changeFragment(Fragment fragment, String tag) {
        if (getFragment(tag) != getFragment(currentFragment)) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame, fragment, tag)
                    .remove(getFragment(currentFragment))
                    .commit();
            currentFragment = tag;
//            dataChangeListener = (DataChangeListener) fragment;
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM yyyy", Locale.US);
        Log.d("Time", simpleDateFormat.format(new Date()));
        clearReference();
        super.onPause();
    }

    private void clearReference() {
        Activity activity = myApp.getCurrentActivity();
        if (this.equals(activity)) {
            myApp.setCurrentActivity(this);
        }
    }
}

interface FragmentCallbackListener {
    void onFragmentChanged();
}
