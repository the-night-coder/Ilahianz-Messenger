package com.nightcoder.ilahianz;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightcoder.ilahianz.ProfileActivityFragments.BarCodeFragment;
import com.nightcoder.ilahianz.Fragments.ForgotPasswordFragment;
import com.nightcoder.ilahianz.Fragments.LoadingFragment;
import com.nightcoder.ilahianz.Fragments.RegisterFragment;
import com.nightcoder.ilahianz.Fragments.SignFragment;
import com.nightcoder.ilahianz.Fragments.VerifyFragment;
import com.nightcoder.ilahianz.Listeners.SignActivityListeners.LoadingFragmentListener;
import com.nightcoder.ilahianz.Listeners.FragmentListeners.QRCoderFragmentCallback;
import com.nightcoder.ilahianz.Listeners.SignActivityListeners.RegisterFragmentListener;
import com.nightcoder.ilahianz.Listeners.SignActivityListeners.SignInFragmentListener;
import com.nightcoder.ilahianz.Listeners.FragmentListeners.VFragmentListener;
import com.nightcoder.ilahianz.Listeners.SignActivityListeners.VerifyFragmentListener;
import com.nightcoder.ilahianz.Listeners.SignActivityListeners.LogInCompleteCallback;
import com.nightcoder.ilahianz.Listeners.QRCodeListener;
import com.nightcoder.ilahianz.Models.UserData;
import com.nightcoder.ilahianz.Supports.MemorySupports;
import com.nightcoder.ilahianz.Supports.Network;
import com.tomer.fadingtextview.FadingTextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import cdflynn.android.library.checkview.CheckView;

import static com.nightcoder.ilahianz.Literals.IntegerConstats.CAMERA_REQUEST;
import static com.nightcoder.ilahianz.Literals.IntegerConstats.ID_CAMERA_REQUEST;
import static com.nightcoder.ilahianz.Literals.IntegerConstats.REQUEST_QR_CODE_RESULT;
import static com.nightcoder.ilahianz.Literals.StringConstants.DEFAULT;
import static com.nightcoder.ilahianz.Literals.StringConstants.EVERYONE;
import static com.nightcoder.ilahianz.Literals.StringConstants.FORGOT_PASS_FRAGMENT_TAG;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ABOUT;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ACADEMIC_YEAR_FROM;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ACADEMIC_YEAR_TO;
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
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_JOIN_DATE;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_LAST_SEEN_DATE;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_LAST_SEEN_PRIVACY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_LATITUDE;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_LOCATION_PRIVACY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_LONGITUDE;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_NICKNAME;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_PHONE_PRIVACY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_PH_NUMBER;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_PROFILE_PRIVACY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_STATUS;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_THUMBNAIL;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_USERNAME;
import static com.nightcoder.ilahianz.Literals.StringConstants.LOADING_FRAGMENT_TAG;
import static com.nightcoder.ilahianz.Literals.StringConstants.NOT_PROVIDED;
import static com.nightcoder.ilahianz.Literals.StringConstants.OFFLINE;
import static com.nightcoder.ilahianz.Literals.StringConstants.QR_CODE_FRAGMENT_TAG;
import static com.nightcoder.ilahianz.Literals.StringConstants.QR_CODE_RESULT_KEY;
import static com.nightcoder.ilahianz.Literals.StringConstants.REG_FRAGMENT_TAG;
import static com.nightcoder.ilahianz.Literals.StringConstants.SIGN_FRAGMENT_TAG;
import static com.nightcoder.ilahianz.Literals.StringConstants.VERIFY_FRAGMENT_TAG;

