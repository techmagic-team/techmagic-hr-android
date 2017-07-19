package co.techmagic.hr.data.db;


import co.techmagic.hr.data.entity.EmergencyContact;

public class EmergencyEntry {

    private String name;
    private String phone;

    EmergencyEntry(EmergencyContact emergencyContact) {
        if (emergencyContact == null) {
            return;
        }

        name = emergencyContact.getName();
        phone = emergencyContact.getPhone();
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
