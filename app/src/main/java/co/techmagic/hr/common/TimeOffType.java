package co.techmagic.hr.common;

import android.content.res.Resources;
import android.text.TextUtils;

import org.jetbrains.annotations.Nullable;

import co.techmagic.hr.R;

/**
 * Created by Roman Ursu on 5/12/17
 */

public enum TimeOffType {
    VACATION(R.string.tm_hr_vacation_time_off_name),
    DAYOFF(R.string.tm_hr_dayoff_time_off_name),
    ILLNESS(R.string.tm_hr_illness_time_off_name),
    REQUESTED(R.string.tm_hr_requested);

    private int displayNameId;

    TimeOffType(int displayNameId) {
        this.displayNameId = displayNameId;
    }

    public int getDisplayNameId() {
        return displayNameId;
    }

    @Nullable
    public static TimeOffType getType(Resources resources, String name) {
        for (TimeOffType timeOffType : TimeOffType.values()) {
            String displayName = resources.getString(timeOffType.displayNameId);

            if (TextUtils.equals(displayName, name)) {
                return timeOffType;
            }
        }

        return null;
    }
}
