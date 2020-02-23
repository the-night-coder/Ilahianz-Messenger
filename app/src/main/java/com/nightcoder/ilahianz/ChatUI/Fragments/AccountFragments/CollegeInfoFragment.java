package com.nightcoder.ilahianz.ChatUI.Fragments.AccountFragments;

import android.app.Activity;
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

import com.nightcoder.ilahianz.Listeners.ProfileActivity.EditInfoListener;
import com.nightcoder.ilahianz.Listeners.ProfileActivity.EventChangeListener;
import com.nightcoder.ilahianz.R;
import com.nightcoder.ilahianz.Supports.MemorySupports;
import com.nightcoder.ilahianz.Supports.ViewSupports;

import static android.content.Context.MODE_PRIVATE;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ACADEMIC_YEAR_FROM;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ACADEMIC_YEAR_TO;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_CATEGORY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_DEPARTMENT;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_EMAIL;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID_NUMBER;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_PH_NUMBER;
import static com.nightcoder.ilahianz.Literals.StringConstants.USER_INFO_SP;


public class CollegeInfoFragment extends Fragment implements EventChangeListener {

    public CollegeInfoFragment(Context mContext) {
        this.mContext = mContext;
    }

    private Context mContext;
    private TextView department, category,
            academicYear, idNumber, email,
            phone;
    private View view;
    private EditInfoListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_college_info, container, false);
        LinearLayout editPhone = view.findViewById(R.id.edit_phone);


        editPhone.setOnClickListener(editListener);
        init();

        return view;
    }

    private void init() {
        idNumber = view.findViewById(R.id.profile_id);
        department = view.findViewById(R.id.profile_department);
        email = view.findViewById(R.id.profile_email);
        phone = view.findViewById(R.id.profile_phone);
        academicYear = view.findViewById(R.id.profile_academic_year);
        category = view.findViewById(R.id.profile_category);
        setUserData();
    }

    private View.OnClickListener editListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.edit_phone) {
                openEditDialog("Enter Phone Number", "Phone Number",
                        phone.getText().toString(), InputType.TYPE_CLASS_PHONE, KEY_PH_NUMBER);
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
        email.setText(getUserInfo(KEY_EMAIL));
        phone.setText(getUserInfo(KEY_PH_NUMBER));
        department.setText(getUserInfo(KEY_DEPARTMENT));
        category.setText(getUserInfo(KEY_CATEGORY));
        idNumber.setText(getUserInfo(KEY_ID_NUMBER));
        academicYear.setText(String.format("%s to %s", MemorySupports.getUserInfo(mContext, KEY_ACADEMIC_YEAR_FROM),
                MemorySupports.getUserInfo(mContext, KEY_ACADEMIC_YEAR_TO)));
    }

    private String getUserInfo(String key) {
        SharedPreferences preferences = mContext.getSharedPreferences(USER_INFO_SP, MODE_PRIVATE);
        return preferences.getString(key, "none");
    }

    private void setEdits(String key, String data) {
        listener.setEdits(key, data);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        listener = (EditInfoListener) activity;
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
    public void onDataChange() {
        setUserData();
    }
}
