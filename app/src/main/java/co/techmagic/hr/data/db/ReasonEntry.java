package co.techmagic.hr.data.db;

import co.techmagic.hr.data.entity.Reason;
import io.realm.annotations.PrimaryKey;

public class ReasonEntry {

    @PrimaryKey
    private String id;
    private String name;

    ReasonEntry(final Reason reason) {
        if (reason == null) {
            return;
        }

        id = reason.getId();
        name = reason.getName();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
