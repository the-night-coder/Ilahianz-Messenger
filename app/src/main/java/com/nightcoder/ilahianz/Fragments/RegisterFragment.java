package com.nightcoder.ilahianz.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.nightcoder.ilahianz.Listeners.FragmentListeners.RegisterFragmentListener;
import com.nightcoder.ilahianz.Listeners.QRCodeListener;
import com.nightcoder.ilahianz.R;
import com.nightcoder.ilahianz.Supports.Graphics;
import com.nightcoder.ilahianz.Supports.Network;
import com.nightcoder.ilahianz.Supports.ViewSupports;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import static android.provider.Telephony.Carriers.PASSWORD;
import static com.nightcoder.ilahianz.Literals.StringConstants.DEFAULT;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BIRTH_DAY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BIRTH_MONTH;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BIRTH_YEAR;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_CATEGORY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_CITY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_DEPARTMENT;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_EMAIL;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_GENDER;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID_NUMBER;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_IMAGE_URL;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_PH_NUMBER;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_SEARCH;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_USERNAME;
import static com.nightcoder.ilahianz.Literals.StringConstants.NOT_PROVIDED;
import static com.nightcoder.ilahianz.Literals.StringConstants.REG_FRAGMENT_TAG;

public class RegisterFragment extends Fragment implements QRCodeListener {

    private Context mContext;
    private EditText email, phone, fName, lName, password,
            cPassword, cityName, birthday;
    private Button registerButton;
    private NestedScrollView scrollView;
    private RadioGroup gender;
    private View view;
    private int birthDay = 0, birthMonth = 0, birthYear = 0;
    private int nowDay, nowMonth, nowYear;
    private RegisterFragmentListener listener;
    private RadioButton student, teacher;
    private EditText department, idNumber;
    private Button scanButton;
    private boolean verified = true;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    private String categoryText = "Student";
    private ViewGroup mViewGroup;
    private RelativeLayout formContainer;


