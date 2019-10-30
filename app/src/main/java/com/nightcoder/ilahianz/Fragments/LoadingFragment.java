package com.nightcoder.ilahianz.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.nightcoder.ilahianz.Listeners.FragmentListeners.LoadingFragmentListener;
import com.nightcoder.ilahianz.Listeners.LogInCompleteCallback;
import com.nightcoder.ilahianz.R;
import com.nightcoder.ilahianz.Supports.Graphics;
import com.tomer.fadingtextview.FadingTextView;

import java.util.concurrent.TimeUnit;

public class LoadingFragment extends Fragment implements LogInCompleteCallback {

    private Context mContext;
    private LoadingFragmentListener listener;
    private FadingTextView textView;

    public LoadingFragment(Context context) {
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loading, container, false);
        ImageView imageView = view.findViewById(R.id.gif);
        textView = view.findViewById(R.id.text);
        String[] array = {
                "We just passing your information to our Witch!",
                "She will carry your information in our cloud"};
        textView.setTexts(array);
        textView.setTimeout(1500, TimeUnit.MILLISECONDS);
        Graphics.setGifImage(mContext, R.raw.giphy, imageView);

        return view;
    }

    @Override
    public void onRegistered() {

    }

    @Override
    public void onFailed() {
        String[] array = {
                "Witch can't carry your information at this moment",
                "We are sorry for the interruption",
                "Try again later"};
        textView.setTexts(array);
        textView.setTimeout(2000, TimeUnit.MILLISECONDS);
        new CountDownTimer(6000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                listener.onProcessFailed();
            }
        }.start();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void logInComplete() {
        String[] array = {
                "Great",
                "You are all Set",
                "Fall back to log in.."};
        textView.setTexts(array);
        textView.setTimeout(2000, FadingTextView.MILLISECONDS);
        new CountDownTimer(6000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                listener.onProcessComplete();
            }
        }.start();
    }

    @Override
    public void logInIncomplete() {
        listener.onProcessIncomplete();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        listener = (LoadingFragmentListener) activity;
    }
}
