package com.nightcoder.ilahianz.ChatUI.Fragments.AccountFragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nightcoder.ilahianz.R;
import com.nightcoder.ilahianz.Supports.Network;
import com.nightcoder.ilahianz.Supports.ViewSupports;

import java.util.Calendar;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BIRTH_DAY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BIRTH_MONTH;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BIRTH_YEAR;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_CATEGORY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_CITY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_DEPARTMENT;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_DISTRICT;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_EMAIL;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_GENDER;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID_NUMBER;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_PH_NUMBER;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_USERNAME;
import static com.nightcoder.ilahianz.Literals.StringConstants.USER_INFO_SP;


public class PersonalInfoFragment extends Fragment {

    public PersonalInfoFragment(Context mContext) {
        this.mContext = mContext;
    }

    private Context mContext;
    private TextView name, birthday, gender, city,
            district, department, category,
            academicYear, idNumber, email,
            phone;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_personal_info, container, false);
        LinearLayout editName = view.findViewById(R.id.edit_name);
        LinearLayout editCity = view.findViewById(R.id.edit_city);
        LinearLayout editDistrict = view.findViewById(R.id.edit_district);
        LinearLayout editPhone = view.findViewById(R.id.edit_phone);

        idNumber = view.findViewById(R.id.profile_id);
        birthday = view.findViewById(R.id.profile_birthday);
        city = view.findViewById(R.id.profile_city);
        department = view.findViewById(R.id.profile_department);
        district = view.findViewById(R.id.profile_district);
        email = view.findViewById(R.id.profile_email);
        phone = view.findViewById(R.id.profile_phone);
        academicYear = view.findViewById(R.id.profile_academic_year);
        category = view.findViewById(R.id.profile_category);
        gender = view.findViewById(R.id.profile_gender);
        name = view.findViewById(R.id.name);

        editCity.setOnClickListener(editListener);
        editDistrict.setOnClickListener(editListener);
        editName.setOnClickListener(editListener);
        editPhone.setOnClickListener(editListener);
        init();

        return view;
    }

    private void init() {
        setUserData();
    }

    private View.OnClickListener editListener = new View.OnClickListener() {
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
                case R.id.edit_phone:
                    openEditDialog("Enter Phone Number", "Phone Number",
                            phone.getText().toString(), InputType.TYPE_CLASS_PHONE, KEY_PH_NUMBER);
                    break;
            }
        }
    };

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

    private void setUserData() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Integer.parseInt(getUserInfo(KEY_BIRTH_MONTH)));
        String date = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US) +
                " " + getUserInfo(KEY_BIRTH_DAY) + ", " + getUserInfo(KEY_BIRTH_YEAR);

        name.setText(getUserInfo(KEY_USERNAME));
        birthday.setText(date);
        gender.setText(getUserInfo(KEY_GENDER));
        email.setText(getUserInfo(KEY_EMAIL));
        phone.setText(getUserInfo(KEY_PH_NUMBER));
        department.setText(getUserInfo(KEY_DEPARTMENT));
        city.setText(getUserInfo(KEY_CITY));
        district.setText(getUserInfo(KEY_DISTRICT));
        category.setText(getUserInfo(KEY_CATEGORY));
        idNumber.setText(getUserInfo(KEY_ID_NUMBER));
    }

    private void setUserInfo(String key, String value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_INFO_SP, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private String getUserInfo(String key) {
        SharedPreferences preferences = mContext.getSharedPreferences(USER_INFO_SP, MODE_PRIVATE);
        return preferences.getString(key, "none");
    }

    private void setEdits(final String key, final String data) {
        if (Network.Connected(mContext)) {
            FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
            assert fUser != null;
            DatabaseReference reference = FirebaseDatabase.getInstance()
                    .getReference("Users").child(fUser.getUid()).child(key);
            reference.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar snackbar = Snackbar.make(view, "Changes applied",
                            Snackbar.LENGTH_SHORT).setAction("Action", null);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(getResources().getColor(R.color.dd_green));
                    sbView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    snackbar.show();
                    setUserInfo(key, data);
                    init();
                }
            }).addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
                    Snackbar snackbar = Snackbar.make(view, "Changes can't applied",
                            Snackbar.LENGTH_SHORT).setAction("Action", null);
                    View sbView = snackbar.getView();
                    sbView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    snackbar.show();
                }
            });
        } else {
            Snackbar snackbar = Snackbar.make(view, "Connection required",
                    Snackbar.LENGTH_SHORT).setAction("Action", null);
            View sbView = snackbar.getView();
            sbView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            snackbar.show();
        }
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

}
