package com.nightcoder.ilahianz.ChatUI.Fragments.BloodDonationFragments;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nightcoder.ilahianz.Listeners.BloodDonation.BloodDonationActivityCallbacks;
import com.nightcoder.ilahianz.R;
import com.nightcoder.ilahianz.Supports.ViewSupports;

import cdflynn.android.library.checkview.CheckView;

public class LoadingFragment extends Fragment {

    private BloodDonationActivityCallbacks callbacks;
    private RelativeLayout layoutContainer;
    private Handler handler = new Handler(Looper.getMainLooper());
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.loading_blood_donation_progress, container, false);
        CheckView checkView = root.findViewById(R.id.check_view);
        checkView.setVisibility(View.GONE);
        layoutContainer = root.findViewById(R.id.container);
        ViewSupports.visibilityFadeAnimation(600, checkView, (ViewGroup) root, View.VISIBLE);
        checkView.check();
        int colorFrom = getResources().getColor(R.color.blood_donation_form_bg);
        int colorTo = getResources().getColor(R.color.white);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(1500);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                layoutContainer.setBackgroundColor((int) animation.getAnimatedValue());
            }
        });
        colorAnimation.start();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callbacks.onProcessLoadingComplete();
            }
        }, 2000);
        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        callbacks = (BloodDonationActivityCallbacks) activity;
    }
}
