package co.techmagic.hr.presentation.mvp.view;

import android.support.annotation.NonNull;

import java.util.List;

import co.techmagic.hr.data.entity.Company;
import co.techmagic.hr.data.entity.User;

public interface LoginView extends View {

    void showEmptyEmailError();

    void onEmailError();

    void hideEmailError();

    void showShortPasswordMessage();

    void setPasswordToggleEnabled(boolean b);

    void onPasswordError();

    void hidePasswordError();

    void onForgotPassEmailError();

    void showEmptyForgotPassEmailError();

    void hideForgotPassEmailError();

    void onLoginSuccess(@NonNull User user);

    void onForgotPasswordRequestSent();

    void showCompanySelectionDialogView(@NonNull List<Company> companyList);

    void onCompanyError();

    void updateSelectedCompanyView(String name);
}
