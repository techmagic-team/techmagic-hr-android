package co.techmagic.hr.data.request;

import com.google.gson.annotations.SerializedName;

public class DepartmentRequest {

    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    public DepartmentRequest(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "id='" + id + '\'' +
                ", name='" + name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}