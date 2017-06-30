package co.techmagic.hr.common;

/**
 * Created by Roman Ursu on 6/26/17
 */

public enum Role {
    ROLE_USER(0),
    ROLE_HR(1),
    ROLE_ADMIN(2);

    int code;

    Role(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static Role getRoleByCode(int code) {
        switch (code) {
            case 0: return ROLE_USER;
            case 1: return ROLE_HR;
            case 2: return ROLE_ADMIN;
            default: return null;
        }
    }
}
