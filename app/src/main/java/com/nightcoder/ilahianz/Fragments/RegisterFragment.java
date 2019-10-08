package com.nightcoder.ilahianz.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

import com.nightcoder.ilahianz.Listeners.FragmentListeners.RegisterFragmentListener;
import com.nightcoder.ilahianz.Listeners.QRCodeListener;
import com.nightcoder.ilahianz.R;
import com.nightcoder.ilahianz.Supports.Graphics;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import static android.provider.Telephony.Carriers.PASSWORD;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BIRTH_DAY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BIRTH_MONTH;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BIRTH_YEAR;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_CATEGORY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_CITY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_DEPARTMENT;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_EMAIL;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_GENDER;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_IMAGE_URL;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_PH_NUMBER;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_SEARCH;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_USERNAME;

public class RegisterFragment extends Fragment implements QRCodeListener {

    private Context mContext;
    private EditText email, phone, fName, lName, password,
            cPassword, cityName, birthday;
    private Button registerButton;
    private ScrollView scrollView;
    private RadioGroup gender;
    private View view;
    private int birthDay = 0, birthMonth = 0, birthYear = 0;
    private int nowDay, nowMonth, nowYear;
    private RegisterFragmentListener listener;
    private RadioButton student, teacher, otherStaff;
    private EditText department;
    private View bottomLine;
    private Button verify;
    private String departmentText = "Student",
            categoryText = "Student";
    private boolean verified = true;


    public RegisterFragment(Context context) {
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                department.setVisibility(View.VISIBLE);
                bottomLine.setVisibility(View.VISIBLE);
                verify.setVisibility(View.GONE);
                categoryText = "Student";
                departmentText = "Student";
                verified = true;
            }
        });
        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                department.setVisibility(View.VISIBLE);
                bottomLine.setVisibility(View.VISIBLE);
                verify.setVisibility(View.VISIBLE);
                categoryText = "Teacher";
                departmentText = "Teacher";
                verified = false;
            }
        });
        otherStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                department.setVisibility(View.GONE);
                bottomLine.setVisibility(View.GONE);
                verify.setVisibility(View.GONE);
                categoryText = "Staff";
                departmentText = "Staff";
                verified = true;
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnScannerRequest();
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
        student = view.findViewById(R.id.radio_student);
        teacher = view.findViewById(R.id.radio_teacher);
        otherStaff = view.findViewById(R.id.radio_staff);
        registerButton = view.findViewById(R.id.btn_register);
        scrollView = view.findViewById(R.id.form_scroll);
        gender = view.findViewById(R.id.radio_gender);
        birthday = view.findViewById(R.id.birthday);
        verify = view.findViewById(R.id.btn_verify);
        department = view.findViewById(R.id.department);
        bottomLine = view.findViewById(R.id.view_department);
        Calendar calendar = Calendar.getInstance();
        nowMonth = calendar.get(Calendar.MONTH);
        nowDay = calendar.get(Calendar.DAY_OF_MONTH);
        nowYear = 2000;
        student.setChecked(true);
        department.setVisibility(View.VISIBLE);
        bottomLine.setVisibility(View.VISIBLE);
        verify.setVisibility(View.GONE);
    }

    private void registerUser() {
        String fullName = (fName.getText().toString() + " " + lName.getText().toString());
        String pass = password.getText().toString();
        String cPass = cPassword.getText().toString();
        String cityText = cityName.getText().toString();
        String genderText = getGender();
        if (!(departmentText.equals("Staff"))) {
            departmentText = department.getText().toString();
        }
        if (fName.getText().toString().isEmpty()) {
            fName.setError("Required");
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
        } else if (!departmentText.equals("Staff") && department.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "Provide your department", Toast.LENGTH_SHORT).show();
        } else if (!verified) {
            Toast.makeText(mContext, "Please verify you!", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put(KEY_USERNAME, fullName);
            hashMap.put(KEY_SEARCH, fullName.toLowerCase());
            hashMap.put(KEY_IMAGE_URL, "default");
            hashMap.put(KEY_DEPARTMENT, departmentText);
            hashMap.put(KEY_CATEGORY, categoryText);
            hashMap.put(KEY_GENDER, genderText);
            hashMap.put(KEY_EMAIL, email.getText().toString());
            hashMap.put(KEY_CITY, cityText);
            hashMap.put(KEY_BIRTH_DAY, birthDay);
            hashMap.put(KEY_BIRTH_YEAR, birthYear);
            hashMap.put(KEY_BIRTH_MONTH, birthMonth);
            hashMap.put(PASSWORD, password.getText().toString());
            hashMap.put(KEY_PH_NUMBER, phone.getText().toString().isEmpty() ?
                    "Not Provided" : phone.getText().toString());
            listener.OnRegisterButtonClicked(hashMap,
                    email.getText().toString(), password.getText().toString());
            Log.d(KEY_USERNAME, fullName);
            Log.d(KEY_SEARCH, fullName.toLowerCase());
            Log.d(KEY_IMAGE_URL, "default");
            Log.d(KEY_DEPARTMENT, departmentText);
            Log.d(KEY_CATEGORY, categoryText);
            Log.d(KEY_GENDER, genderText);
            Log.d(KEY_EMAIL, email.getText().toString());
            Log.d(KEY_CITY, cityText);
            Log.d(KEY_BIRTH_DAY, String.valueOf(birthDay));
            Log.d(KEY_BIRTH_YEAR, String.valueOf(birthYear));
            Log.d(KEY_BIRTH_MONTH, String.valueOf(birthMonth));
            Log.d(KEY_PH_NUMBER, phone.getText().toString().isEmpty() ?
                    "Not Provided" : phone.getText().toString());
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

    @SuppressLint("SetTextI18n")
    @Override
    public void OnQRCodeResultOK(String result) {
        verified = true;
        if (result.equals("ILAHIANZ:-{TEACHER}")) {
            categoryText = "Teacher";
            verify.setText("Verified");
            verify.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void OnQRCodeResultCancelled() {
        verified = false;
    }
}
