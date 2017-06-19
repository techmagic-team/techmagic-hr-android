package co.techmagic.hr.presentation.pojo;

import java.util.LinkedHashMap;
import java.util.List;

import co.techmagic.hr.data.entity.UserProfile;

/**
 * Created by Roman Ursu on 5/12/17
 */

public class UserAllTimeOffsMap {

    private LinkedHashMap<UserProfile, List<UserTimeOff>> map = new LinkedHashMap<>();
    private LinkedHashMap<UserProfile, List<UserTimeOff>> requestedMap = new LinkedHashMap<>();

    public LinkedHashMap<UserProfile, List<UserTimeOff>> getMap() {
        return map;
    }

    public LinkedHashMap<UserProfile, List<UserTimeOff>> getRequestedMap() {
        return requestedMap;
    }

    public boolean isEmpty() {
        return map.isEmpty() && requestedMap.isEmpty();
    }
}