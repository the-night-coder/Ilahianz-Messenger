package com.nightcoder.ilahianz.ChatUI.Fragments.AccountFragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.nightcoder.ilahianz.Listeners.ProfileActivity.EventChangeListener;
import com.nightcoder.ilahianz.R;
import com.nightcoder.ilahianz.SignActivity;
import com.nightcoder.ilahianz.Supports.ViewSupports;


public class SettingsFragment extends Fragment implements EventChangeListener {

    public SettingsFragment(Context mContext) {
        this.mContext = mContext;
    }

    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        Button logOut = view.findViewById(R.id.btn_log_out);
        logOut.setOnClickListener(clickListener);
        return view;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_log_out:
                    logOut();
                    break;
            }
        }
    };

    private void logOut() {
        Dialog dialog = ViewSupports.materialSignOutDialog(mContext);
        dialog.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FirebaseAuth.getInstance().signOut();
                    mContext.startActivity(new Intent(mContext, SignActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dialog.show();

    }

    @Override
    public void onDataChange() {
        
    }
}
