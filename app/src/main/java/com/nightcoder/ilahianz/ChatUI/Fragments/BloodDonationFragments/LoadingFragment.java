package com.nightcoder.ilahianz.ChatUI.Fragments.BloodDonationFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nightcoder.ilahianz.Listeners.BloodDonation.LoadingFragmentCallbacks;
import com.nightcoder.ilahianz.R;

public class LoadingFragment extends Fragment implements LoadingFragmentCallbacks {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.loading_blood_donation_progress, container, false);
        return root;
    }

    @Override
    public void onProcessFailed() {

    }

    @Override
    public void onProcessSuccess() {

    }
}
