package co.techmagic.hr.data.entity;

import com.google.gson.annotations.SerializedName;

public class Reason {

    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    public Reason() {}

    public Reason(String id, String name) {
        this.id = id;
        this.name = name;
    }

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