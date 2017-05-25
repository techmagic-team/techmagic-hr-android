package co.techmagic.hr.presentation.pojo;

import java.util.LinkedHashMap;
import java.util.List;

import co.techmagic.hr.data.entity.Docs;

/**
 * Created by Roman Ursu on 5/12/17
 */

public class UserAllTimeOffsMap {

    private LinkedHashMap<Docs, List<UserTimeOff>> map = new LinkedHashMap<>();
    private LinkedHashMap<Docs, List<UserTimeOff>> requestedMap = new LinkedHashMap<>();

    public LinkedHashMap<Docs, List<UserTimeOff>> getMap() {
        return map;
    }

    public LinkedHashMap<Docs, List<UserTimeOff>> getRequestedMap() {
        return requestedMap;
    }

    public boolean isEmpty() {
        return map.isEmpty() && requestedMap.isEmpty();
    }
}