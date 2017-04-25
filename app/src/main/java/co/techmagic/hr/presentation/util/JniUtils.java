package co.techmagic.hr.presentation.util;

/**
 * Created by techmagic on 3/27/17.
 */

public class JniUtils {
    static JniUtils instance;

    static {
        System.loadLibrary("keys");
    }

    public static JniUtils getInstance() {
        if (instance == null) {
            instance = new JniUtils();
        }
        return instance;
    }

    public native String getKey();
    public native String getInitializationVector();
}
