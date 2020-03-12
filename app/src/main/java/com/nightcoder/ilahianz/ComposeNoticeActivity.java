package com.nightcoder.ilahianz;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.nightcoder.ilahianz.Models.Notice;
import com.nightcoder.ilahianz.Supports.MemorySupports;
import com.nightcoder.ilahianz.Supports.ViewSupports;
import com.nightcoder.ilahianz.Utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.nightcoder.ilahianz.Literals.IntegerConstants.CAMERA_REQUEST;
import static com.nightcoder.ilahianz.Literals.IntegerConstants.IMAGE_REQUEST;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;
import static com.nightcoder.ilahianz.Models.Notice.TYPE_IMAGE;
import static com.nightcoder.ilahianz.Models.Notice.TYPE_TEXT;

public class ComposeNoticeActivity extends AppCompatActivity {

    private RadioGroup options;
    private EditText content, subject;
    private Button composeButton;
    private ImageButton audioOption, imageOption, docOption, cameraOption;
    private Context mContext;
    private RadioButton department;
    private Handler handler = new Handler();
    private TextView attachFileName;
    private ImageView attachImage;
    private TextView attachFileSize;
    private ImageButton attachCloseBtn;
    private ConstraintLayout attachContainer;
    private ImageView attachImageFile;


    private String target = null;
    private int attachType = 0;
    private Uri attachUri = null;
    private File attachFile = null;

