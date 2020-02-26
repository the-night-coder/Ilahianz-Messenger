package com.nightcoder.ilahianz;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.nightcoder.ilahianz.Models.Notice;
import com.nightcoder.ilahianz.Supports.MemorySupports;

import java.util.HashMap;

import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;
import static com.nightcoder.ilahianz.Models.Notice.TYPE_TEXT;

public class ComposeNoticeActivity extends AppCompatActivity {

    private RadioGroup options;
    private EditText content, subject;
    private Button composeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_notice);
        init();

        composeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                composeNotice();
            }
        });

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

    private void init() {
        options = findViewById(R.id.notice_option);
        composeButton = findViewById(R.id.compose_btn);
        content = findViewById(R.id.content);
        subject = findViewById(R.id.subject);
    }
}
