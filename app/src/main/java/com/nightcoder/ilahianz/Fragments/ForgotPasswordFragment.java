package com.nightcoder.ilahianz.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.nightcoder.ilahianz.R;
import com.nightcoder.ilahianz.Supports.Graphics;

import java.util.Objects;

import cdflynn.android.library.checkview.CheckView;

public class ForgotPasswordFragment extends Fragment {

    private Context mContext;

    public ForgotPasswordFragment(Context mContext) {
        this.mContext = mContext;
    }

    private EditText email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        ImageView right = view.findViewById(R.id.right_drawable);
        ImageView left = view.findViewById(R.id.left_drawable);
        email = view.findViewById(R.id.email);
        Button sent = view.findViewById(R.id.sent_action);

        int[] dimen = Graphics.getResolution(Objects.requireNonNull(getContext()));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                (dimen[0] / 2), (dimen[0] / 2)
        );

        params.setMargins(0, (dimen[1] / 20), 0, 0);
        right.setLayoutParams(params);
        left.setLayoutParams(new LinearLayout.LayoutParams((dimen[0] / 2), (dimen[0] / 2)));

        sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!email.getText().toString().isEmpty()) {
                    sentMail(email.getText().toString());
                } else {
                    Toast.makeText(mContext, "Provide email address !", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void sentMail(String mail) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.loading_progressbar_circle);
        final CheckView checkView = dialog.findViewById(R.id.check_view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    checkView.setVisibility(View.VISIBLE);
                    checkView.check();
                    Toast.makeText(mContext, "Mail sent", Toast.LENGTH_SHORT).show();
                    new CountDownTimer(1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            dialog.cancel();
                        }
                    }.start();
                } else {
                    dialog.cancel();
                    Toast.makeText(mContext, "Something went to wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }
}
