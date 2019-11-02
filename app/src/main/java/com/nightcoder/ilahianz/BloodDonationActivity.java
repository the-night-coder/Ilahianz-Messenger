package com.nightcoder.ilahianz;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

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
import com.nightcoder.ilahianz.Fragments.LoadingFragment;
import com.nightcoder.ilahianz.Listeners.BloodDonation.BloodDonationFormCallbacks;
import com.nightcoder.ilahianz.Supports.MemorySupports;

import java.util.HashMap;

import static com.nightcoder.ilahianz.Literals.StringConstants.BLOOD_DONATE;
import static com.nightcoder.ilahianz.Literals.StringConstants.KEY_ID;
import static com.nightcoder.ilahianz.Literals.StringConstants.LOADING_FRAGMENT_TAG;
import static com.nightcoder.ilahianz.Literals.StringConstants.SIGN_FRAGMENT_TAG;

public class BloodDonationActivity extends AppCompatActivity implements BloodDonationFormCallbacks {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_doantion);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
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

    @Override
    public void onParticipateButtonClicked(HashMap hashMap) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child(BLOOD_DONATE).child(MemorySupports.getUserInfo(mContext, KEY_ID));
        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {

            }
        });
    }
}
