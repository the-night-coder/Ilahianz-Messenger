package com.nightcoder.ilahianz.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.zxing.Result;
import com.nightcoder.ilahianz.Listeners.QRCodeListener;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRCodeFragment extends Fragment implements ZXingScannerView.ResultHandler {

    private Context mContext;
    private ZXingScannerView zXingScannerView;
    private QRCodeListener listener;

    public QRCodeFragment(Context context) {
        this.mContext = context;
        zXingScannerView = new ZXingScannerView(context);
    }

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
        listener = (QRCodeListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        zXingScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        listener.OnQRCodeResultOK(result.getText());
        Log.d("Result", result.getText());
    }
}
