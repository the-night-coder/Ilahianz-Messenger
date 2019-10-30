package com.nightcoder.ilahianz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.Collections;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.nightcoder.ilahianz.Literals.StringConstants.QR_CODE_RESULT_KEY;

public class QRCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new ZXingScannerView(this);
        List<BarcodeFormat> formats = Collections.singletonList(BarcodeFormat.QR_CODE);
        view.setFormats(formats);
        setContentView(view);

    }

    @Override
    protected void onResume() {
        super.onResume();
        view.setResultHandler(this);
        view.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        view.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        Intent intent = new Intent();
        intent.putExtra(QR_CODE_RESULT_KEY, result.getText());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
