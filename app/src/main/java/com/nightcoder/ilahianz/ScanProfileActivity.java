package com.nightcoder.ilahianz;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.zxing.BarcodeFormat;
import com.nightcoder.ilahianz.Fragments.ProfileQRCodeFragment;
import com.nightcoder.ilahianz.Fragments.ScanProfileFragment;
import com.nightcoder.ilahianz.Listeners.ProfileQRCodeScanCallback;
import com.nightcoder.ilahianz.Listeners.ProfileScanButtonCallback;

import static com.nightcoder.ilahianz.Literals.IntegerConstats.CAMERA_REQUEST;
import static com.nightcoder.ilahianz.Literals.StringConstants.PROFILE_QR_CODE_FRAGMENT_KEY;
import static com.nightcoder.ilahianz.Literals.StringConstants.PROFILE_QR_CODE_SCAN_FRAGMENT_KEY;

public class ScanProfileActivity extends AppCompatActivity implements ProfileQRCodeScanCallback, ProfileScanButtonCallback {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_profile);
        mContext = ScanProfileActivity.this;
        Fragment fragment = new ProfileQRCodeFragment(mContext);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, fragment, PROFILE_QR_CODE_FRAGMENT_KEY)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    public void onResultOK(String result, BarcodeFormat barcodeFormat) {
        Log.d("QR_RESULT", result);
        Log.d("FORMAT", String.valueOf(barcodeFormat));
        if (BarcodeFormat.QR_CODE == barcodeFormat) {
            searchById();
        } else if (BarcodeFormat.ITF == barcodeFormat) {
            searchByIdCard();
        }
        onBackPressed();
    }

    private void searchByIdCard() {

    }

    private void searchById() {

    }

    @Override
    public void onCameraCancelled() {

    }

    private void openQRCodeScanner() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
        } else {
            Fragment fragment = new ScanProfileFragment(mContext);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame, fragment, PROFILE_QR_CODE_SCAN_FRAGMENT_KEY)
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .addToBackStack(PROFILE_QR_CODE_FRAGMENT_KEY)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_REQUEST) {
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_GRANTED) {
                    Fragment fragment = new ScanProfileFragment(mContext);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame, fragment, PROFILE_QR_CODE_SCAN_FRAGMENT_KEY)
                            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                            .addToBackStack(PROFILE_QR_CODE_FRAGMENT_KEY)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();
                }
            }
        }
    }

    @Override
    public void onScanButtonClicked() {
        openQRCodeScanner();
    }

    @Override
    public void onCloseButtonClicked() {
        finish();
    }
}

