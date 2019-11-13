package com.nightcoder.ilahianz.ChatUI.Fragments.BloodDonationFragments;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nightcoder.ilahianz.Listeners.BloodDonation.BloodDonationFormCallbacks;
import com.nightcoder.ilahianz.R;
import com.nightcoder.ilahianz.Supports.Graphics;
import com.nightcoder.ilahianz.Supports.MemorySupports;
import com.nightcoder.ilahianz.Supports.ViewSupports;

import java.util.Calendar;
import java.util.HashMap;

import static com.nightcoder.ilahianz.Literals.StringConstants.BLOOD_DONATE;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_AGE;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BIRTH_YEAR;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BLOOD_TYPE;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_GENDER;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_USERNAME;

public class BloodDonationForm extends Fragment {


    private Context mContext;
    private BloodDonationFormCallbacks callbacks;
    private Button participateButton;
    private View root;
    private ImageView banner;
    private EditText name, age;
    private TextView gender, bloodGroup;
    private RadioGroup bloodGroups, antiBodies;
    private String bloodTypeText;
    private String bloodAntibodyText;
    private boolean bloodTypeSelected = false;
    private CheckBox checkBox;


    public BloodDonationForm(Context mContext) {
        this.mContext = mContext;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.blood_donation_form, container, false);

        init();
        setUserData();

        Graphics.setGifImage(mContext, R.raw.blood_banner, banner);

        participateButton.setOnClickListener(clickListener);
        bloodGroups.setOnCheckedChangeListener(checkedChangeListener);
        antiBodies.setOnCheckedChangeListener(checkedChangeListener);

        return root;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_dialog_participate) {
                participation();
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (group.getId() == R.id.blood_groups) {
                RadioButton rb = root.findViewById(checkedId);
                bloodTypeText = rb.getText().toString();
                if (bloodAntibodyText != null)
                    setBloodType(String.format("%s%s", bloodTypeText, bloodAntibodyText));
            } else if (group.getId() == R.id.blood_group_anti_body) {
                if (bloodTypeText == null) {
                    Toast.makeText(mContext, "Select blood type", Toast.LENGTH_SHORT).show();
                    bloodTypeSelected = false;
                } else {
                    RadioButton rb = root.findViewById(checkedId);
                    setBloodType(String.format("%s%s", bloodTypeText, rb.getText().toString()));
                    bloodTypeSelected = true;
                    bloodAntibodyText = rb.getText().toString();
                }
            }
        }
    };

    private void participation() {
        final HashMap<String, Object> hashMap = new HashMap<>();
        if (name.getText().length() < 3) {
            Toast.makeText(mContext, "Provide Name", Toast.LENGTH_SHORT).show();
            name.setError("Required");
        } else if (age.getText().length() < 2) {
            Toast.makeText(mContext, "Provide Age", Toast.LENGTH_SHORT).show();
            age.setError("Required");
        } else if (!bloodTypeSelected) {
            Toast.makeText(mContext, "Select Blood Group", Toast.LENGTH_SHORT).show();
        } else if (!checkBox.isChecked()) {
            Toast.makeText(mContext, "Complete the form", Toast.LENGTH_SHORT).show();
        } else {
            hashMap.put(KEY_USERNAME, name.getText().toString());
            hashMap.put(KEY_ID, MemorySupports.getUserInfo(mContext, KEY_ID));
            hashMap.put(KEY_AGE, age.getText().toString());
            hashMap.put(KEY_BLOOD_TYPE, bloodGroup.getText().toString());
            hashMap.put(KEY_GENDER, MemorySupports.getUserInfo(mContext, KEY_GENDER));
            MemorySupports.setUserInfo(mContext, KEY_BLOOD_TYPE, bloodGroup.getText().toString());

            final Dialog dialog = ViewSupports.materialDialog(mContext, Gravity.CENTER, R.layout.blood_donation_confirm_dialog);

            TextView username, dialogAge, gender, bloodType;
            username = dialog.findViewById(R.id.name);
            dialogAge = dialog.findViewById(R.id.age);
            gender = dialog.findViewById(R.id.gender);
            bloodType = dialog.findViewById(R.id.blood_group);
            Button confirm = dialog.findViewById(R.id.btn_confirm);

            username.setText(name.getText().toString());
            dialogAge.setText(age.getText().toString());
            bloodType.setText(bloodGroup.getText().toString());
            gender.setText(this.gender.getText().toString());

            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callbacks.onParticipateButtonClicked(hashMap);
                    dialog.cancel();
                }
            });

            dialog.show();

            //debug
            Log.d(KEY_USERNAME, name.getText().toString());
            Log.d(KEY_ID, MemorySupports.getUserInfo(mContext, KEY_ID));
            Log.d(KEY_AGE, age.getText().toString());
            Log.d(KEY_BLOOD_TYPE, bloodGroup.getText().toString());
            Log.d(KEY_GENDER, MemorySupports.getUserInfo(mContext, KEY_GENDER));
        }

    }

    private void init() {
        participateButton = root.findViewById(R.id.btn_dialog_participate);
        banner = root.findViewById(R.id.banner);
        name = root.findViewById(R.id.name);
        age = root.findViewById(R.id.age);
        gender = root.findViewById(R.id.gender);
        bloodGroup = root.findViewById(R.id.blood_group);
        bloodGroups = root.findViewById(R.id.blood_groups);
        antiBodies = root.findViewById(R.id.blood_group_anti_body);
        checkBox = root.findViewById(R.id.check_instruction);
        bloodGroup.setVisibility(View.GONE);
    }

    private void setUserData() {
        name.setText(MemorySupports.getUserInfo(mContext, KEY_USERNAME));
        gender.setText(MemorySupports.getUserInfo(mContext, KEY_GENDER));

        Calendar today = Calendar.getInstance();
        age.setText(String.valueOf((today.get(Calendar.YEAR)
                - Integer.parseInt(MemorySupports.getUserInfo(mContext, KEY_BIRTH_YEAR)))));
    }

    private void setBloodType(String type) {
        ViewSupports.visibilityFadeAnimation(600, bloodGroup, (ViewGroup) root, View.VISIBLE);
        bloodGroup.setText(type);
        int colorFrom = getResources().getColor(R.color.transparent_blue);
        int colorTo = getResources().getColor(R.color.black);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(500);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                bloodGroup.setTextColor((int) animation.getAnimatedValue());
            }
        });
        colorAnimation.start();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        callbacks = (BloodDonationFormCallbacks) activity;
    }
}
