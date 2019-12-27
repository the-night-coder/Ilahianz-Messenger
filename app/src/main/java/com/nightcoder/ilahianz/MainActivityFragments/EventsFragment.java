package com.nightcoder.ilahianz.MainActivityFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.nightcoder.ilahianz.BloodConsoleActivity;
import com.nightcoder.ilahianz.R;


public class EventsFragment extends Fragment {

    private Context mContext;
    private Button bloodConsoleBtn;
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.blood_console_button:
                    mContext.startActivity(new Intent(mContext, BloodConsoleActivity.class));
                    break;
            }
        }
    };

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
        return view;
    }

    private void initViews(View view) {
        bloodConsoleBtn = view.findViewById(R.id.blood_console_button);
    }

    private void initCallbacks(){
        bloodConsoleBtn.setOnClickListener(clickListener);
    }
}
