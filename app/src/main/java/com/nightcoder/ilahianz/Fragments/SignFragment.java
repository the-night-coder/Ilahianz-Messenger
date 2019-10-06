package com.nightcoder.ilahianz.Fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nightcoder.ilahianz.Listeners.SignInFragmentListener;
import com.nightcoder.ilahianz.R;
import com.nightcoder.ilahianz.Supports.Graphics;

import java.util.HashMap;


public class SignFragment extends Fragment {

    private SignInFragmentListener listener;
    private EditText email, password;
    private Button signin, create;
    private Context mContext;

    public SignFragment(Context context) {
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign, container, false);
        email = view.findViewById(R.id.txt_email);
        password = view.findViewById(R.id.text_password);
        signin = view.findViewById(R.id.login_btn);
        create = view.findViewById(R.id.register_btn);
        //
        ImageView right = view.findViewById(R.id.right_drawable);
        ImageView left = view.findViewById(R.id.left_drawable);

        int[] dimen = Graphics.getResolution(mContext);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                (dimen[0] / 2), (dimen[0] / 2)
        );

        params.setMargins(0, (dimen[1] / 20), 0, 0);
        right.setLayoutParams(params);
        left.setLayoutParams(new LinearLayout.LayoutParams((dimen[0] / 2), (dimen[0] / 2)));
//
        logIn();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        listener = (SignInFragmentListener) activity;
    }

    private void logIn() {
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text_email = email.getText().toString();
                String text_password = password.getText().toString();
                if (!(text_email.isEmpty() && text_password.isEmpty()))
                    listener.OnSignInButtonClicked(text_email, text_password);
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnRegisterButtonClicked();
            }
        });

    }
}
