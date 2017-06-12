package co.techmagic.hr.domain.pojo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import co.techmagic.hr.common.TimeOffType;

/**
 * Created by roman on 6/13/17.
 */

public class RemainedTimeOffsAmountDto {
    private Map<TimeOffType, Integer> map = Collections.synchronizedMap(new HashMap<>());

    public Map<TimeOffType, Integer> getMap() {
        return map;
    }

    public void setMap(Map<TimeOffType, Integer> map) {
        this.map = map;
    }
}
