package co.techmagic.hr.presentation.login;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.techmagic.viper.base.BasePresenter;

import co.techmagic.hr.R;
import co.techmagic.hr.data.repository.UserRepositoryImpl;
import co.techmagic.hr.domain.repository.IUserRepository;
import co.techmagic.hr.presentation.ui.activity.HomeActivity;
import co.techmagic.hr.presentation.ui.manager.MixpanelManager;

public class LoginActivity extends AppCompatActivity {

    protected static final String MIXPANEL_HOME_TAG = "Home";

    private MixpanelManager mixpanelManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        mixpanelManager = new MixpanelManager(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, LoginFragment.Companion.newInstance())
                    .commit();
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof LoginFragment) {
            IUserRepository userRepository = new UserRepositoryImpl();
            HrAppLoginPresenter loginPresenter = new HrAppLoginPresenter(userRepository);
            BasePresenter.Companion.bind((LoginView)fragment, loginPresenter, (LoginRouter) () -> {
                mixpanelManager.sendLoggedInUserToMixpanel();
                mixpanelManager.trackArrivedAtScreenEventIfUserExists(MIXPANEL_HOME_TAG);
                startHomeScreenWithFlags();
            });
        }
    }

    private void startHomeScreenWithFlags() {
        Bundle animation = ActivityOptions.makeCustomAnimation(this, R.anim.anim_slide_in, R.anim.anim_not_move).toBundle();
        Intent i = new Intent(this, HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i, animation);
        finish();
    }
}