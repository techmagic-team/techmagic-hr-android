package co.techmagic.hr.domain.pojo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.techmagic.hr.common.TimeOffType;

/**
 * Created by roman on 6/13/17
 */

public class RemainedTimeOffsAmountDto {
    private Map<TimeOffType, Integer> map = Collections.synchronizedMap(new HashMap<>());
    private List<Calendar> holidays = new ArrayList<>();

    public Map<TimeOffType, Integer> getMap() {
        return map;
    }

    public void setMap(Map<TimeOffType, Integer> map) {
        this.map = map;
    }

    public List<Calendar> getHolidays() {
        return holidays;
    }
}
