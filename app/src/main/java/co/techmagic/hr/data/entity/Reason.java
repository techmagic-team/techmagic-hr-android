package co.techmagic.hr.data.entity;

import com.google.gson.annotations.SerializedName;

public class Reason  {

    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}