package com.nightcoder.ilahianz.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.nightcoder.ilahianz.Listeners.FragmentListeners.VFragmentListener;
import com.nightcoder.ilahianz.Listeners.FragmentListeners.VerifyFragmentListener;
import com.nightcoder.ilahianz.R;
import com.nightcoder.ilahianz.Supports.Graphics;

import static com.nightcoder.ilahianz.Literals.IntegerConstats.WIDTH_INDEX;


public class VerifyFragment extends Fragment implements VFragmentListener {
    private Context mContext;
    private VerifyFragmentListener listener;
    private View view;
    private Button verifyButton, backButton;

    public VerifyFragment(Context context) {
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_verify, container, false);
        ImageView imageView = view.findViewById(R.id.gif);
        verifyButton = view.findViewById(R.id.btn_verify);
        backButton = view.findViewById(R.id.go_back_button);
        int[] dimen = Graphics.getResolution(mContext);
        imageView.setLayoutParams(new LinearLayout.LayoutParams((dimen[WIDTH_INDEX]), (dimen[WIDTH_INDEX])));

        Glide.with(mContext).load(R.raw.programer).into(imageView);

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
            Snackbar snackbar = Snackbar.make(view, "Verified",
                    Snackbar.LENGTH_SHORT).setAction("Action", null);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(getResources().getColor(R.color.dd_green));
            sbView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            snackbar.show();
            verifyButton.setText("Verified");
            verifyButton.setBackgroundColor(getResources().getColor(R.color.dd_green));
            backButton.setVisibility(View.VISIBLE);
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

