package co.techmagic.hr.data.request;

import com.google.gson.annotations.SerializedName;

public class ReasonRequest {

    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    public ReasonRequest(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}