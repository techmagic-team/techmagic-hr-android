package co.techmagic.hr.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by techmagic on 5/29/17.
 */

public class Company implements IFilterModel {

    @SerializedName("_id")
    String id;

    @SerializedName("name")
    String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
