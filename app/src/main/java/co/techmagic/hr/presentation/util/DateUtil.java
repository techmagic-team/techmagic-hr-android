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
    private static SimpleDateFormat outputFullDateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.US);
    private static SimpleDateFormat outputMonthAndDayFormat = new SimpleDateFormat("MMM d", Locale.US);
    private static SimpleDateFormat outputMonthAndYearFormat = new SimpleDateFormat("MMM yyyy", Locale.US);


    public static String getFormattedFullDate(@Nullable String inputData) {
        String formattedDate = null;

        if (inputData == null) {
            return formattedDate;
        }

        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            Date date = inputFormat.parse(inputData);
            formattedDate = outputFullDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }


    public static String getFormattedFullDate(@Nullable Date inputData) {
        String formattedDate = null;

        if (inputData == null) {
            return formattedDate;
        }

        formattedDate = outputFullDateFormat.format(inputData);

        return formattedDate;
    }


    public static String getFormattedMonthAndDay(@Nullable String inputData) {
        String formattedDate = null;

        if (inputData == null) {
            return formattedDate;
        }

        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            Date date = inputFormat.parse(inputData);
            formattedDate = outputMonthAndDayFormat.format(date);
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


    public static long getDateAfterYearInMillis() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 1);

        return cal.getTimeInMillis();
    }


    public static String getFormattedMonthAndYear(@Nullable Date inputDate) {
        String formattedDate = null;

        if (inputDate == null) {
            return formattedDate;
        }

        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String date = inputFormat.format(inputDate);

        try {
            Date d = inputFormat.parse(date);
            formattedDate = outputMonthAndYearFormat.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }


    public static boolean isValidSelectedDatesRange(@Nullable Calendar from, @Nullable Calendar to) {
        if (from == null || to == null) {
            return false;
        }

        return from.get(Calendar.YEAR) <= to.get(Calendar.YEAR);
    }


    public static boolean isValidDatesRange(@Nullable Date start, @Nullable Date end, @Nullable Date inputDate) {
        if (start == null || end == null || inputDate == null) {
            return false;
        }

        return start.getTime() <= inputDate.getTime() && end.getTime() >= inputDate.getTime();
    }


    public static long calendarToMidnightMillis(@Nullable Calendar calendar) {
        if (calendar == null) {
            return 0;
        }

        Calendar c = Calendar.getInstance();
        c.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return calendar.getTimeInMillis();
    }
}