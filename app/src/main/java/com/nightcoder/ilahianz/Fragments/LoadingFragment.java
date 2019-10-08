package com.nightcoder.ilahianz.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.nightcoder.ilahianz.R;

public class LoadingFragment extends Fragment {

    private Context mContext;
    private ImageView imageView;

    public LoadingFragment(Context context) {
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loading, container, false);
        imageView = view.findViewById(R.id.gif);
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
}
