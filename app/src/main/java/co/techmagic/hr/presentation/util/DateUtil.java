package co.techmagic.hr.presentation.util;

import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {


    public static String getFormattedDate(@Nullable String inputData) {
        String formattedDate = null;

        if (inputData == null) {
            return formattedDate;
        }

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            Date date = inputFormat.parse(inputData);
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM d, yyyy", Locale.US);
            formattedDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }
}
