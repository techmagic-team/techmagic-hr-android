package co.techmagic.hr.domain.pojo;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.techmagic.hr.common.TimeOffType;

/**
 * Created by Roman Ursu on 5/12/17
 */

public class AllTimeOffsDto {
    private Map<TimeOffType, List<RequestedTimeOffDto>> map = Collections.synchronizedMap(new HashMap<>());
    private List<CalendarInfoDto> calendarInfo;

    public Map<TimeOffType, List<RequestedTimeOffDto>> getMap() {
        return map;
    }

    public List<CalendarInfoDto> getCalendarInfo() {
        return calendarInfo;
    }

    public void setCalendarInfo(List<CalendarInfoDto> calendarInfo) {
        this.calendarInfo = calendarInfo;
    }
}
