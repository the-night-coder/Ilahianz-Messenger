package com.nightcoder.ilahianz;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nightcoder.ilahianz.ChatUI.Fragments.BloodDonationFragments.BloodDonationForm;
import com.nightcoder.ilahianz.ChatUI.Fragments.BloodDonationFragments.LoadingFragment;
import com.nightcoder.ilahianz.Listeners.BloodDonation.BloodDonationActivityCallbacks;
import com.nightcoder.ilahianz.Listeners.BloodDonation.BloodDonationFormCallbacks;
import com.nightcoder.ilahianz.Supports.MemorySupports;
import com.nightcoder.ilahianz.Supports.ViewSupports;

import java.util.HashMap;

import static com.nightcoder.ilahianz.Literals.StringConstants.BLOOD_DONATE;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BLOOD_DONATE;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_BLOOD_TYPE;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;
import static com.nightcoder.ilahianz.Literals.StringConstants.NOT_PROVIDED;

public class BloodDonationActivity extends AppCompatActivity
        implements BloodDonationFormCallbacks, BloodDonationActivityCallbacks {

    private Context mContext;
    private String bloodType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_doantion);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        LinearLayout container = findViewById(R.id.container);
        container.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        container.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        mContext = BloodDonationActivity.this;

        Fragment fragment = new BloodDonationForm(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, fragment)
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .attach(fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

    }

    private void openLoadingFragment() {
        LoadingFragment fragment = new LoadingFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, fragment)
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .attach(fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onParticipateButtonClicked(HashMap hashMap) {
        final Dialog dialog = ViewSupports.materialLoadingDialog(mContext);
        dialog.show();
        dialog.setCancelable(false);
        ((TextView) dialog.findViewById(R.id.text_content)).setText("Loading...");
        bloodType = (String) hashMap.get(KEY_BLOOD_TYPE);
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child(BLOOD_DONATE).child(MemorySupports.getUserInfo(mContext, KEY_ID));
        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    DatabaseReference reference = FirebaseDatabase.getInstance()
                            .getReference("Users").child(MemorySupports.getUserInfo(mContext, KEY_ID));
                    HashMap<String, Object> update = new HashMap<>();
                    update.put(KEY_BLOOD_DONATE, MemorySupports.getUserInfo(mContext, KEY_ID));
                    update.put(KEY_BLOOD_TYPE, bloodType);
                    reference.updateChildren(update)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        MemorySupports.setUserInfo(mContext, KEY_BLOOD_DONATE,
                                                MemorySupports.getUserInfo(mContext, KEY_ID));
                                        MemorySupports.setUserInfo(mContext, KEY_BLOOD_TYPE, bloodType);
                                        dialog.cancel();
                                        openLoadingFragment();
                                    }
                                }
                            });
                } else {
                    dialog.cancel();
                    Toast.makeText(mContext, "Failed to Join", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                dialog.cancel();
                Toast.makeText(mContext, "Something went to wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onProcessLoadingComplete() {
        finish();
    }
}
