package com.nightcoder.ilahianz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ContentFrameLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nightcoder.ilahianz.Fragments.RegisterFragment;
import com.nightcoder.ilahianz.Fragments.SignFragment;
import com.nightcoder.ilahianz.Listeners.RegisterFragmentListener;
import com.nightcoder.ilahianz.Listeners.SignInFragmentListener;

import java.util.HashMap;

import static com.nightcoder.ilahianz.Literals.StringConstants.REG_FRAGMENT_TAG;
import static com.nightcoder.ilahianz.Literals.StringConstants.SIGN_FRAGMENT_TAG;

public class SignActivity extends AppCompatActivity implements SignInFragmentListener,
        RegisterFragmentListener {

    RegisterFragment registerFragment;
    SignFragment signFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        LinearLayout container = findViewById(R.id.container);

        signFragment = new SignFragment(this);
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
        ////
    }

    @Override
    public void OnSignInButtonClicked(String email, String password) {
        Toast.makeText(this, "Log In", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnRegisterButtonClicked() {
        openSignUp();
    }

    private void openSignUp() {
        registerFragment = new RegisterFragment(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, registerFragment, REG_FRAGMENT_TAG)
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .addToBackStack(SIGN_FRAGMENT_TAG)
                .attach(registerFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    public void OnRegisterButtonClicked(HashMap userDetails) {
        Toast.makeText(this, "Registered", Toast.LENGTH_SHORT).show();
    }
}
