package co.techmagic.hr.presentation.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.techmagic.hr.data.entity.Docs;

/**
 * Created by Roman Ursu on 5/12/17
 */

public class UserAllTimeOffsMap {

    private Map<Docs, List<UserTimeOff>> map = new HashMap<>();

    private Map<Docs, List<UserTimeOff>> requestedMap = new HashMap<>();

    public Map<Docs, List<UserTimeOff>> getMap() {
        return map;
    }

    public Map<Docs, List<UserTimeOff>> getRequestedMap() {
        return requestedMap;
    }

    public boolean isEmpty() {
        return map.isEmpty() && requestedMap.isEmpty();
    }
}