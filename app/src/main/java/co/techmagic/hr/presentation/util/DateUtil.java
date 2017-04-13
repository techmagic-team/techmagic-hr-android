package co.techmagic.hr.presentation.util;

import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {

    private static SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
    private static SimpleDateFormat outputFormat = new SimpleDateFormat("MMM d, yyyy", Locale.US);


    public static String getFormattedDate(@Nullable String inputData) {
        String formattedDate = null;

        if (inputData == null) {
            return formattedDate;
        }

        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            Date date = inputFormat.parse(inputData);
            formattedDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }


    public static long getFirstWorkingDayInMillis(@Nullable String firstDayDate) {
        long dayInMillis = 0;

        if (firstDayDate == null) {
            return dayInMillis;
        }

        try {
            Date date = inputFormat.parse(firstDayDate);
            dayInMillis = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dayInMillis;
    }


    public static long getDateAfterYearInMillis(long currentDate) {
        long dayAfterYear = 0;

        if (currentDate == 0) {
            return dayAfterYear;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(currentDate);
        cal.add(Calendar.YEAR, 1);

        return cal.getTimeInMillis();
    }
}
