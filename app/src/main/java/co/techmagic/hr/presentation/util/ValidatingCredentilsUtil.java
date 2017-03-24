package co.techmagic.hr.presentation.util;

import java.util.regex.Pattern;

public class ValidatingCredentilsUtil {

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final int PASSWORD_MINIMUM_LENGTH = 6;
    private static final int PASSWORD_MAXIMUM_LENGTH = 25;


    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= PASSWORD_MINIMUM_LENGTH && password.length() <= PASSWORD_MAXIMUM_LENGTH && !password.contains(" ");
    }


    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(email).matches();
    }
}
