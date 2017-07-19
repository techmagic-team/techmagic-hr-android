package co.techmagic.hr.data.db;

import co.techmagic.hr.data.entity.Lead;
import io.realm.annotations.PrimaryKey;

public class LeadEntry {

    @PrimaryKey
    private String id;
    private String lastWorkingDay;
    private String firstName;
    private String lastName;

    LeadEntry(final Lead lead) {
        if (lead == null) {
            return;
        }

        id = lead.getId();
        lastWorkingDay = lead.getLastWorkingDay();
        firstName = lead.getFirstName();
        lastName = lead.getLastName();
    }

    public String getId() {
        return id;
    }

    public String getLastWorkingDay() {
        return lastWorkingDay;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}