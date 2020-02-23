package com.nightcoder.ilahianz.ChatUI.Fragments.AccountFragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nightcoder.ilahianz.BloodDonationActivity;
import com.nightcoder.ilahianz.Listeners.ProfileActivity.EditInfoListener;
import com.nightcoder.ilahianz.Listeners.ProfileActivity.EventChangeListener;
import com.nightcoder.ilahianz.Listeners.ProfileActivity.PersonalInfoFragmentListener;
import com.nightcoder.ilahianz.R;
import com.nightcoder.ilahianz.Supports.MemorySupports;
import com.nightcoder.ilahianz.Supports.ViewSupports;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.nightcoder.ilahianz.Literals.StringConstants.BLOOD_DONATE;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BIO;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BIRTH_DAY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BIRTH_MONTH;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BIRTH_YEAR;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BLOOD_DONATE;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BLOOD_TYPE;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_CITY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_DISTRICT;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_GENDER;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_NICKNAME;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_USERNAME;
import static com.nightcoder.ilahianz.Literals.StringConstants.NOT_PROVIDED;
import static com.nightcoder.ilahianz.Literals.StringConstants.USER_INFO_SP;


public class PersonalInfoFragment extends Fragment implements EventChangeListener {

    public PersonalInfoFragment(Context mContext) {
        this.mContext = mContext;
    }

