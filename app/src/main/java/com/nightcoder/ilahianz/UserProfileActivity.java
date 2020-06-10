package com.nightcoder.ilahianz;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightcoder.ilahianz.Models.UserData;
import com.nightcoder.ilahianz.Supports.Graphics;
import com.nightcoder.ilahianz.Supports.ViewSupports;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;

public class UserProfileActivity extends AppCompatActivity {

    private String userId;
    private TextView name, category, nameTop;
    private CircleImageView profileImage;
    private TabLayout tabLayout;
    private LinearLayout appbarContainer;
    private ImageView profilePictureBanner;
    private AppBarLayout appBarLayout;
    protected Context mContext;
    private LinearLayout toolbarLayout;
    private ImageButton closeBtn;
    private RelativeLayout container;
    private Handler handler = new Handler(Looper.getMainLooper());
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private boolean expandedAppBar = false;
    public static final String TAG = "UserProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mContext = UserProfileActivity.this;
        initViews();
        initViewParams();
        initInterfaces();
        init();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initAnimation();
            }
        }, 600);
    }

    private void initViews() {
        name = findViewById(R.id.profile_name);
        category = findViewById(R.id.profile_category);
        appbarContainer = findViewById(R.id.appbar_container);
        profilePictureBanner = findViewById(R.id.profile_image_container);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        container = findViewById(R.id.container);
        tabLayout = findViewById(R.id.tab_profile);
        appBarLayout = findViewById(R.id.appbar);
        closeBtn = findViewById(R.id.close_btn);
        nameTop = findViewById(R.id.name_top);
        toolbarLayout = findViewById(R.id.toolbar_height);
        profileImage = findViewById(R.id.profile_image);
        profilePictureBanner.setVisibility(View.GONE);
        profileImage.setVisibility(View.GONE);
        name.setVisibility(View.GONE);
        category.setVisibility(View.GONE);
    }

    private void initAnimation() {
        profilePictureBanner.setVisibility(View.VISIBLE);
        profileImage.setVisibility(View.VISIBLE);
        name.setVisibility(View.VISIBLE);
        category.setVisibility(View.VISIBLE);
        name.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_in));
        category.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_in));
        profilePictureBanner.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_in));
        profileImage.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_enter_animation));
    }

    private void initInterfaces() {
        closeBtn.setOnClickListener(clickListener);
        appBarLayout.addOnOffsetChangedListener(offsetChangedListener);
        appBarLayout.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.close_btn:
                    finish();
                    break;
                case R.id.appbar:
                    appBarLayout.setExpanded(true);
                    handler.post(intentProfileView);
                    break;
            }
        }
    };

    private Runnable intentProfileView = new Runnable() {
        @Override
        public void run() {
            ActivityOptions options =
                    ActivityOptions.makeSceneTransitionAnimation(UserProfileActivity.this,
                            new Pair<View, String>(profilePictureBanner, "profileImage"));
            startActivity(new Intent(mContext, ProfileImageViewActivity.class), options.toBundle());
        }
    };

    private void initViewParams() {
        int[] dimen = Graphics.getResolution(mContext);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dimen[0] - 300
        );
        appbarContainer.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dimen[0] - 200
        ));
        toolbarLayout.setLayoutParams(params);
        nameTop.setVisibility(View.GONE);
        container.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        container.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private AppBarLayout.OnOffsetChangedListener offsetChangedListener
            = new AppBarLayout.OnOffsetChangedListener() {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
            Log.d(TAG, "Offset : " + i);
            Log.d(TAG, "toolbar Height : " + (-collapsingToolbarLayout.getHeight()));
            ViewGroup.MarginLayoutParams margin = (ViewGroup.MarginLayoutParams)
                    collapsingToolbarLayout.getLayoutParams();
            if (i == -collapsingToolbarLayout.getHeight() - margin.bottomMargin) {
                Log.d(TAG, "Collapsed");
                ViewSupports.visibilityFadeAnimation(600, nameTop, container, View.VISIBLE);
                profileImage.setVisibility(View.GONE);
                name.setVisibility(View.GONE);
                category.setVisibility(View.GONE);
                expandedAppBar = false;
            } else {
                final Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                ViewSupports.visibilityFadeAnimation(200, nameTop, container, View.GONE);

                if (i == 0) {
                    expandedAppBar = true;
                    if (closeBtn.getVisibility() == View.GONE) {
                        closeBtn.setVisibility(View.VISIBLE);
                        profileImage.setVisibility(View.VISIBLE);
                        name.setVisibility(View.VISIBLE);
                        category.setVisibility(View.VISIBLE);
                        name.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_in));
                        category.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_in));
                        closeBtn.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_enter_animation));
                        profileImage.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_enter_animation));
                    }
                } else {
                    expandedAppBar = false;
                    if (closeBtn.getVisibility() == View.VISIBLE) {
                        closeBtn.setVisibility(View.GONE);
                        closeBtn.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_exit_animation));
                    }
                }
            }
        }
    };


    private void init() {
        userId = getIntent().getStringExtra(KEY_ID);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserData userData = dataSnapshot.getValue(UserData.class);
                assert userData != null;
                if (userData.getThumbnailURL().equals("default")) {
                    profileImage.setImageResource(R.drawable.ic_person);
                } else {
                    Picasso.with(mContext).load(userData.getThumbnailURL()).into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
