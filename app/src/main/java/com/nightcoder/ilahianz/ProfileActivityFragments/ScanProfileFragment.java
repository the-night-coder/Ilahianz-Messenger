package com.nightcoder.ilahianz.ProfileActivityFragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.nightcoder.ilahianz.Listeners.FragmentListeners.QRCoderFragmentCallback;
import com.nightcoder.ilahianz.Listeners.ProfileQRCodeScanCallback;

import java.util.Collections;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanProfileFragment extends Fragment implements ZXingScannerView.ResultHandler {

    private Context mContext;
    private ZXingScannerView zXingScannerView;
    private ProfileQRCodeScanCallback listener;

    public ScanProfileFragment(Context context) {
        this.mContext = context;
        zXingScannerView = new ZXingScannerView(context);
    }

    public ScanProfileFragment() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return zXingScannerView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
        Activity activity = (Activity) context;
        listener = (ProfileQRCodeScanCallback) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        zXingScannerView.stopCamera();
        listener.onCameraCancelled();
    }

    @Override
    public void handleResult(Result result) {
        listener.onResultOK(result.getText(), result.getBarcodeFormat());
        Log.d("Result", result.getText());
    }
}
