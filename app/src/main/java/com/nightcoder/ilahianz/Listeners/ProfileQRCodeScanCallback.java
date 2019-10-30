package com.nightcoder.ilahianz.Listeners;

import com.google.zxing.BarcodeFormat;

public interface ProfileQRCodeScanCallback {
    void onResultOK(String result, BarcodeFormat barcodeFormat);
    void onCameraCancelled();
}
