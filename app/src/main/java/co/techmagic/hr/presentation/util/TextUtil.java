package co.techmagic.hr.presentation.util;

import java.util.regex.Pattern;

public class TextUtil {

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String URL_PATTERN = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";
    private static final int PASSWORD_MINIMUM_LENGTH = 6;
    private static final int PASSWORD_MAXIMUM_LENGTH = 25;


    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= PASSWORD_MINIMUM_LENGTH && password.length() <= PASSWORD_MAXIMUM_LENGTH && !password.contains(" ");
    }


    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(email).matches();
    }


    public static boolean containsValidUrl(String text) {
        Pattern pattern = Pattern.compile(URL_PATTERN);
        return pattern.matcher(text).find();
    }


    public static String getFormattedText(String rawText) {
        String fmtMsg = rawText.replaceAll("(?m)(^ *| +(?= |$))", "").replaceAll("(?m)^$([\r\n]+?)(^$[\r\n]+?^)+", "$1");
        return fmtMsg.replace("\n\n", "\n");
    }
}
