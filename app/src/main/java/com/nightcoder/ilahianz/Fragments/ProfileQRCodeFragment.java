package com.nightcoder.ilahianz.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.nightcoder.ilahianz.Listeners.ProfileScanButtonCallback;
import com.nightcoder.ilahianz.R;
import com.nightcoder.ilahianz.Supports.ViewSupports;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_USERNAME;
import static com.nightcoder.ilahianz.Literals.StringConstants.USER_INFO_SP;

public class ProfileQRCodeFragment extends Fragment {

    private Context mContext;
    private ProfileScanButtonCallback callback;
    private ImageView qrCode;
    private ViewGroup root;
    private TextView name;
    private LinearLayout scanButton;
    private CircleImageView profileImage;

    public ProfileQRCodeFragment(Context context) {
        this.mContext = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_qr_code, container, false);
        root = container;
        qrCode = view.findViewById(R.id.qr_code);
        scanButton = view.findViewById(R.id.scan_button);
        name = view.findViewById(R.id.username);
        profileImage = view.findViewById(R.id.profile_photo);
        ImageButton closeButton = view.findViewById(R.id.close_btn);
        name.setText(getUserInfo(KEY_USERNAME));
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix qrCodeMap = writer.encode(getUserInfo(KEY_ID), BarcodeFormat.QR_CODE, 350, 350);
            int height = qrCodeMap.getHeight();
            int width = qrCodeMap.getWidth();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, qrCodeMap.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            qrCode.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }

        qrCode.setVisibility(View.GONE);
        name.setVisibility(View.GONE);
        scanButton.setVisibility(View.GONE);
        profileImage.setVisibility(View.GONE);
        //Animation
        new CountDownTimer(300, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                ViewSupports.visibilitySlideAnimation(Gravity.BOTTOM, 500, scanButton, root, View.VISIBLE);
            }

        }.start();
        new CountDownTimer(400, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                ViewSupports.visibilitySlideAnimation(Gravity.TOP, 500, profileImage, root, View.VISIBLE);
            }

        }.start();
        new CountDownTimer(600, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                ViewSupports.visibilityFadeAnimation(600, qrCode, root, View.VISIBLE);
            }
        }.start();
        new CountDownTimer(1200, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                ViewSupports.visibilityFadeAnimation(500, name, root, View.VISIBLE);
            }
        }.start();
        //
        scanButton.setOnClickListener(clickListener);
        closeButton.setOnClickListener(clickListener);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        callback = (ProfileScanButtonCallback) activity;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.close_btn:
                    callback.onCloseButtonClicked();
                    break;
                case R.id.scan_button:
                    callback.onScanButtonClicked();
                    break;
            }
        }
    };

    private String getUserInfo(String key) {
        SharedPreferences preferences = mContext.getSharedPreferences(USER_INFO_SP, MODE_PRIVATE);
        return preferences.getString(key, "none");
    }

    @Override
    public void onDestroy() {
        ViewSupports.visibilitySlideAnimation(Gravity.TOP, 500, profileImage, root, View.GONE);
        super.onDestroy();
    }
}