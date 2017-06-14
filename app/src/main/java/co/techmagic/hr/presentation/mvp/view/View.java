package co.techmagic.hr.presentation.mvp.view;

public interface View {

    void showMessage(String message);

    void showConnectionErrorMessage();

    void showMessage(int messageResId);

    void showSnackBarMessage(String message);

    void showSnackBarMessage(int messageResId);

    void showSnackBarWrongLoginCredentialsError();

    void showProgress();

    void showProgress(String message);

    void showProgress(int messageResId);

    void showProgress(String message, String title);

    void showProgress(int messageResId, int titleResId);

    void hideProgress();

    void logOut();
}
