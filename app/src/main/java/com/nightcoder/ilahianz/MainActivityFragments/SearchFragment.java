package com.nightcoder.ilahianz.MainActivityFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nightcoder.ilahianz.R;
import com.nightcoder.ilahianz.ScanProfileActivity;

public class SearchFragment extends Fragment {
    private Context mContext;
    private ImageView scanButton;
    private View root;

    public SearchFragment(Context mContext) {
        this.mContext = mContext;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_search, container, false);
        init();

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ScanProfileActivity.class));
            }
        });
        return root;
    }

    private void init() {
        scanButton = root.findViewById(R.id.scan_button);
    }
}
