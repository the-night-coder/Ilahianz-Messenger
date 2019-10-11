package com.nightcoder.ilahianz.ChatUI.Fragments.AccountFragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.nightcoder.ilahianz.R;

import java.util.Objects;


public class PersonalInfoFragment extends Fragment {

    private Context mContext;
    private BottomSheetDialog dialog;

    public PersonalInfoFragment(Context mContext) {
        this.mContext = mContext;
    }

    private LinearLayout editName;
    private TextView name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_info, container, false);
        editName = view.findViewById(R.id.edit_name);
        name = view.findViewById(R.id.name);
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditDialog("Enter Your Name", "Name", name.getText().toString());
            }
        });
        return view;
    }

    private void openEditDialog(String headingText, String hintText, String contentText) {
        dialog = new BottomSheetDialog(mContext);
        dialog.setContentView(R.layout.text_edit_dialog);
        EditText text = dialog.findViewById(R.id.edit_text);
        Button cancel = dialog.findViewById(R.id.cancel_action);
        Button save = dialog.findViewById(R.id.save_action);
        TextView heading = dialog.findViewById(R.id.heading);
        assert heading != null;
        heading.setText(headingText);
        assert text != null;
        text.setText(contentText);
        text.setHint(hintText);
        text.setSelection(0, contentText.length());
        text.setSelected(true);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

}