    public RegisterFragment(Context context) {
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_register, container, false);
        ImageView right = view.findViewById(R.id.right_drawable);
        ImageView left = view.findViewById(R.id.left_drawable);
        mViewGroup = container;
        int[] dimen = Graphics.getResolution(mContext);
        init();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                (dimen[0] / 2), (dimen[0] / 2)
        );
        params.setMargins(0, (dimen[1] / 20), 0, 0);
        right.setLayoutParams(params);
        left.setLayoutParams(new LinearLayout.LayoutParams((dimen[0] / 2), (dimen[0] / 2)));

        formContainer.setVisibility(View.GONE);
        ViewSupports.visibilitySlideAnimation(Gravity.BOTTOM, 600, formContainer, mViewGroup, View.VISIBLE);
        birthday.setOnClickListener(clickListener);
        scanButton.setOnClickListener(clickListener);
        registerButton.setOnClickListener(clickListener);
        student.setOnClickListener(clickListener);
        teacher.setOnClickListener(clickListener);
        department.setOnClickListener(clickListener);

        return view;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.department:
                    openDepartmentDialog();
                    break;
                case R.id.radio_teacher:
                    openCategory();
                    break;
                case R.id.radio_student: {
                    registerButton.setText(getResources().getString(R.string.create_account));
                    verified = true;
                    categoryText = "Student";
                }
                break;
                case R.id.scan_button:
                    listener.OnIDScanRequest();
                    break;
                case R.id.btn_register: {
                    if (registerButton.getText().equals(getResources().getString(R.string.create_account)))
                        registerUser();
                    else
                        listener.OnScannerRequest();
                }
                break;
                case R.id.birthday:
                    getCalender();
                    break;

            }
        }
    };

    private void init() {
        scanButton = view.findViewById(R.id.scan_button);
        idNumber = view.findViewById(R.id.id);
        fName = view.findViewById(R.id.first_name);
        lName = view.findViewById(R.id.last_name);
        password = view.findViewById(R.id.password);
        cPassword = view.findViewById(R.id.confirm_password);
        cityName = view.findViewById(R.id.city);
        email = view.findViewById(R.id.email);
        phone = view.findViewById(R.id.phone);
        student = view.findViewById(R.id.radio_student);
        teacher = view.findViewById(R.id.radio_teacher);
        registerButton = view.findViewById(R.id.btn_register);
        scrollView = view.findViewById(R.id.form_scroll);
        gender = view.findViewById(R.id.radio_gender);
        birthday = view.findViewById(R.id.birthday);
        department = view.findViewById(R.id.department);
        formContainer = view.findViewById(R.id.form_container);
        idNumber = view.findViewById(R.id.id);
        Calendar calendar = Calendar.getInstance();
        nowMonth = calendar.get(Calendar.MONTH);
        nowDay = calendar.get(Calendar.DAY_OF_MONTH);
        nowYear = 2000;
    }

    private void openCategory() {
        registerButton.setText(getResources().getString(R.string.verify_you));
        arrayList = new ArrayList<>();
        arrayList.add("Head Of Department (HOD)");
        arrayList.add("Assistant HOD");
        arrayList.add("Lecturer");
        arrayList.add("Guest Lecturer");
        arrayList.add("Professor");
        arrayList.add("Other Staff");
        adapter = new ArrayAdapter<>(mContext, R.layout.list_item, arrayList);
        final Dialog dialog = ViewSupports.materialDialog(mContext, R.layout.staff_list);
        final ListView listView = dialog.findViewById(R.id.list);
        final Button cancel = dialog.findViewById(R.id.cancel_action);
        listView.setAdapter(adapter);
        dialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) listView.getAdapter().getItem(position);
                teacher.setText(item);
                categoryText = item;
                dialog.cancel();
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (categoryText.equals("Student")) {
                    student.setChecked(true);
                    registerButton.setText(getResources().getString(R.string.create_account));
                }
            }
        });

        verified = false;
    }

    private void openDepartmentDialog() {
        final Dialog dialog = ViewSupports.materialDialog(mContext, R.layout.departments_list_dialog);
        Button cancel = dialog.findViewById(R.id.cancel_action);
        final ListView listView = dialog.findViewById(R.id.list);
        arrayList = new ArrayList<>();
        arrayList.add("Bachelor Of Computer Applications (BCA)");
        arrayList.add("Bachelor Of Business Administrations (BBA)");
        arrayList.add("B.COM");
        arrayList.add("BA");
        arrayList.add("MCA");
        arrayList.add("Electronics");
        arrayList.add("Economics");
        arrayList.add("Maths");
        arrayList.add("Malayalam");
        arrayList.add("English");
        adapter = new ArrayAdapter<>(mContext, R.layout.list_item, arrayList);
        listView.setAdapter(adapter);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) listView.getAdapter().getItem(position);
                department.setText(item);
                dialog.cancel();
            }
        });

        dialog.show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        listener = (RegisterFragmentListener) activity;
    }

    private void registerUser() {
        String fullName = (fName.getText().toString() + " " + lName.getText().toString());
        String pass = password.getText().toString();
        String cPass = cPassword.getText().toString();
        String cityText = cityName.getText().toString();
        String genderText = getGender();
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
        } else if (department.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "Provide your department", Toast.LENGTH_SHORT).show();
            department.setError("Required!");
        } else if (idNumber.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "Provide your ID Card Number", Toast.LENGTH_SHORT).show();
            idNumber.setError("Required!");
        } else if (!verified) {
            Toast.makeText(mContext, "Please verify you!", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put(KEY_USERNAME, fullName);
            hashMap.put(KEY_SEARCH, fullName.toLowerCase());
            hashMap.put(KEY_ID_NUMBER, idNumber.getText().toString());
            hashMap.put(KEY_IMAGE_URL, DEFAULT);
            hashMap.put(KEY_DEPARTMENT, department.getText().toString());
            hashMap.put(KEY_CATEGORY, categoryText);
            hashMap.put(KEY_GENDER, genderText);
            hashMap.put(KEY_EMAIL, email.getText().toString());
            hashMap.put(KEY_CITY, cityText);
            hashMap.put(KEY_BIRTH_DAY, String.valueOf(birthDay));
            hashMap.put(KEY_BIRTH_YEAR, String.valueOf(birthYear));
            hashMap.put(KEY_BIRTH_MONTH, String.valueOf(birthMonth));
            hashMap.put(PASSWORD, password.getText().toString());
            hashMap.put(KEY_PH_NUMBER, phone.getText().toString().isEmpty() ?
                    NOT_PROVIDED : phone.getText().toString());
            if (Network.Connected(mContext)) {
                listener.OnRegisterButtonClicked(hashMap,
                        email.getText().toString(), password.getText().toString());
            } else {
                Toast.makeText(mContext, "Connection Failed", Toast.LENGTH_SHORT).show();
            }

            //debugLog.d
            Log.d(KEY_USERNAME, fullName);
            Log.d(KEY_SEARCH, fullName.toLowerCase());
            Log.d(KEY_ID_NUMBER, idNumber.getText().toString());
            Log.d(KEY_IMAGE_URL, DEFAULT);
            Log.d(KEY_DEPARTMENT, department.getText().toString());
            Log.d(KEY_CATEGORY, categoryText);
            Log.d(KEY_GENDER, genderText);
            Log.d(KEY_EMAIL, email.getText().toString());
            Log.d(KEY_CITY, cityText);
            Log.d(KEY_BIRTH_DAY, String.valueOf(birthDay));
            Log.d(KEY_BIRTH_YEAR, String.valueOf(birthYear));
            Log.d(KEY_BIRTH_MONTH, String.valueOf(birthMonth));
            Log.d(PASSWORD, password.getText().toString());
            Log.d(KEY_PH_NUMBER, phone.getText().toString().isEmpty() ?
                    NOT_PROVIDED : phone.getText().toString());
            //debug//
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
                mContext, android.R.style.Theme_DeviceDefault_Dialog,
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

    @Override
    public void OnQRCodeResultOK(String result) {
        verified = true;
        if (result.equals("ILAHIANZ:-{TEACHER}")) {
            registerButton.setText(getResources().getString(R.string.create_account));
        }
    }

    @Override
    public void OnQRCodeResultCancelled() {
        verified = false;
        registerButton.setText(getResources().getString(R.string.create_account));
        student.setChecked(true);
    }

    @Override
    public void OnIDQRCodeResultOK(String id) {
        idNumber.setText(id);
    }

    @Override
    public void onDetach() {
        ViewSupports.visibilitySlideAnimation(Gravity.BOTTOM, 400, formContainer, mViewGroup, View.GONE);
        Log.d("DETACH", REG_FRAGMENT_TAG);
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        Log.d("DESTROY", REG_FRAGMENT_TAG);
        super.onDestroy();
    }
}
