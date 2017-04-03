package co.techmagic.hr.presentation.ui.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import co.techmagic.hr.R;

public class AnimatedProgressDialog extends Dialog {

    private static final long ANIM_DURATION = 500;
    private View animatedLogo;
    private TextView tvMessage;
    private ObjectAnimator animX;
    private ObjectAnimator animY;


    public AnimatedProgressDialog(Context context) {
        super(context);
        setupDialogView(context);
    }


    @Override
    public void show() {
        super.show();
        animX.start();
    }


    @Override
    public void hide() {
        if (isShowing()) {
            animX.cancel();
            animY.cancel();
            dismiss();
        }
    }


    private void setupDialogView(Context context) {
        Window window = getWindow();
        WindowManager.LayoutParams wmlp = window.getAttributes();
        wmlp.gravity = Gravity.CENTER_HORIZONTAL;
        window.setAttributes(wmlp);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        setCancelable(false);
        setOnCancelListener(null);

        setupUi(context);
        setupLogoAnimation();
    }


    private void setupUi(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_animated_logo, null);
        animatedLogo = view.findViewById(R.id.logo);
        tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        setContentView(view);
    }


    public void setMessage(@Nullable String message) {
        tvMessage.setText("");
        if (message == null || message.isEmpty()) {
            tvMessage.setVisibility(View.GONE);
        } else {
            tvMessage.setVisibility(View.VISIBLE);
            tvMessage.setText(message);
        }
    }


    private void setupLogoAnimation() {
        animX = ObjectAnimator.ofFloat(animatedLogo, "rotationX", 180f, 0f);
        animY = ObjectAnimator.ofFloat(animatedLogo, "rotationY", 180f, 0f);

        animX.setDuration(ANIM_DURATION);
        animY.setDuration(ANIM_DURATION);

        animX.setRepeatCount(ObjectAnimator.INFINITE);
        animX.setRepeatMode(ObjectAnimator.RESTART);
        animY.setRepeatCount(ObjectAnimator.INFINITE);
        animY.setRepeatMode(ObjectAnimator.RESTART);

        animX.addListener(getAnimationByXListener());
        animY.addListener(getAnimationByYListener());
    }


    private Animator.AnimatorListener getAnimationByXListener() {
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                animX.cancel();
                animY.start();
            }
        };
    }


    private Animator.AnimatorListener getAnimationByYListener() {
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                animY.cancel();
                animX.start();
            }
        };
    }
}