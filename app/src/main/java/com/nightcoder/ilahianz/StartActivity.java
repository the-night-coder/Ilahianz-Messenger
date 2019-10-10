package com.nightcoder.ilahianz;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.nightcoder.ilahianz.Supports.Graphics;

public class StartActivity extends AppCompatActivity {

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(StartActivity.this, StartupActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ImageView imageView = findViewById(R.id.imageView);

        Graphics.setGifImage(this, R.raw.icon_fffff, imageView);

        handler.postDelayed(runnable, 13000);

    }
}
