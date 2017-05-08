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


    public static Date getFormattedFullDate(@Nullable Date inputData) {
        Date formattedDate = null;

        if (inputData == null) {
            return formattedDate;
        }

        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            String date = outputFullDateFormat.format(inputData);
            formattedDate = inputFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

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

        return inputDate.after(start) && inputDate.before(end);
    }


    public static boolean isToday(@Nullable Calendar time) {
        if (time == null) {
            return false;
        }

        Calendar now = Calendar.getInstance();
        if (time.get(Calendar.YEAR) != now.get(Calendar.YEAR))
            return false;
        if (time.get(Calendar.MONTH) != now.get(Calendar.MONTH))
            return false;
        if (time.get(Calendar.DAY_OF_MONTH) != now.get(Calendar.DAY_OF_MONTH))
            return false;

        return true;
    }


    /**
     * Compare two dates, and check if they are the same.
     * Only checks year, month, day.
     *
     * @return TRUE if the dates are the same.
     */

    public static boolean compareDates(@Nullable Calendar left, @Nullable Calendar right) {
        if (left == null || right == null) {
            return false;
        }

        if (left.get(Calendar.YEAR) != right.get(Calendar.YEAR))
            return false;
        if (left.get(Calendar.MONTH) != right.get(Calendar.MONTH))
            return false;
        if (left.get(Calendar.DAY_OF_MONTH) != right.get(Calendar.DAY_OF_MONTH))
            return false;

        return true;
    }
}