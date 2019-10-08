package com.nightcoder.ilahianz;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.nightcoder.ilahianz.Fragments.RegisterFragment;
import com.nightcoder.ilahianz.Fragments.SignFragment;
import com.nightcoder.ilahianz.Listeners.QRCodeListener;
import com.nightcoder.ilahianz.Listeners.RegisterFragmentListener;
import com.nightcoder.ilahianz.Listeners.SignInFragmentListener;
import com.nightcoder.ilahianz.Supports.Network;

import java.util.HashMap;
import java.util.Objects;

import static com.nightcoder.ilahianz.Literals.IntegerConstats.CAMERA_REQUEST;
import static com.nightcoder.ilahianz.Literals.IntegerConstats.REQUEST_QR_CODE_RESULT;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ABOUT;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BIO;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BIRTHDAY_PRIVACY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_DISTRICT;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_EMAIL_PRIVACY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_LAST_SEEN;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_LATITUDE;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_LOCATION_PRIVACY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_LONGITUDE;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_PHONE_PRIVACY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_PROFILE_PRIVACY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_THUMBNAIL;
import static com.nightcoder.ilahianz.Literals.StringConstants.QR_CODE_RESULT_KEY;
import static com.nightcoder.ilahianz.Literals.StringConstants.REG_FRAGMENT_TAG;
import static com.nightcoder.ilahianz.Literals.StringConstants.SIGN_FRAGMENT_TAG;

public class SignActivity extends AppCompatActivity implements SignInFragmentListener,
        RegisterFragmentListener {

    private Dialog dialog;
    private QRCodeListener QRListener;
    private FirebaseAuth auth;
    private ActivityListener aListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        LinearLayout container = findViewById(R.id.container);
        SignFragment signFragment = new SignFragment(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, signFragment, SIGN_FRAGMENT_TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
        // full screen
        container.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        container.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        auth = FirebaseAuth.getInstance();
        ////
    }

    @Override
    public void OnSignInButtonClicked(String email, String password) {
        logInUser(email, password);
    }

    @Override
    public void OnRegisterButtonClicked() {
        openSignUp();
    }

    private void openSignUp() {
        RegisterFragment registerFragment = new RegisterFragment(this);
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
    public void OnRegisterButtonClicked(HashMap hashMap) {
        Toast.makeText(this, "Registered", Toast.LENGTH_SHORT).show();
        registerUser(hashMap);
    }

    @Override
    public void OnScannerRequest() {
        startActivityForResult(new Intent(SignActivity.this, QRCodeActivity.class), REQUEST_QR_CODE_RESULT);
    }

    private void openDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.verification_dialog);

        Button verify = dialog.findViewById(R.id.btn_verify);

        ///
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void registerUser(HashMap hashMap) {

        hashMap.put(KEY_LAST_SEEN, "Everyone");
        hashMap.put(KEY_PROFILE_PRIVACY, "Everyone");
        hashMap.put(KEY_LOCATION_PRIVACY, "Everyone");
        hashMap.put(KEY_EMAIL_PRIVACY, "Everyone");
        hashMap.put(KEY_PHONE_PRIVACY, "Everyone");
        hashMap.put(KEY_BIRTHDAY_PRIVACY, "Everyone");
        hashMap.put(KEY_DISTRICT, "Not Provided");
        hashMap.put(KEY_BIO, "Not Provided");
        hashMap.put(KEY_ABOUT, "Hey Ilahianz");
        hashMap.put(KEY_LATITUDE, "Not Provided");
        hashMap.put(KEY_LONGITUDE, "Not Provided");
        hashMap.put(KEY_THUMBNAIL, "default");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_QR_CODE_RESULT: {
                if (resultCode == RESULT_OK && data != null) {
                    String result = data.getStringExtra(QR_CODE_RESULT_KEY);
                    if (result != null) {
                        Log.d(QR_CODE_RESULT_KEY, result);
                        QRListener.OnQRCodeResultOK(result);
                    } else {
                        Log.d(QR_CODE_RESULT_KEY, "null");
                        QRListener.OnQRCodeResultOK("null");
                    }
                } else {
                    Log.d(QR_CODE_RESULT_KEY, "Cancelled");
                    QRListener.OnQRCodeResultCancelled();
                }
            }

            case CAMERA_REQUEST: {

            }
        }

    }

    private void logInUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isComplete()) {
                            Log.d("Sign", "Complete");
                        }
                    }
                });
    }

    private void checkConnectioSync() {
        aListener = (ActivityListener) SignActivity.this;
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {
                    if (!Network.Connected(SignActivity.this)) {
                        aListener.OnConnectionInterrupted();
                        break;
                    }
                }
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        SignActivity.this.moveTaskToBack(true);
    }
}

interface ActivityListener {
    void OnConnectionInterrupted();
}
