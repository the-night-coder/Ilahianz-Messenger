package com.nightcoder.ilahianz.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.nightcoder.ilahianz.Listeners.FragmentListeners.LoadingFragmentListener;
import com.nightcoder.ilahianz.Listeners.LogInCompleteCallback;
import com.nightcoder.ilahianz.R;

public class LoadingFragment extends Fragment implements LogInCompleteCallback {

    private Context mContext;
    private ImageView imageView;
    private LoadingFragmentListener listener;
    private TextView textView;

    public LoadingFragment(Context context) {
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loading, container, false);
        imageView = view.findViewById(R.id.gif);
        textView = view.findViewById(R.id.text);
        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Glide.with(mContext).load(R.raw.giphy).asGif().into(imageView);
            }
        }.start();

        return view;
    }

    @Override
    public void onRegistered() {
    }

    @Override
    public void onFailed() {
        listener.onProcessFailed();
    }

    @Override
    public void logInComplete() {
        listener.onProcessIncomplete();
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
