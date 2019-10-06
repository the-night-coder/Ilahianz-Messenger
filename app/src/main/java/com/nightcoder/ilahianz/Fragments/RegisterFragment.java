package com.nightcoder.ilahianz.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.nightcoder.ilahianz.Listeners.RegisterFragmentListener;
import com.nightcoder.ilahianz.Listeners.SignInFragmentListener;
import com.nightcoder.ilahianz.R;
import com.nightcoder.ilahianz.Supports.Graphics;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class RegisterFragment extends Fragment {

    private Context mContext;
    private EditText email, phone, fName, lName, password,
            cPassword, cityName, birthday;
    private Button registerButton;
    private ScrollView scrollView;
    private RadioGroup gender;
    private View view;
    private int birthDay, birthMonth, birthYear;
    private int nowDay, nowMonth, nowYear;
    private RegisterFragmentListener listener;

    public RegisterFragment(Context context) {
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_register, container, false);
        ImageView right = view.findViewById(R.id.right_drawable);
        ImageView left = view.findViewById(R.id.left_drawable);
        int[] dimen = Graphics.getResolution(mContext);
        init();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                (dimen[0] / 2), (dimen[0] / 2)
        );

        params.setMargins(0, (dimen[1] / 20), 0, 0);
        right.setLayoutParams(params);
        left.setLayoutParams(new LinearLayout.LayoutParams((dimen[0] / 2), (dimen[0] / 2)));
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCalender();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        listener = (RegisterFragmentListener) activity;
    }

    private void init() {
        fName = view.findViewById(R.id.first_name);
        lName = view.findViewById(R.id.last_name);
        password = view.findViewById(R.id.password);
        cPassword = view.findViewById(R.id.confirm_password);
        cityName = view.findViewById(R.id.city);
        email = view.findViewById(R.id.email);
        phone = view.findViewById(R.id.phone);
        registerButton = view.findViewById(R.id.btn_register);
        scrollView = view.findViewById(R.id.form_scroll);
        gender = view.findViewById(R.id.radio_gender);
        birthday = view.findViewById(R.id.birthday);
        Calendar calendar = Calendar.getInstance();
        nowMonth = calendar.get(Calendar.MONTH);
        nowDay = calendar.get(Calendar.DAY_OF_MONTH);
        nowYear = 2000;
        birthDay = birthMonth = birthYear = 0;
    }

    private void registerUser() {
        String fullName = (fName.getText().toString() + " " + lName.getText().toString());
        String pass = password.getText().toString();
        String cPass = cPassword.getText().toString();
        String cityText = cityName.getText().toString();
        String genderText = getGender();
        if (fullName.isEmpty()) {
            fName.setError("Required");
            lName.setError("Required");
            scrollView.smoothScrollTo(0, 0);
        } else if (genderText == null) {
            Toast.makeText(mContext, "Please Specify your Gender", Toast.LENGTH_SHORT).show();
        } else if (birthDay == 0 || birthMonth == 0 || birthYear == 0) {
            Toast.makeText(mContext, "Provide your date of birth", Toast.LENGTH_SHORT).show();
            birthday.setError("Required");
            scrollView.smoothScrollTo(0, 5);
        } else if (cityText.isEmpty()) {
            Toast.makeText(mContext, "Provide your city", Toast.LENGTH_SHORT).show();
            scrollView.smoothScrollTo(0, 10);
            cityName.setError("Required");
        } else if (email.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "Provide your email", Toast.LENGTH_SHORT).show();
            email.setError("Required");
        } else if (pass.isEmpty()) {
            Toast.makeText(mContext, "Provide a password", Toast.LENGTH_SHORT).show();
            password.setError("Required");
        } else if (cPass.isEmpty()) {
            Toast.makeText(mContext, "Confirm your password", Toast.LENGTH_SHORT).show();
            cPassword.setError("Required");
        } else if (!pass.equals(cPass)) {
            Toast.makeText(mContext, "Password not match", Toast.LENGTH_SHORT).show();
            cPassword.setError("Not match");
        } else {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("username", fullName);
            hashMap.put("search", fullName.toLowerCase());
            hashMap.put("imageURL", "default");
            hashMap.put("LastSeenPrivacy", "Everyone");
            hashMap.put("ProfilePrivacy", "Everyone");
            hashMap.put("AboutPrivacy", "Everyone");
            hashMap.put("LocationPrivacy", "Everyone");
            hashMap.put("EmailPrivacy", "Everyone");
            hashMap.put("PhonePrivacy", "Everyone");
            hashMap.put("BirthdayPrivacy", "Everyone");
            hashMap.put("PhoneNumber", phone.getText().toString().isEmpty() ?
                    "Not Provided" : phone.getText().toString());
            hashMap.put("gender", genderText);
            hashMap.put("Latitude", "Not Provided");
            hashMap.put("Longitude", "Not Provided");
            hashMap.put("thumbnailURL", "default");
            hashMap.put("email", email.getText().toString());
            hashMap.put("Description", "Hey Ilahianz");
            hashMap.put("city", cityText);
            hashMap.put("district", "Not Provided");
            hashMap.put("bio", "Not Provided");
            hashMap.put("Birthday", birthDay);
            hashMap.put("BirthYear", birthYear);
            hashMap.put("BirthMonth", birthMonth);

            listener.OnRegisterButtonClicked(hashMap);
        }

    }

    private String getGender() {
        int genderId = gender.getCheckedRadioButtonId();
        RadioButton genderTxt = view.findViewById(genderId);
        if (genderTxt != null) {
            return genderTxt.getText().toString();
        }
        return null;
    }

    private void getCalender() {
        DatePickerDialog dialog = new DatePickerDialog(
                mContext, android.R.style.Theme_Material_Light_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        nowDay = birthDay = dayOfMonth;
                        nowMonth = birthMonth = month;
                        nowYear = birthYear = year;

                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(Calendar.MONTH, month);
                        birthday.setText(dayOfMonth + " - " + calendar1.getDisplayName(Calendar.MONTH,
                                Calendar.LONG, Locale.US) + " - " + year);
                    }
                }, nowYear, nowMonth, nowDay);
        dialog.show();
    }

}
