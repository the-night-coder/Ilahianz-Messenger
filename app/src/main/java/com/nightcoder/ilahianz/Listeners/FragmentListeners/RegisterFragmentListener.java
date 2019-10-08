package com.nightcoder.ilahianz.Listeners.FragmentListeners;

import java.util.HashMap;

public interface RegisterFragmentListener {
    void OnRegisterButtonClicked(HashMap<String, Object> userDetails, String email, String password);

    void OnScannerRequest();
}
