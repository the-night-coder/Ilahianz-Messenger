package com.nightcoder.ilahianz.Supports;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.transition.Fade;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.nightcoder.ilahianz.R;

public class ViewSupports {

    private static Dialog dialog;

    public static void showNoConnection(Context context) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.no_internet_connection);
        Button ok = dialog.findViewById(R.id.ok_action);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        Window window = dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    public static Dialog materialDialog(Context context, int id){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(id);
        Window window = dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.DialogAnimation);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        return dialog;
    }

    public static Dialog materialDialog(Context context, int gravity, int id) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(id);
        Window window = dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(gravity);
        window.setWindowAnimations(R.style.DialogAnimation);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        return dialog;
    }

    public static synchronized void visibilityFadeAnimation(long duration, int target, ViewGroup parent, int visibility){
        Transition transition = new Fade();
        transition.setDuration(duration);
        transition.addTarget(target);
        TransitionManager.beginDelayedTransition(parent, transition);
        parent.findViewById(target).setVisibility(visibility);
    }
    public static synchronized void visibilityFadeAnimation(long duration, View target, ViewGroup parent, int visibility){
        Transition transition = new Fade();
        transition.setDuration(duration);
        transition.addTarget(target);
        TransitionManager.beginDelayedTransition(parent, transition);
        target.setVisibility(visibility);
    }
    public static synchronized void visibilitySlideAnimation(int gravity, long duration, View target, ViewGroup parent, int visibility){
        Transition transition = new Slide(gravity);
        transition.setDuration(duration);
        transition.addTarget(target);
        TransitionManager.beginDelayedTransition(parent, transition);
        target.setVisibility(visibility);
    }
//    public static void visibilitySlideAnimation(int gravity, long duration, View target, Context parent, int visibility){
//        Transition transition = new Slide(gravity);
//        transition.setDuration(duration);
//        transition.addTarget(target);
//        TransitionManager.beginDelayedTransition(, transition);
//        target.setVisibility(visibility);
//    }
}
