package com.nightcoder.ilahianz.Listeners;

public interface LogInCompleteCallback {
    void onRegistered();
    void onFailed();
    void logInComplete();
    void logInIncomplete();
}