    private static int DOC_REQUEST = 678;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_notice);
        mContext = ComposeNoticeActivity.this;
        init();

        handler.postDelayed(initAnimation, 200);

        composeButton.setOnClickListener(clickListener);
        audioOption.setOnClickListener(clickListener);
        imageOption.setOnClickListener(clickListener);
        docOption.setOnClickListener(clickListener);
        cameraOption.setOnClickListener(clickListener);
        attachContainer.setOnClickListener(clickListener);

        options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.btn_all) {
                    target = ((RadioButton) findViewById(checkedId)).getText().toString();
                    Log.d("TARGET", ((RadioButton) findViewById(checkedId)).getText().toString());
                } else if (checkedId == R.id.btn_target) {
                    openDepartmentDialog();
                }
            }
        });
    }

    private void openDepartmentDialog() {
        final Dialog dialog = ViewSupports.materialDialog(mContext, R.layout.departments_list_dialog);
        Button cancel = dialog.findViewById(R.id.cancel_action);
        final ListView listView = dialog.findViewById(R.id.list);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("BCA");
        arrayList.add("BBA");
        arrayList.add("B.COM");
        arrayList.add("BA");
        arrayList.add("MCA");
        arrayList.add("Electronics");
        arrayList.add("Economics");
        arrayList.add("Maths");
        arrayList.add("Malayalam");
        arrayList.add("English");
        ArrayAdapter adapter = new ArrayAdapter<>(mContext, R.layout.list_item, arrayList);
        listView.setAdapter(adapter);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RadioButton) findViewById(R.id.btn_all)).setChecked(true);
                dialog.cancel();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) listView.getAdapter().getItem(position);
                department.setText(item);
                target = item;
                Log.d("TARGET", item);
                dialog.cancel();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    Runnable initAnimation = new Runnable() {
        @Override
        public void run() {
            audioOption.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_enter_animation));
            imageOption.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_enter_animation));
            docOption.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_enter_animation));
            cameraOption.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_enter_animation));
            attachContainer.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.attach_animation));
            audioOption.setVisibility(View.VISIBLE);
            cameraOption.setVisibility(View.VISIBLE);
            imageOption.setVisibility(View.VISIBLE);
            docOption.setVisibility(View.VISIBLE);
            attachContainer.setVisibility(View.VISIBLE);

        }
    };

    private void init() {
        options = findViewById(R.id.notice_option);
        composeButton = findViewById(R.id.compose_btn);
        content = findViewById(R.id.content);
        subject = findViewById(R.id.subject);
        audioOption = findViewById(R.id.audio_option);
        cameraOption = findViewById(R.id.camera_option);
        imageOption = findViewById(R.id.image_option);
        docOption = findViewById(R.id.doc_option);
        audioOption.setVisibility(View.INVISIBLE);
        cameraOption.setVisibility(View.INVISIBLE);
        imageOption.setVisibility(View.INVISIBLE);
        docOption.setVisibility(View.INVISIBLE);
        department = findViewById(R.id.btn_target);
        attachFileName = findViewById(R.id.attach_file_name);
        attachFileSize = findViewById(R.id.attach_size);
        attachImage = findViewById(R.id.attach_image);
        attachCloseBtn = findViewById(R.id.attach_file_close_btn);
        attachContainer = findViewById(R.id.attach_container);
        attachContainer.setVisibility(View.INVISIBLE);
        attachImageFile = findViewById(R.id.attach_image_file);
        attachImageFile.setVisibility(View.GONE);
        attachCloseBtn.setOnClickListener(clickListener);
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
                case R.id.attach_container:
                    if (attachUri != null)
                        openAttach(attachType, attachUri);
                    break;
                case R.id.attach_file_close_btn:
                    removeAttach();
                    break;

            }
        }
    };

    private void openAttach(int type, Uri file) {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        intent.setDataAndType(Uri.fromFile(attachFile), "application/pdf");
//
//        try {
//            startActivity(intent);
//        } catch (ActivityNotFoundException e) {
//            Toast.makeText(mContext, "ClassNotFound", Toast.LENGTH_SHORT).show();
//        }
    }

    private boolean checkPermission(String permission) {
        return (ContextCompat.checkSelfPermission(mContext, permission)
                == PackageManager.PERMISSION_GRANTED);

    }

    private void askPermission(int requestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
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
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, DOC_REQUEST);
            } else ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, DOC_REQUEST);
        } else {
            Intent intent = new Intent();
            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, DOC_REQUEST);
        }
    }

    private void setAttach(Uri uri) {
        FileUtils.FileDetail fileDetail = FileUtils.getFileDetailFromUri(mContext, uri);
        attachFileName.setText(fileDetail.fileName);
        attachFileSize.setText(FileUtils.getFileSize(fileDetail.fileSize));
        if (attachType == TYPE_IMAGE) {
            //compressImage(attachFile);
            //attachImageFile.setImageURI(uri);
            attachImageFile.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_enter_animation));
            attachImageFile.setVisibility(View.VISIBLE);
            attachImage.setImageDrawable(getResources().getDrawable(R.mipmap.photo));
        }
    }

    private void removeAttach() {
        if (attachType == TYPE_IMAGE) {
            attachImageFile.setVisibility(View.GONE);
            attachImageFile.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.error_dialog_exit_animation));
        }
        attachType = TYPE_TEXT;
        attachFileSize.setText("---");
        attachFileName.setText(getResources().getString(R.string.attachment_not_found));
        attachImage.setImageDrawable(getResources().getDrawable(R.mipmap.attach));
    }

    private void composeNotice() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notice");
        String key = reference.push().getKey();

        if (content.getText().toString().trim().isEmpty()) {
            Toast.makeText(mContext, "Empty Message", Toast.LENGTH_SHORT).show();
        } else if (target == null) {
            Toast.makeText(mContext, "Select Target", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put(Notice.KEY_COMPOSER_ID, MemorySupports.getUserInfo(this, KEY_ID));
            hashMap.put(Notice.KEY_CONTENT_PATH, "null");
            hashMap.put(Notice.KEY_CONTENT_TYPE, TYPE_TEXT);
            hashMap.put(Notice.KEY_SUBJECT, subject.getText().toString());
            hashMap.put(Notice.KEY_TEXT, content.getText().toString());
            hashMap.put(Notice.KEY_TARGET, target);
            hashMap.put(Notice.KEY_TIMESTAMP, ServerValue.TIMESTAMP);
            hashMap.put(Notice.KEY_ID, key);

            assert key != null;
            reference.child(key).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d("Notice", "Sent");
                    onBackPressed();
                }
            });

            content.setText("");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Uri uri = data.getData();
                attachType = TYPE_IMAGE;
                assert uri != null;
                removeAttach();
                setAttach(uri);
                attachUri = uri;
                //attachFile = new File(uri.toString());

                try {
                    attachFile = FileUtils.from(mContext, uri);
                    compressImage(attachFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                askPermission(IMAGE_REQUEST);
            }

        } else if (requestCode == DOC_REQUEST && resultCode == RESULT_OK && data != null) {
            removeAttach();
            setAttach(data.getData());
            attachUri = data.getData();
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            attachType = TYPE_IMAGE;
            assert uri != null;
            removeAttach();
            attachUri = uri;
            try {
                attachFile = FileUtils.from(mContext, uri);
                compressImage(attachFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void compressImage(File actualImage) {
        if (actualImage != null) {
            final ProgressDialog pd = new ProgressDialog(mContext);
            pd.setMessage("Compressing...");
            pd.show();
            // Compress image in main thread
            //compressedImage = new Compressor(this).compressToFile(actualImage);
            //setCompressedImage();

            // Compress image to bitmap in main thread
            //compressedImageView.setImageBitmap(new Compressor(this).compressToBitmap(actualImage));

            // Compress image using RxJava in background thread
//            new Compressor(this)
//                    .compressToFileAsFlowable(actualImage)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<File>() {
//                        @Override
//                        public void accept(File file) {
//                            attachImageFile.setImageURI(Uri.fromFile(file));
//                            setAttach(Uri.fromFile(file));
//                            pd.dismiss();
//                        }
//                    }, new Consumer<Throwable>() {
//                        @Override
//                        public void accept(Throwable throwable) {
//                            throwable.printStackTrace();
//                            pd.dismiss();
//                        }
//                    });
            // Compress image using RxJava in background thread with custom Compressor
            //noinspection ResultOfMethodCallIgnored
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
                            attachImageFile.setImageURI(Uri.fromFile(file));
                            setAttach(Uri.fromFile(file));
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
}
