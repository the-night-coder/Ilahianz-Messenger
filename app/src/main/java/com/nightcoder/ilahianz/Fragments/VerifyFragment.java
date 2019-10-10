package com.nightcoder.ilahianz.Fragments;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.nightcoder.ilahianz.Listeners.FragmentListeners.VFragmentListener;
import com.nightcoder.ilahianz.Listeners.FragmentListeners.VerifyFragmentListener;
import com.nightcoder.ilahianz.R;
import com.nightcoder.ilahianz.Supports.Graphics;
import com.tomer.fadingtextview.FadingTextView;

import cdflynn.android.library.checkview.CheckView;

import static com.nightcoder.ilahianz.Literals.IntegerConstats.WIDTH_INDEX;


@SuppressWarnings("ALL")
public class VerifyFragment extends Fragment implements VFragmentListener {
    private Context mContext;
    private VerifyFragmentListener listener;
    private View view;
    private Button verifyButton, backButton;
    private CheckView checkView;
    private ImageView imageView;
    private FadingTextView fadingTextView;
    private LinearLayout container;

    public VerifyFragment(Context context) {
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_verify, container, false);
        imageView = view.findViewById(R.id.gif);
        verifyButton = view.findViewById(R.id.btn_verify);
        backButton = view.findViewById(R.id.go_back_button);
        checkView = view.findViewById(R.id.check_view);
        fadingTextView = view.findViewById(R.id.verified_text);
        this.container = view.findViewById(R.id.container);
        int[] dimen = Graphics.getResolution(mContext);
        imageView.setLayoutParams(new LinearLayout.LayoutParams((dimen[WIDTH_INDEX]), (dimen[WIDTH_INDEX])));

        Graphics.setGifImage(mContext, R.raw.qr_code, imageView);

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onVerifyButtonClicked();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCompleteButtonClicked();
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        listener = (VerifyFragmentListener) activity;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResultOk(String result) {
        if (result.equals("ILAHIANZ:-{TEACHER}")) {
            verifyButton.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            backButton.setVisibility(View.VISIBLE);
            new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    int colorFrom = getResources().getColor(R.color.background_verify_fr);
                    int colorTo = getResources().getColor(R.color.white);
                    ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                    colorAnimation.setDuration(1000);
                    colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            container.setBackgroundColor((int) animation.getAnimatedValue());
                        }
                    });
                    colorAnimation.start();
                    checkView.setVisibility(View.VISIBLE);
                    checkView.check();
                    String[] array = {"You are a Teacher", "Verified"};
                    fadingTextView.setTexts(array);
                    fadingTextView.setTextColor(getResources().getColor(R.color.dd_green));
                    fadingTextView.setTimeout(2000, FadingTextView.MILLISECONDS);
                }
            }.start();
        } else {
            Snackbar snackbar = Snackbar.make(view, "Verification failed",
                    Snackbar.LENGTH_SHORT).setAction("Action", null);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(getResources().getColor(R.color.dark_grey));
            sbView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            snackbar.show();
        }
    }

    @Override
    public void onResultCancelled() {
        Snackbar snackbar = Snackbar.make(view, "Verification Cancelled",
                Snackbar.LENGTH_SHORT).setAction("Action", null);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(getResources().getColor(R.color.dark_grey));
        sbView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        snackbar.show();
    }
}

