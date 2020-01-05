package com.nightcoder.ilahianz.MainActivityFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nightcoder.ilahianz.Adapters.FeatureAdapter;
import com.nightcoder.ilahianz.BloodConsoleActivity;
import com.nightcoder.ilahianz.Models.FeatureModel;
import com.nightcoder.ilahianz.R;

import java.util.ArrayList;
import java.util.List;


public class EventsFragment extends Fragment {

    private Context mContext;
    private RecyclerView recyclerView;

    public EventsFragment(Context context) {
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_user, container, false);
        initViews(view);
        initCallbacks();
        initAnimation();

        ArrayList<FeatureModel> data = new ArrayList<>();
        data.add(new FeatureModel("Blood Donation Console", getString(R.string.blood_donation_banner_quotes),
                R.mipmap.blood_transfusion, "Go to Blood Donation Console"));
        data.add(new FeatureModel("Noticeboard", getString(R.string.notice_quotes),
                R.mipmap.megaphone, "Go to Noticeboard"));
        data.add(new FeatureModel("Ideas & Thoughts", getString(R.string.ideas_quotes),
                R.mipmap.marketing, "Explore your Ideas with Ilahianz"));
        data.add(new FeatureModel("Feedback", getString(R.string.feedback_quotes),
                R.mipmap.feedback, "Send feedback"));

        FeatureAdapter featureAdapter = new FeatureAdapter(mContext, data);

        recyclerView.setAdapter(featureAdapter);

        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    private void initAnimation() {


    }

    private void initCallbacks() {
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    };
}
