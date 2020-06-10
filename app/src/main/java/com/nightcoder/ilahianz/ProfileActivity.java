package com.nightcoder.ilahianz;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.nightcoder.ilahianz.ChatUI.Fragments.AccountFragments.CollegeInfoFragment;
import com.nightcoder.ilahianz.ChatUI.Fragments.AccountFragments.PersonalInfoFragment;
import com.nightcoder.ilahianz.ChatUI.Fragments.AccountFragments.PrivacyFragment;
import com.nightcoder.ilahianz.ChatUI.Fragments.AccountFragments.SettingsFragment;
import com.nightcoder.ilahianz.Listeners.ProfileActivity.EditInfoListener;
import com.nightcoder.ilahianz.Listeners.ProfileActivity.EventChangeListener;
import com.nightcoder.ilahianz.Listeners.ProfileActivity.PersonalInfoFragmentListener;
import com.nightcoder.ilahianz.Supports.MemorySupports;
import com.nightcoder.ilahianz.Supports.Network;
import com.nightcoder.ilahianz.Supports.ViewSupports;
import com.nightcoder.ilahianz.Utils.FileUtils;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.nightcoder.ilahianz.Literals.IntegerConstants.CAMERA_REQUEST;
import static com.nightcoder.ilahianz.Literals.IntegerConstants.IMAGE_REQUEST;
import static com.nightcoder.ilahianz.Literals.StringConstants.DEFAULT;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_IMAGE_URL;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_USERNAME;
import static com.nightcoder.ilahianz.Literals.StringConstants.USER_INFO_SP;
import static com.nightcoder.ilahianz.Models.Notice.KEY_ID;
import static com.nightcoder.ilahianz.Models.Notice.TYPE_IMAGE;

public class ProfileActivity extends AppCompatActivity implements EditInfoListener, PersonalInfoFragmentListener {

    private Context mContext;
    private CircleImageView profileImage;
    private TextView name;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    protected MyApp myApp;
    private ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    private EventChangeListener eventChangeListener;
    private Handler handler = new Handler(Looper.getMainLooper());

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

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewSupports.visibilitySlideAnimation(Gravity.BOTTOM, 600, viewPager,
                        (ViewGroup) viewPager.getRootView(), View.VISIBLE);
            }
        }, 200);

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
                                                3000, R.drawable.ic_check_circle_black_24dp);
                                        MemorySupports.setUserInfo(mContext, key, data);
                                        Log.d("Current Fragment", viewPager.getCurrentItem() + "");
                                        int pos = viewPager.getCurrentItem();
                                        if (Objects.requireNonNull(viewPager.getAdapter()).getCount() != 0) {
                                            eventChangeListener = (EventChangeListener) viewPagerAdapter.getFragment(pos);
                                            eventChangeListener.onDataChange();

                                        }
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

    @Override
    public void onProfileEdit() {
        final Dialog dialog = ViewSupports.materialDialog(mContext, R.layout.choose_image_source);
        dialog.show();

        LinearLayout delete = dialog.findViewById(R.id.linearLayout);
        LinearLayout camera = dialog.findViewById(R.id.linearLayout2);
        LinearLayout gallery = dialog.findViewById(R.id.linearLayout4);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
                dialog.cancel();
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
                dialog.cancel();
            }
        });
    }


    private void openCamera() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST);
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, CAMERA_REQUEST);
            } else
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
        } else {
            Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera, CAMERA_REQUEST);
        }
    }

    private void openImage() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, IMAGE_REQUEST);
            } else ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, IMAGE_REQUEST);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, IMAGE_REQUEST);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void compressImage(File actualImage) {
        if (actualImage != null) {
            final ProgressDialog pd = new ProgressDialog(mContext);
            pd.setMessage("Compressing...");
            pd.show();
            new Compressor(this)
                    .setQuality(50)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
                    .compressToFileAsFlowable(actualImage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<File>() {
                        @Override
                        public void accept(File file) {
                            pd.dismiss();
                            myApp.uploadMedia(Uri.fromFile(file));
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            throwable.printStackTrace();
                            pd.dismiss();
                        }
                    });

        }
    }



    private void startCrop(Uri uri) {
        String destination = MemorySupports.getUserInfo(mContext, KEY_USERNAME);
        destination += ".jpeg";
        UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destination)))
                .withAspectRatio(1, 1)
                .withMaxResultSize(640, 640)
                .withOptions(getOption())
                .start(this);
    }

    private UCrop.Options getOption() {
        UCrop.Options options = new UCrop.Options();
        options.setToolbarTitle("Profile Image");
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(50);
        options.setHideBottomControls(true);
        options.setActiveWidgetColor(Color.WHITE);
        options.setStatusBarColor(Color.WHITE);
        return options;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageUri;
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();

            if (imageUri != null) {
                startCrop(imageUri);
            }
//            if (imageUri != null) {
//                try {
//                    if (Build.VERSION.SDK_INT != Build.VERSION_CODES.LOLLIPOP) {
//                        if (ContextCompat.checkSelfPermission(this,
//                                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                            compressImage(FileUtils.from(this, imageUri));
//                            //startCrop(imageUri);
//                        } else {
//                            Toast.makeText(this, "Storage permission not granted", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        compressImage(FileUtils.from(this, imageUri));
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            if (imageUri != null) {
                try {
                    if (Build.VERSION.SDK_INT != Build.VERSION_CODES.LOLLIPOP) {
                        if (ContextCompat.checkSelfPermission(this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            compressImage(FileUtils.from(this, imageUri));
                        }
                    } else {
                        compressImage(FileUtils.from(this, imageUri));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Log.d("Scrop", "null");
            assert data != null;
            Uri uri = UCrop.getOutput(data);
            if (uri != null) {
                Log.d("Scrop", "Croped");
            }
        }
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
