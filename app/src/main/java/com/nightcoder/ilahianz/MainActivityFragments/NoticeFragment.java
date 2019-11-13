package com.nightcoder.ilahianz.MainActivityFragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nightcoder.ilahianz.R;
import com.nightcoder.ilahianz.Supports.ViewSupports;

public class NoticeFragment extends Fragment {
    private Button newButton;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notice, container, false);
        newButton = view.findViewById(R.id.new_btn);
        newButton.setVisibility(View.GONE);
        new CountDownTimer(1000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                ViewSupports.visibilitySlideAnimation(Gravity.BOTTOM, 600, newButton,
                        (ViewGroup) view.getRootView(), View.VISIBLE);
            }
        }.start();
        return view;
    }
}
