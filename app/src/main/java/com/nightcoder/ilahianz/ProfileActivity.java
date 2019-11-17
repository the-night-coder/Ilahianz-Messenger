package com.nightcoder.ilahianz;

import android.app.Activity;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nightcoder.ilahianz.ChatUI.Fragments.AccountFragments.CollegeInfoFragment;
import com.nightcoder.ilahianz.ChatUI.Fragments.AccountFragments.PersonalInfoFragment;
import com.nightcoder.ilahianz.ChatUI.Fragments.AccountFragments.PrivacyFragment;
import com.nightcoder.ilahianz.ChatUI.Fragments.AccountFragments.SettingsFragment;
import com.nightcoder.ilahianz.Listeners.ProfileActivity.EditInfoListener;
import com.nightcoder.ilahianz.Supports.MemorySupports;
import com.nightcoder.ilahianz.Supports.Network;
import com.nightcoder.ilahianz.Supports.ViewSupports;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.nightcoder.ilahianz.Literals.StringConstants.DEFAULT;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_IMAGE_URL;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_USERNAME;
import static com.nightcoder.ilahianz.Literals.StringConstants.USER_INFO_SP;

public class ProfileActivity extends AppCompatActivity implements EditInfoListener {

    private Context mContext;
    private CircleImageView profileImage;
    private TextView name;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    protected MyApp myApp;
    private ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mContext = ProfileActivity.this;
        tabLayout = findViewById(R.id.tab_account);
        viewPager = findViewById(R.id.view_pager);
        tabLayout.setupWithViewPager(viewPager);
        profileImage = findViewById(R.id.profile_image);
        ImageButton closeBtn = findViewById(R.id.close_btn);
        name = findViewById(R.id.profile_name);
        viewPager.setVisibility(View.GONE);
        myApp = (MyApp) this.getApplicationContext();

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


        viewPagerAdapter.addFragment(new PersonalInfoFragment(mContext), "Personal Info");
        viewPagerAdapter.addFragment(new CollegeInfoFragment(mContext), "Academic Info");
        viewPagerAdapter.addFragment(new PrivacyFragment(mContext), "Privacy");
        viewPagerAdapter.addFragment(new SettingsFragment(mContext), "Settings");

        viewPager.setAdapter(viewPagerAdapter);
        name.setText(getUserInfo(KEY_USERNAME));

        if (!getUserInfo(KEY_IMAGE_URL).equals(DEFAULT)) {
            Glide.with(mContext).load(getUserInfo(KEY_IMAGE_URL)).into(profileImage);
        }
    }

    private String getUserInfo(String key) {
        SharedPreferences preferences = mContext.getSharedPreferences(USER_INFO_SP, MODE_PRIVATE);
        return preferences.getString(key, "none");
    }

    @Override
    public void setEdits(final String key, final String data) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (Network.Connected(mContext)) {
                    FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                    assert fUser != null;
                    DatabaseReference reference = FirebaseDatabase.getInstance()
                            .getReference("Users").child(fUser.getUid()).child(key);
                    reference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ViewSupports.materialSnackBar(mContext, "Changes Applied !",
                                                4000, R.drawable.ic_check_circle_black_24dp);
                                        MemorySupports.setUserInfo(mContext, key, data);
                                    }
                                });
                            } else {
                                ViewSupports.materialSnackBar(mContext, "Changes can't Applied !",
                                        4000, R.drawable.ic_close_black_24dp);
                            }

                        }
                    });
                } else {
                    ViewSupports.materialSnackBar(mContext, "Connection required !",
                            4000, R.drawable.ic_info_black_24dp);

                }
            }
        }.run();
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

    private void clearReference() {
        Activity activity = myApp.getCurrentActivity();
        if (this.equals(activity)) {
            myApp.setCurrentActivity(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        myApp.setCurrentActivity(this);
    }

    @Override
    protected void onDestroy() {
        clearReference();
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        overridePendingTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        startActivity(getIntent());
        overridePendingTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
    }

    @Override
    protected void onPause() {
        ViewSupports.visibilitySlideAnimation(Gravity.BOTTOM, 400, viewPager, (ViewGroup) viewPager.getRootView(), View.GONE);
        clearReference();
        super.onPause();

    }
}
