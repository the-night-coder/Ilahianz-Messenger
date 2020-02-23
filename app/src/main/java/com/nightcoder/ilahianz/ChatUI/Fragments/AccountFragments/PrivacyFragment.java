package com.nightcoder.ilahianz.ChatUI.Fragments.AccountFragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.nightcoder.ilahianz.Listeners.ProfileActivity.EditInfoListener;
import com.nightcoder.ilahianz.Listeners.ProfileActivity.EventChangeListener;
import com.nightcoder.ilahianz.R;
import com.nightcoder.ilahianz.Supports.ViewSupports;

import static android.content.Context.MODE_PRIVATE;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BIRTHDAY_PRIVACY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_EMAIL_PRIVACY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_LAST_SEEN_PRIVACY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_LOCATION_PRIVACY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_PHONE_PRIVACY;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_PROFILE_PRIVACY;
import static com.nightcoder.ilahianz.Literals.StringConstants.USER_INFO_SP;


public class PrivacyFragment extends Fragment implements EventChangeListener {

    private Context mContext;
    private EditInfoListener listener;

    private TextView phone, email, photo, lastSeen, birthday, location;
    private View view;
    private String value;

    public PrivacyFragment(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_privacy, container, false);
        LinearLayout privacyBirthday = view.findViewById(R.id.privacy_birthday);
        LinearLayout privacyEmail = view.findViewById(R.id.privacy_email);
        LinearLayout privacyPhone = view.findViewById(R.id.privacy_phone);
        LinearLayout privacyPhoto = view.findViewById(R.id.privacy_profile);
        LinearLayout privacyLastSeen = view.findViewById(R.id.privacy_last_seen);
        LinearLayout privacyLocation = view.findViewById(R.id.privacy_location);

        init();
        privacyBirthday.setOnClickListener(clickListener);
        privacyEmail.setOnClickListener(clickListener);
        privacyPhone.setOnClickListener(clickListener);
        privacyPhoto.setOnClickListener(clickListener);
        privacyLastSeen.setOnClickListener(clickListener);
        privacyLocation.setOnClickListener(clickListener);

        return view;
    }

    private void init() {

        phone = view.findViewById(R.id.phone);
        email = view.findViewById(R.id.email);
        photo = view.findViewById(R.id.profile);
        lastSeen = view.findViewById(R.id.last_seen);
        birthday = view.findViewById(R.id.birthday);
        location = view.findViewById(R.id.location);
        setUserData();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.privacy_profile:
                    openEditDialog("Profile Photo", KEY_PROFILE_PRIVACY);
                    break;
                case R.id.privacy_birthday:
                    openEditDialog("Birthday", KEY_BIRTHDAY_PRIVACY);
                    break;
                case R.id.privacy_last_seen:
                    openEditDialog("Last Seen", KEY_LAST_SEEN_PRIVACY);
                    break;
                case R.id.privacy_location:
                    openEditDialog("Location", KEY_LOCATION_PRIVACY);
                    break;
                case R.id.privacy_email:
                    openEditDialog("Email", KEY_EMAIL_PRIVACY);
                    break;
                case R.id.privacy_phone:
                    openEditDialog("Phone Number", KEY_PHONE_PRIVACY);
                    break;
            }
        }
    };

    private void openEditDialog(String headingText, final String key) {
        final Dialog dialog = ViewSupports.materialDialog(mContext, R.layout.privacy_dialog_bottom);
        Button cancel = dialog.findViewById(R.id.cancel_action);
        TextView heading = dialog.findViewById(R.id.heading);
        RadioGroup rg = dialog.findViewById(R.id.radio_group);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_everyone:
                        value = "Everyone";
                        break;
                    case R.id.radio_teachers_only:
                        value = "Teachers Only";
                        break;
                    case R.id.radio_nobody:
                        value = "Nobody";
                        break;
                }
                listener.setEdits(key, value);
                dialog.cancel();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        assert heading != null;
        heading.setText(headingText);
        Window window = dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void setUserData() {
        phone.setText(getUserInfo(KEY_PHONE_PRIVACY));
        email.setText(getUserInfo(KEY_EMAIL_PRIVACY));
        photo.setText(getUserInfo(KEY_PROFILE_PRIVACY));
        birthday.setText(getUserInfo(KEY_BIRTHDAY_PRIVACY));
        lastSeen.setText(getUserInfo(KEY_LAST_SEEN_PRIVACY));
        location.setText(getUserInfo(KEY_LOCATION_PRIVACY));

        switch (getUserInfo(KEY_BIRTHDAY_PRIVACY)) {
            case "Everyone":
                birthday.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.ic_lock_open_blue_24dp, 0);
                break;
            case "Nobody":
                birthday.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.ic_lock_green_24dp, 0);
                break;
            case "Teachers Only":
                birthday.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.ic_person_black_24dp, 0);
                break;
        }
        switch (getUserInfo(KEY_EMAIL_PRIVACY)) {
            case "Everyone":
                email.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.ic_lock_open_blue_24dp, 0);
                break;
            case "Nobody":
                email.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.ic_lock_green_24dp, 0);
                break;
            case "Teachers Only":
                email.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.ic_person_black_24dp, 0);
                break;
        }
        switch (getUserInfo(KEY_PHONE_PRIVACY)) {
            case "Everyone":
                phone.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.ic_lock_open_blue_24dp, 0);
                break;
            case "Nobody":
                phone.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.ic_lock_green_24dp, 0);
                break;
            case "Teachers Only":
                phone.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.ic_person_black_24dp, 0);
                break;
        }
        switch (getUserInfo(KEY_LAST_SEEN_PRIVACY)) {
            case "Everyone":
                lastSeen.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.ic_lock_open_blue_24dp, 0);
                break;
            case "Nobody":
                lastSeen.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.ic_lock_green_24dp, 0);
                break;
            case "Teachers Only":
                lastSeen.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.ic_person_black_24dp, 0);
                break;
        }
        switch (getUserInfo(KEY_PROFILE_PRIVACY)) {
            case "Everyone":
                photo.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.ic_lock_open_blue_24dp, 0);
                break;
            case "Nobody":
                photo.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.ic_lock_green_24dp, 0);
                break;
            case "Teachers Only":
                photo.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.ic_person_black_24dp, 0);
                break;
        }
        switch (getUserInfo(KEY_LOCATION_PRIVACY)) {
            case "Everyone":
                location.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.ic_lock_open_blue_24dp, 0);
                break;
            case "Nobody":
                location.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.ic_lock_green_24dp, 0);
                break;
            case "Teachers Only":
                location.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.ic_person_black_24dp, 0);
                break;
        }
    }

    private String getUserInfo(String key) {
        SharedPreferences preferences = mContext.getSharedPreferences(USER_INFO_SP, MODE_PRIVATE);
        return preferences.getString(key, "none");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        listener = (EditInfoListener) activity;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("LISTENER", "REMOVED");
    }

    @Override
    public void onDataChange() {
        Log.d("Data", "Changed");
        setUserData();
    }
}