    private Context mContext;
    private TextView name, birthday, gender, city,
            district, nickname, bio;
    private View view;
    private CheckBox checkBox;
    private LinearLayout bloodDetails;
    private RelativeLayout bloodParent;
    private Button participateButton;
    private TextView bloodDonationHeading;
    private LinearLayout bloodDonateProfile;
    private TextView bloodGroup;
    private EditInfoListener callback;
    private PersonalInfoFragmentListener profileEditListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_personal_info, container, false);
        LinearLayout editName = view.findViewById(R.id.edit_name);
        LinearLayout editCity = view.findViewById(R.id.edit_city);
        LinearLayout editDistrict = view.findViewById(R.id.edit_district);
        LinearLayout editNickname = view.findViewById(R.id.edit_nickname);
        LinearLayout editBio = view.findViewById(R.id.edit_bio);
        RelativeLayout editProfileImage = view.findViewById(R.id.edit_profile_image);
        init();

        bloodDetails.setVisibility(View.GONE);
        bloodDonateProfile.setVisibility(View.GONE);

        setUserData();
        editCity.setOnClickListener(editListener);
        editDistrict.setOnClickListener(editListener);
        editName.setOnClickListener(editListener);
        editBio.setOnClickListener(editListener);
        editNickname.setOnClickListener(editListener);
        editProfileImage.setOnClickListener(editListener);
        participateButton.setOnClickListener(editListener);
        editProfileImage.setOnClickListener(editListener);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.getId() == R.id.check_instruction) {
                    if (isChecked) {
                        participateButton.setText("Participate");
                    } else {
                        participateButton.setText("Cancel");
                    }
                }
            }
        });

        return view;
    }

    private void init() {
        birthday = view.findViewById(R.id.profile_birthday);
        city = view.findViewById(R.id.profile_city);
        nickname = view.findViewById(R.id.profile_nickname);
        district = view.findViewById(R.id.profile_district);
        bio = view.findViewById(R.id.profile_bio);
        gender = view.findViewById(R.id.profile_gender);
        checkBox = view.findViewById(R.id.check_instruction);
        bloodParent = view.findViewById(R.id.view_container_blood);
        bloodDonationHeading = view.findViewById(R.id.blood_donation_heading);
        bloodDonateProfile = view.findViewById(R.id.blood_donate_profile);
        bloodGroup = view.findViewById(R.id.blood_group);
        name = view.findViewById(R.id.name);
        participateButton = view.findViewById(R.id.btn_participate);
        bloodDetails = view.findViewById(R.id.blood_details_container);
    }

    private View.OnClickListener editListener = new View.OnClickListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.edit_city:
                    openEditDialog("Enter City Name", "City", city.getText().toString(),
                            InputType.TYPE_TEXT_VARIATION_PERSON_NAME, KEY_CITY);
                    break;
                case R.id.edit_district:
                    openEditDialog("Enter District", "District", district.getText().toString(),
                            InputType.TYPE_TEXT_VARIATION_PERSON_NAME, KEY_DISTRICT);
                    break;
                case R.id.edit_name:
                    openEditDialog("Enter your Name", "Name", name.getText().toString(),
                            InputType.TYPE_TEXT_VARIATION_PERSON_NAME, KEY_USERNAME);
                    break;
                case R.id.edit_bio:
                    openEditDialog("Enter Bio", "Bio",
                            bio.getText().toString(), InputType.TYPE_TEXT_FLAG_MULTI_LINE, KEY_BIO);
                    break;
                case R.id.edit_nickname:
                    openEditDialog("Enter Nickname", "Nickname",
                            nickname.getText().toString(), InputType.TYPE_TEXT_VARIATION_PERSON_NAME, KEY_NICKNAME);
                    break;
                case R.id.btn_participate:
                    if (participateButton.getText().equals("Leave")) {
                        leaveBloodGroup();
                    } else {
                        bloodDonationForm();
                    }
                    break;
                case R.id.edit_profile_image:
                    profileEditListener.onProfileEdit();
                    break;
            }
        }
    };

    private void leaveBloodGroup() {
        final Dialog dialog = ViewSupports.materialDialog(mContext, Gravity.CENTER, R.layout.confirm_dialog);
        TextView heading = dialog.findViewById(R.id.heading);
        Button confirm = dialog.findViewById(R.id.btn_confirm);
        heading.setText(getResources().getString(R.string.leave_confirmation));
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                final Dialog loading = ViewSupports.materialLoadingDialog(mContext, "Leaving...");
                loading.show();
                loading.setCancelable(false);
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                        .child(BLOOD_DONATE).child(MemorySupports.getUserInfo(mContext, KEY_ID));
                reference.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            HashMap<String, Object> update = new HashMap<>();
                            update.put(KEY_BLOOD_TYPE, NOT_PROVIDED);
                            update.put(KEY_BLOOD_DONATE, NOT_PROVIDED);
                            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users")
                                    .child(getUserInfo(KEY_ID));
                            reference1.updateChildren(update)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                loading.cancel();
                                                MemorySupports.setUserInfo(mContext, KEY_BLOOD_TYPE, NOT_PROVIDED);
                                                MemorySupports.setUserInfo(mContext, KEY_BLOOD_DONATE, NOT_PROVIDED);
                                                ViewSupports.materialSnackBar(mContext, "Removed !",
                                                        4000, R.drawable.ic_check_circle_black_24dp);
                                                setUserData();
                                            }
                                        }
                                    });
                        }
                    }
                });
            }
        });
        dialog.show();
    }


    @SuppressLint("SetTextI18n")
    private void bloodDonationForm() {
        if (participateButton.getText().toString().equals("Participate") && !checkBox.isChecked()) {
            ViewSupports.visibilitySlideAnimation(Gravity.TOP, 500, bloodDetails, bloodParent, View.VISIBLE);
            participateButton.setText("Cancel");
        } else if (participateButton.getText().toString().equals("Cancel") && !checkBox.isChecked()) {
            ViewSupports.visibilitySlideAnimation(Gravity.TOP, 500, bloodDetails, bloodParent, View.GONE);
            participateButton.setText("Participate");
        } else if (!checkBox.isChecked()) {
            Toast.makeText(mContext, "Agree Instructions", Toast.LENGTH_SHORT).show();
        } else {
            mContext.startActivity(new Intent(mContext, BloodDonationActivity.class));
        }
    }

    private void openEditDialog(String headingText, String hintText, String contentText, int inputType, final String key) {
        final Dialog dialog = ViewSupports.materialDialog(mContext, R.layout.text_edit_dialog);
        final EditText text = dialog.findViewById(R.id.edit_text);
        Button cancel = dialog.findViewById(R.id.cancel_action);
        Button save = dialog.findViewById(R.id.save_action);
        TextView heading = dialog.findViewById(R.id.heading);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEdits(key, text.getText().toString());
                dialog.cancel();
            }
        });
        assert heading != null;
        heading.setText(headingText);
        assert text != null;
        text.setText(contentText);
        text.setHint(hintText);
        text.setSelection(0, contentText.length());
        text.requestFocus(contentText.length());
        text.setInputType(inputType);
        Window window = dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
        showKeyboard();
        dialog.setOnCancelListener(dialogListener);
    }

    private Dialog.OnCancelListener dialogListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            hideKeyboard();
        }
    };

    @SuppressLint("SetTextI18n")
    private void setUserData() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Integer.parseInt(getUserInfo(KEY_BIRTH_MONTH)));
        String date = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US) +
                " " + getUserInfo(KEY_BIRTH_DAY) + ", " + getUserInfo(KEY_BIRTH_YEAR);

        name.setText(getUserInfo(KEY_USERNAME));
        birthday.setText(date);
        gender.setText(getUserInfo(KEY_GENDER));
        nickname.setText(getUserInfo(KEY_NICKNAME));
        city.setText(getUserInfo(KEY_CITY));
        district.setText(getUserInfo(KEY_DISTRICT));
        bio.setText(getUserInfo(KEY_BIO));
        if (!getUserInfo(KEY_BLOOD_DONATE).equals(NOT_PROVIDED)) {
            bloodDonateProfile.setVisibility(View.VISIBLE);
            bloodDonationHeading.setText("Blood Donation Profile");
            participateButton.setText("Leave");
            bloodGroup.setText(getUserInfo(KEY_BLOOD_TYPE));
        } else {
            ViewSupports.visibilityFadeAnimation(600, bloodDonateProfile, (ViewGroup) view.getRootView(), View.GONE);
            bloodDonationHeading.setText("Blood Donation Group");
            participateButton.setText("Participate");
        }
    }

    private String getUserInfo(String key) {
        SharedPreferences preferences = mContext.getSharedPreferences(USER_INFO_SP, MODE_PRIVATE);
        return preferences.getString(key, "none");
    }

    private void setEdits(String key, String data) {
        callback.setEdits(key, data);
    }

    private void showKeyboard() {
        InputMethodManager inputMethod = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert inputMethod != null;
        inputMethod.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void hideKeyboard() {
        InputMethodManager inputMethod = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert inputMethod != null;
        inputMethod.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        callback = (EditInfoListener) activity;
        profileEditListener = (PersonalInfoFragmentListener) activity;
    }

    @Override
    public void onDataChange() {
        setUserData();
    }
}
