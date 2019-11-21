package com.nightcoder.ilahianz;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nightcoder.ilahianz.Supports.Graphics;
import com.tomer.fadingtextview.FadingTextView;

public class StartActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button skip;
    Handler handler = new Handler();
    FirebaseUser firebaseUser;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(StartActivity.this, StartupActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    };

    Runnable intentMainActivity = new Runnable() {
        @Override
        public void run() {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StartActivity.this,
                    new Pair<View, String>(imageView, "imageTransition"));
            startActivity(new Intent(StartActivity.this, MainActivity.class), options.toBundle());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        imageView = findViewById(R.id.imageView);
        skip = findViewById(R.id.skip_action);
        FadingTextView fadingTextView = findViewById(R.id.fad_text);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            String[] array = {"ILAHIANZ"};
            fadingTextView.setTexts(array);
            handler.postDelayed(intentMainActivity, 1000);
        } else {
            skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.removeCallbacks(runnable);
                    handler.post(runnable);
                }
            });
            Graphics.setGifImage(this, R.raw.logo_gif, imageView);
            handler.postDelayed(runnable, 12000);
        }

    }
}