public class SignActivity extends AppCompatActivity implements SignInFragmentListener,
        RegisterFragmentListener, VerifyFragmentListener, LoadingFragmentListener, QRCoderFragmentCallback {

    private QRCodeListener QRListener;
    private FirebaseAuth auth;
    private boolean threadAlive = true;
    private Dialog progressBar;
    private ProgressBar progress;
    private VFragmentListener vFragmentListener;
    private DatabaseReference reference;
    private LogInCompleteCallback callback;
    private CheckView checkView;
    private RelativeLayout container;
    private LinearLayout background;
    private Context mContext;
    private RegisterFragment registerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        container = findViewById(R.id.container);
        openLoginFragment();
        // full screen
        container.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        container.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        background = findViewById(R.id.background_sign);
        background.setVisibility(View.GONE);
        checkConnectionSync();
        mContext = SignActivity.this;
        ////
    }

    @Override
    public void OnSignInButtonClicked(String email, String password) {
        progressBar = new Dialog(this);
        progressBar.setContentView(R.layout.loading_progressbar_circle);
        checkView = progressBar.findViewById(R.id.check_view);
        progress = progressBar.findViewById(R.id.progressbar);
        Objects.requireNonNull(progressBar.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressBar.setCancelable(false);
        logInUser(email, password);
    }

    @Override
    public void OnRegisterButtonClicked() {
        openSignUp();
    }

    @Override
    public void OnForgotPasswordClicked() {
        ForgotPasswordFragment fragment = new ForgotPasswordFragment(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, fragment, FORGOT_PASS_FRAGMENT_TAG)
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .addToBackStack(SIGN_FRAGMENT_TAG)
                .attach(fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    private void openSignUp() {
        openRegisterFragment();
    }

    @Override
    public void OnRegisterButtonClicked(HashMap<String, Object> hashMap, String email, String password) {
        registerUser(hashMap, email, password);
    }

    private void openRegisterFragment() {
        registerFragment = new RegisterFragment(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, registerFragment, REG_FRAGMENT_TAG)
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .addToBackStack(SIGN_FRAGMENT_TAG)
                .attach(registerFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
        QRListener = registerFragment;
    }

    @Override
    public void OnScannerRequest() {
        VerifyFragment verifyFragment = new VerifyFragment(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.frame, verifyFragment, VERIFY_FRAGMENT_TAG)
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .addToBackStack(REG_FRAGMENT_TAG)
                .hide(registerFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
        vFragmentListener = verifyFragment;
    }

    @Override
    public void OnIDScanRequest() {

        //startActivity(new Intent(SignActivity.this, MLKitActivity.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, ID_CAMERA_REQUEST);
        } else {
            BarCodeFragment verifyFragment = new BarCodeFragment(this);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.frame, verifyFragment, QR_CODE_FRAGMENT_TAG)
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                    .addToBackStack(REG_FRAGMENT_TAG)
                    .hide(registerFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }

    }

    private void registerUser(final HashMap<String, Object> hashMap, String email, String password) {
        openLoadingFragment();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String uId = firebaseUser.getUid();
                            reference = FirebaseDatabase.getInstance().getReference("Users").child(uId);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.US);
                            Log.d("Time", simpleDateFormat.format(new Date()));
                            hashMap.put(KEY_LAST_SEEN_PRIVACY, EVERYONE);
                            hashMap.put(KEY_ID, uId);
                            hashMap.put(KEY_STATUS, OFFLINE);
                            hashMap.put(KEY_LAST_SEEN_DATE, simpleDateFormat.format(new Date()));
                            hashMap.put(KEY_PROFILE_PRIVACY, EVERYONE);
                            hashMap.put(KEY_LOCATION_PRIVACY, EVERYONE);
                            hashMap.put(KEY_EMAIL_PRIVACY, EVERYONE);
                            hashMap.put(KEY_PHONE_PRIVACY, EVERYONE);
                            hashMap.put(KEY_BIRTHDAY_PRIVACY, EVERYONE);
                            hashMap.put(KEY_CLASS_NAME, NOT_PROVIDED);
                            hashMap.put(KEY_DISTRICT, NOT_PROVIDED);
                            hashMap.put(KEY_BIO, NOT_PROVIDED);
                            hashMap.put(KEY_ABOUT, "Hey Ilahianz");
                            hashMap.put(KEY_LATITUDE, NOT_PROVIDED);
                            hashMap.put(KEY_LONGITUDE, NOT_PROVIDED);
                            hashMap.put(KEY_THUMBNAIL, DEFAULT);
                            hashMap.put(KEY_NICKNAME, NOT_PROVIDED);
                            hashMap.put(KEY_BLOOD_DONATE, NOT_PROVIDED);
                            simpleDateFormat = new SimpleDateFormat("MMM yyyy", Locale.US);
                            hashMap.put(KEY_JOIN_DATE, simpleDateFormat.format(new Date()));

                            //debug
                            Log.d(KEY_LAST_SEEN_PRIVACY, "Everyone");
                            Log.d(KEY_ID, uId);
                            Log.d(KEY_LAST_SEEN_DATE, uId);
                           Log.d(KEY_PROFILE_PRIVACY, "Everyone");
                           Log.d(KEY_LOCATION_PRIVACY, "Everyone");
                           Log.d(KEY_EMAIL_PRIVACY, "Everyone");
                           Log.d(KEY_PHONE_PRIVACY, "Everyone");
                           Log.d(KEY_BIRTHDAY_PRIVACY, "Everyone");
                           Log.d(KEY_DISTRICT, "Not Provided");
                           Log.d(KEY_BIO, "Not Provided");
                           Log.d(KEY_ABOUT, "Hey Ilahianz");
                           Log.d(KEY_LATITUDE, "Not Provided");
                           Log.d(KEY_LONGITUDE, "Not Provided");
                           Log.d(KEY_THUMBNAIL, DEFAULT);
                           Log.d(KEY_NICKNAME, "Not Provided");
                           Log.d(KEY_BLOOD_DONATE, "false");
                            //
                            Log.d("LOGIN", "Complete");
                            callback.onRegistered();
                            reference.setValue(hashMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isComplete()) {
                                                Log.d("LOGIN PUSH", "Complete");
                                                callback.logInComplete();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    callback.logInIncomplete();
                                    Log.d("LOGIN PUSH", "Failed");
                                }
                            });

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailed();
                Log.d("LOGIN", "Failed");
            }
        });


    }

    private void openLoginFragment() {
        SignFragment signFragment = new SignFragment(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.frame, signFragment, SIGN_FRAGMENT_TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_QR_CODE_RESULT) {
            if (resultCode == RESULT_OK && data != null) {
                String result = data.getStringExtra(QR_CODE_RESULT_KEY);
                if (result != null) {
                    Log.d(QR_CODE_RESULT_KEY, result);
                    QRListener.OnQRCodeResultOK(result);
                    vFragmentListener.onResultOk(result);
                } else {
                    Log.d(QR_CODE_RESULT_KEY, "null");
                    QRListener.OnQRCodeResultOK("null");
                    vFragmentListener.onResultOk("null");
                }
            } else {
                Log.d(QR_CODE_RESULT_KEY, "Cancelled");
                QRListener.OnQRCodeResultCancelled();
                vFragmentListener.onResultCancelled();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == CAMERA_REQUEST) {
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_GRANTED) {
                    {
                        startActivityForResult(new Intent(SignActivity.this, QRCodeActivity.class),
                                REQUEST_QR_CODE_RESULT);
                    }
                }
            }

        }
        if (requestCode == ID_CAMERA_REQUEST) {
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_GRANTED) {
                    {
                        BarCodeFragment verifyFragment = new BarCodeFragment(this);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame, verifyFragment, QR_CODE_FRAGMENT_TAG)
                                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                                .addToBackStack(REG_FRAGMENT_TAG)
                                .attach(verifyFragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .commit();
                    }
                }
            }

        }
    }

    private void logInUser(String email, String password) {
        progressBar.show();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            onCompleteSign();
                        }else{
                            Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.cancel();
            }
        });
    }

    private void checkConnectionSync() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Log.d("Thread", "Started");
                do {
                    while (threadAlive) {
                        if (!Network.Connected(SignActivity.this)) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Snackbar snackbar = Snackbar.make(findViewById(R.id.frame), "Connection Interrupted",
                                            Snackbar.LENGTH_INDEFINITE).setAction("Action", null);
                                    View sbView = snackbar.getView();
                                    sbView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    snackbar.show();
                                }
                            });
                            break;
                        }
                    }
                    while (threadAlive) {
                        if (Network.Connected(SignActivity.this)) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Snackbar snackbar = Snackbar.make(findViewById(R.id.frame), "Back to Online",
                                            Snackbar.LENGTH_SHORT).setAction("Action", null);
                                    View sbView = snackbar.getView();
                                    sbView.setBackgroundColor(getResources().getColor(R.color.dd_green));
                                    sbView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    snackbar.show();
                                }
                            });
                            break;
                        }
                    }
                } while (threadAlive);
                Log.d("Thread", "Stopped");
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        if (((getFragment(LOADING_FRAGMENT_TAG)) != null && getFragment(LOADING_FRAGMENT_TAG).isVisible())) {
            super.onBackPressed();
        } else if ((getFragment(REG_FRAGMENT_TAG) != null && getFragment(REG_FRAGMENT_TAG).isVisible())) {
            super.onBackPressed();
        } else if ((getFragment(VERIFY_FRAGMENT_TAG) != null && getFragment(VERIFY_FRAGMENT_TAG).isVisible())) {
            super.onBackPressed();
        } else if ((getFragment(FORGOT_PASS_FRAGMENT_TAG) != null && getFragment(FORGOT_PASS_FRAGMENT_TAG).isVisible())) {
            super.onBackPressed();
        } else if ((getFragment(QR_CODE_FRAGMENT_TAG) != null && getFragment(QR_CODE_FRAGMENT_TAG).isVisible())) {
            super.onBackPressed();
        } else {
            SignActivity.this.moveTaskToBack(true);
            threadAlive = false;
        }
    }

    @Override
    protected void onResume() {
        threadAlive = true;
        checkConnectionSync();
        super.onResume();
    }

    private void openLoadingFragment() {
        LoadingFragment loadingFragment = new LoadingFragment(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, loadingFragment, LOADING_FRAGMENT_TAG)
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .addToBackStack(SIGN_FRAGMENT_TAG)
                .attach(loadingFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

        callback = loadingFragment;
    }

    private Fragment getFragment(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    @Override
    public void onVerifyButtonClicked() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
        } else {
            startActivityForResult(new Intent(SignActivity.this, QRCodeActivity.class), REQUEST_QR_CODE_RESULT);
        }
    }

    @Override
    public void onCompleteButtonClicked() {
        onBackPressed();
    }

    @Override
    public void onProcessComplete() {
        openLoginFragment();
    }

    @Override
    public void onProcessFailed() {
        onBackPressed();
    }

    @Override
    public void onProcessIncomplete() {
        onBackPressed();
    }

    private void onCompleteSign() {
        Log.d("Sign", "Complete");
        checkView.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        checkView.check();
        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("TICK", String.valueOf(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                final FirebaseUser fUser = auth.getCurrentUser();
                assert fUser != null;
                DatabaseReference reference = FirebaseDatabase.getInstance()
                        .getReference("Users").child(fUser.getUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        progressBar.cancel();
                        int colorFrom = getResources().getColor(R.color.white);
                        int colorTo = getResources().getColor(R.color.blue_dark);
                        Fragment fragment = getFragment(SIGN_FRAGMENT_TAG);
                        background.setVisibility(View.VISIBLE);
                        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                        colorAnimation.setDuration(1000);
                        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                background.setBackgroundColor((int) animation.getAnimatedValue());
                            }
                        });
                        colorAnimation.start();
                        FadingTextView view = new FadingTextView(SignActivity.this);
                        view.setTimeout(2000, TimeUnit.MILLISECONDS);
                        view.setTextColor(getResources().getColor(R.color.white));
                        view.setTextSize(30);
                        Typeface font = ResourcesCompat.getFont(SignActivity.this, R.font.roboto_bold);
                        view.setTypeface(font);

                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        params.addRule(RelativeLayout.CENTER_VERTICAL);
                        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        view.setLayoutParams(params);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .detach(fragment)
                                .commit();
                        UserData data = dataSnapshot.getValue(UserData.class);
                        if (data != null) {
                            setUserInfo(data);
                            String[] array = {"HI", data.getUsername(), "Welcome back"};
                            view.setTexts(array);
                            container.addView(view);

                            new CountDownTimer(4000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                    startActivity(new Intent(SignActivity.this, MainActivity.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                }
                            }.start();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        }.start();
    }

    @Override
    public void onScanComplete(String id) {
        Log.d("RESULT_OK", id);
        onBackPressed();
        QRListener.OnIDQRCodeResultOK(id);
    }

    private void setUserInfo(UserData data) {
        MemorySupports.setUserInfo(mContext, KEY_USERNAME, data.getUsername());
        MemorySupports.setUserInfo(mContext, KEY_PH_NUMBER, data.getPhoneNumber());
        MemorySupports.setUserInfo(mContext, KEY_ID_NUMBER, data.getIdNumber());
        MemorySupports.setUserInfo(mContext, KEY_GENDER, data.getGender());
        MemorySupports.setUserInfo(mContext, KEY_CLASS_NAME, data.getClassName());
        MemorySupports.setUserInfo(mContext, KEY_EMAIL, data.getEmail());
        MemorySupports.setUserInfo(mContext, KEY_BIRTH_DAY, data.getBirthday());
        MemorySupports.setUserInfo(mContext, KEY_BIRTH_YEAR, data.getBirthYear());
        MemorySupports.setUserInfo(mContext, KEY_BIRTH_MONTH, data.getBirthMonth());
        MemorySupports.setUserInfo(mContext, KEY_NICKNAME, data.getNickname());
        MemorySupports.setUserInfo(mContext, KEY_CATEGORY, data.getCategory());
        MemorySupports.setUserInfo(mContext, KEY_ABOUT, data.getDescription());
        MemorySupports.setUserInfo(mContext, KEY_ID, data.getId());
        MemorySupports.setUserInfo(mContext, KEY_LONGITUDE, data.getLongitude());
        MemorySupports.setUserInfo(mContext, KEY_LATITUDE, data.getLatitude());
        MemorySupports.setUserInfo(mContext, KEY_PROFILE_PRIVACY, data.getProfilePrivacy());
        MemorySupports.setUserInfo(mContext, KEY_LOCATION_PRIVACY, data.getLocationPrivacy());
        MemorySupports.setUserInfo(mContext, KEY_EMAIL_PRIVACY, data.getEmailPrivacy());
        MemorySupports.setUserInfo(mContext, KEY_PHONE_PRIVACY, data.getPhonePrivacy());
        MemorySupports.setUserInfo(mContext, KEY_BIRTHDAY_PRIVACY, data.getBirthdayPrivacy());
        MemorySupports.setUserInfo(mContext, KEY_LAST_SEEN_PRIVACY, data.getLastSeenPrivacy());
        MemorySupports.setUserInfo(mContext, KEY_CITY, data.getCity());
        MemorySupports.setUserInfo(mContext, KEY_DISTRICT, data.getDistrict());
        MemorySupports.setUserInfo(mContext, KEY_DEPARTMENT, data.getDepartment());
        MemorySupports.setUserInfo(mContext, KEY_BIO, data.getBio());
        MemorySupports.setUserInfo(mContext, KEY_THUMBNAIL, data.getThumbnailURL());
        MemorySupports.setUserInfo(mContext, KEY_IMAGE_URL, data.getImageURL());
        MemorySupports.setUserInfo(mContext, KEY_BLOOD_DONATE, data.getBloodDonate());
        MemorySupports.setUserInfo(mContext, KEY_JOIN_DATE, data.getJoinDate());
        MemorySupports.setUserInfo(mContext, KEY_ACADEMIC_YEAR_FROM, data.getAcademicYearFrom());
        MemorySupports.setUserInfo(mContext, KEY_ACADEMIC_YEAR_TO, data.getAcademicYearTo());
    }
}

