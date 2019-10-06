package com.nightcoder.ilahianz.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.nightcoder.ilahianz.R;
import com.nightcoder.ilahianz.Supports.Graphics;

import java.util.Objects;

public class BackgroundFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_background, container, false);
        ImageView right = view.findViewById(R.id.right_drawable);
        ImageView left = view.findViewById(R.id.left_drawable);

        int[] dimen = Graphics.getResolution(Objects.requireNonNull(getContext()));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                (dimen[0] / 2), (dimen[0] / 2)
        );

        params.setMargins(0, (dimen[1] / 20), 0, 0);
        right.setLayoutParams(params);
        left.setLayoutParams(new LinearLayout.LayoutParams((dimen[0] / 2), (dimen[0] / 2)));
        return view;
    }
}
