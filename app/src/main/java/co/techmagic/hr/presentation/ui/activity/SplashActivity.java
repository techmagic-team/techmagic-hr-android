package co.techmagic.hr.presentation.ui.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import co.techmagic.hr.R;

public class SplashActivity extends AppCompatActivity {

    private static final long ANIM_DURATION = 700;
    private static final long DELAY = 1400; // To show 2 animations

    private View animatedLogo;
    private ObjectAnimator animX;
    private ObjectAnimator animY;

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        animatedLogo = findViewById(R.id.animatedLogo);
        setupLogoAnimation();
        initHandler();
        initRunnableWithPostDelay();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (handler != null) {
            handler.removeCallbacks(runnable);
            handler = null;
        }

        animX.cancel();
        animY.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        animX.start();
        initHandler();

        if (runnable == null) {
            initRunnableWithPostDelay();
        } else {
            handler.postDelayed(runnable, DELAY);
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

    private void initHandler() {
        if (handler == null) {
            handler = new Handler();
        }
    }

    private void initRunnableWithPostDelay() {
        if (runnable == null) {
            runnable = () -> {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            };
        }
        handler.postDelayed(runnable, DELAY);
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