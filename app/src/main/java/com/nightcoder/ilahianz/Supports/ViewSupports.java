package com.nightcoder.ilahianz.Supports;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.transition.Fade;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.nightcoder.ilahianz.MyApp;
import com.nightcoder.ilahianz.R;

public class ViewSupports {

    public static Dialog materialDialog(Context context, int id) {
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

    public static Dialog materialLoadingDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.material_loading_dialog);
        Window window = dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.DialogAnimation);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        return dialog;
    }

    public static void materialErrorDialog(Context context, String error, String heading) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_error);
        Window window = dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.ErrorDialogAnimation);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        ((TextView) dialog.findViewById(R.id.error_message)).setText(error);
        ((TextView) dialog.findViewById(R.id.heading)).setText(heading);
        dialog.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    public static Dialog materialSignOutDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_sign_out_confirm);
        Window window = dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.ErrorDialogAnimation);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        return dialog;
    }

    public static Dialog materialLoadingDialog(Context context, String content) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.material_loading_dialog);
        Window window = dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.DialogAnimation);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        ((TextView) dialog.findViewById(R.id.text_content)).setText(content);
        return dialog;
    }

    public static void materialSnackBar(Context context, String content, long duration, int icon) {
        Activity activity = ((MyApp) context.getApplicationContext()).getCurrentActivity();
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.custom_snack_bar);
        Window window = dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.SnackBarAnimation);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        ((TextView) dialog.findViewById(R.id.message)).setText(content);
        ((ImageView) dialog.findViewById(R.id.icon)).setImageResource(icon);
        dialog.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.cancel();
            }
        }, duration);
    }

    public static void materialLoadingSnackBar(Context context, String content, long duration) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_loading_snack_bar);
        Window window = dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.SnackBarAnimation);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        ((TextView) dialog.findViewById(R.id.message)).setText(content);
        dialog.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.cancel();
            }
        }, duration);
    }

//    public static synchronized void visibilityFadeAnimation(long duration, int target, ViewGroup parent, int visibility) {
//        Transition transition = new Fade();
//        transition.setDuration(duration);
//        transition.addTarget(target);
//        TransitionManager.beginDelayedTransition(parent, transition);
//        parent.findViewById(target).setVisibility(visibility);
//    }

    public static synchronized void visibilityFadeAnimation(long duration, View target, ViewGroup parent, int visibility) {
        Transition transition = new Fade();
        transition.setDuration(duration);
        transition.addTarget(target);
        TransitionManager.beginDelayedTransition(parent, transition);
        target.setVisibility(visibility);
    }

    public static synchronized void visibilitySlideAnimation(int gravity, long duration, View target, ViewGroup parent, int visibility) {
        Transition transition = new Slide(gravity);
        transition.setDuration(duration);
        transition.addTarget(target);
        TransitionManager.beginDelayedTransition(parent, transition);
        target.setVisibility(visibility);
    }
}
