package co.techmagic.hr.data.db;

import co.techmagic.hr.data.entity.Department;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DepartmentEntry extends RealmObject {

    @PrimaryKey
    private String id;
    private String name;

    DepartmentEntry(final Department department) {
        if (department == null) {
            return;
        }

        id = department.getId();
        name = department.getName();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
