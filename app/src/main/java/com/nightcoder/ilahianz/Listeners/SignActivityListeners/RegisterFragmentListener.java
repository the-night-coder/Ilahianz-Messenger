package com.nightcoder.ilahianz.Listeners.SignActivityListeners;

import java.util.HashMap;

public interface RegisterFragmentListener {
    void OnRegisterButtonClicked(HashMap<String, Object> userDetails, String email, String password);

    void OnScannerRequest();

    void OnIDScanRequest();
}
