package com.nightcoder.ilahianz.Listeners.SignActivityListeners;

public interface LogInCompleteCallback {
    void onRegistered();
    void onFailed();
    void logInComplete();
    void logInIncomplete();
}

