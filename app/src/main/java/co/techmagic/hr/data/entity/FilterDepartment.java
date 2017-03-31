package co.techmagic.hr.data.entity;

import com.google.gson.annotations.SerializedName;

public class FilterDepartment implements IFilterModel {

    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
