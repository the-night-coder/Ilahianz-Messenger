package com.nightcoder.ilahianz.ChatUI.Fragments.AccountFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.nightcoder.ilahianz.R;


public class SettingsFragment extends Fragment {

    public SettingsFragment(Context mContext) {
        this.mContext = mContext;
    }

    private Context mContext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

}
