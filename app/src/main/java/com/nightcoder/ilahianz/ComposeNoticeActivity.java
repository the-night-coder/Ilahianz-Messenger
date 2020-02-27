package com.nightcoder.ilahianz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.nightcoder.ilahianz.Models.Notice;
import com.nightcoder.ilahianz.Supports.MemorySupports;

import java.util.HashMap;

import static com.nightcoder.ilahianz.Literals.IntegerConstants.CAMERA_REQUEST;
import static com.nightcoder.ilahianz.Literals.IntegerConstants.IMAGE_REQUEST;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;
import static com.nightcoder.ilahianz.Models.Notice.TYPE_TEXT;

public class ComposeNoticeActivity extends AppCompatActivity {

    private RadioGroup options;
    private EditText content, subject;
    private Button composeButton;
    private ImageButton audioOption, imageOption, docOption, cameraOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_notice);
        init();

        composeButton.setOnClickListener(clickListener);
        audioOption.setOnClickListener(clickListener);
        imageOption.setOnClickListener(clickListener);
        docOption.setOnClickListener(clickListener);
        cameraOption.setOnClickListener(clickListener);


    }

    private void init() {
        options = findViewById(R.id.notice_option);
        composeButton = findViewById(R.id.compose_btn);
        content = findViewById(R.id.content);
        subject = findViewById(R.id.subject);
        audioOption = findViewById(R.id.audio_option);
        cameraOption = findViewById(R.id.camera_option);
        imageOption = findViewById(R.id.image_option);
        docOption = findViewById(R.id.doc_option);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.compose_btn:
                    composeNotice();
                    break;
                case R.id.audio_option:
                    recordAudio();
                    break;
                case R.id.image_option:
                    openGallery();
                    break;
                case R.id.doc_option:
                    openDocument();
                    break;
                case R.id.camera_option:
                    openCamera();
                    break;

            }
        }
    };

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

    private void recordAudio() {

    }

    private void openGallery() {
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

    private void openDocument() {

    }

    private void composeNotice() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notice");
        String key = reference.push().getKey();
        if (!content.getText().toString().trim().isEmpty()) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put(Notice.KEY_COMPOSER_ID, MemorySupports.getUserInfo(this, KEY_ID));
            hashMap.put(Notice.KEY_CONTENT_PATH, "null");
            hashMap.put(Notice.KEY_CONTENT_TYPE, TYPE_TEXT);
            hashMap.put(Notice.KEY_SUBJECT, subject.getText().toString());
            hashMap.put(Notice.KEY_TEXT, content.getText().toString());
            hashMap.put(Notice.KEY_TARGET, "All");
            hashMap.put(Notice.KEY_TIMESTAMP, ServerValue.TIMESTAMP);
            hashMap.put(Notice.KEY_ID, key);

            assert key != null;
            reference.child(key).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d("Notice", "Sent");
                }
            });

            content.setText("");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
