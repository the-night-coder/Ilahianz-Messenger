package com.nightcoder.ilahianz;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightcoder.ilahianz.ChatUI.Fragments.ChatFragment;
import com.nightcoder.ilahianz.ChatUI.Fragments.HelpFragment;
import com.nightcoder.ilahianz.ChatUI.Fragments.SearchFragment;
import com.nightcoder.ilahianz.Models.UserData;
import com.nightcoder.ilahianz.Supports.ViewSupports;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.nightcoder.ilahianz.Literals.StringConstants.CHAT_FRAGMENT_TAG;
import static com.nightcoder.ilahianz.Literals.StringConstants.HELP_FRAGMENT_TAG;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ABOUT;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BIO;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BIRTHDAY_PRIVACY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BIRTH_DAY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BIRTH_MONTH;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BIRTH_YEAR;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BLOOD_DONATE;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_CATEGORY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_CITY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_CLASS_NAME;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_DEPARTMENT;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_DISTRICT;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_EMAIL;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_EMAIL_PRIVACY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_GENDER;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID_NUMBER;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_IMAGE_URL;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_LAST_SEEN;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_LATITUDE;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_LOCATION_PRIVACY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_LONGITUDE;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_NICKNAME;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_PHONE_PRIVACY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_PH_NUMBER;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_PROFILE_PRIVACY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_THUMBNAIL;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_USERNAME;
import static com.nightcoder.ilahianz.Literals.StringConstants.SEARCH_FRAGMENT_TAG;
import static com.nightcoder.ilahianz.Literals.StringConstants.USER_INFO_SP;

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
        readData();
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

    private void readData() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserData data = dataSnapshot.getValue(UserData.class);
                assert data != null;
                setUserInfo(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    private void setUserInfo(UserData data) {
        setUserInfo(KEY_USERNAME, data.getUsername());
        setUserInfo(KEY_PH_NUMBER, data.getPhoneNumber());
        setUserInfo(KEY_ID_NUMBER, data.getIdNumber());
        setUserInfo(KEY_GENDER, data.getGender());
        setUserInfo(KEY_CLASS_NAME, data.getClassName());
        setUserInfo(KEY_EMAIL, data.getEmail());
        setUserInfo(KEY_BIRTH_DAY, data.getBirthday());
        setUserInfo(KEY_BIRTH_YEAR, data.getBirthYear());
        setUserInfo(KEY_BIRTH_MONTH, data.getBirthMonth());
        setUserInfo(KEY_NICKNAME, data.getNickname());
        setUserInfo(KEY_CATEGORY, data.getCategory());
        setUserInfo(KEY_ABOUT, data.getDescription());
        setUserInfo(KEY_ID, data.getId());
        setUserInfo(KEY_LONGITUDE, data.getLongitude());
        setUserInfo(KEY_LATITUDE, data.getLatitude());
        setUserInfo(KEY_PROFILE_PRIVACY, data.getProfilePrivacy());
        setUserInfo(KEY_LOCATION_PRIVACY, data.getLocationPrivacy());
        setUserInfo(KEY_EMAIL_PRIVACY, data.getEmailPrivacy());
        setUserInfo(KEY_PHONE_PRIVACY, data.getPhonePrivacy());
        setUserInfo(KEY_BIRTHDAY_PRIVACY, data.getBirthdayPrivacy());
        setUserInfo(KEY_LAST_SEEN, data.getLastSeenPrivacy());
        setUserInfo(KEY_CITY, data.getCity());
        setUserInfo(KEY_DISTRICT, data.getDistrict());
        setUserInfo(KEY_DEPARTMENT, data.getDepartment());
        setUserInfo(KEY_BIO, data.getBio());
        setUserInfo(KEY_THUMBNAIL, data.getThumbnailURL());
        setUserInfo(KEY_IMAGE_URL, data.getImageURL());
        setUserInfo(KEY_BLOOD_DONATE, data.getBloodDonate());
    }

    private void setUserInfo(String key, String value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_INFO_SP, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

//    private String getUserInfo(String key) {
//        SharedPreferences preferences = mContext.getSharedPreferences(USER_INFO_SP, MODE_PRIVATE);
//        return preferences.getString(key, "none");
//    }

    @Override
    public void onFragmentChanged() {
        if (CHAT_FRAGMENT_TAG.equals(currentFragment)) {
            navigationView.setSelectedItemId(R.id.nav_chats);
        }
    }
}

interface FragmentCallbackListener {
    void onFragmentChanged();
}
