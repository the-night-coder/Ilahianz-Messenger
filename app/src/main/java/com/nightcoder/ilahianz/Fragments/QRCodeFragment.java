package com.nightcoder.ilahianz.Fragments;

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

import java.util.Collections;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRCodeFragment extends Fragment implements ZXingScannerView.ResultHandler {

    private Context mContext;
    private ZXingScannerView zXingScannerView;
    private QRCoderFragmentCallback listener;

    public QRCodeFragment(Context context) {
        this.mContext = context;
        zXingScannerView = new ZXingScannerView(context);
    }

    public QRCodeFragment() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        List<BarcodeFormat> formats = Collections.singletonList(BarcodeFormat.QR_CODE);
        zXingScannerView.setFormats(formats);
        return zXingScannerView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
        Activity activity = (Activity) context;
        listener = (QRCoderFragmentCallback) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        zXingScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        listener.onScanComplete(result.getText());
        Log.d("Result", result.getText());
    }
}
