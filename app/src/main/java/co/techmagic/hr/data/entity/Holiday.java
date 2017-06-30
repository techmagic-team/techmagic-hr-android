package co.techmagic.hr.data.entity;

import com.google.gson.annotations.SerializedName;

public class Holiday {

    @SerializedName("date")
    private int date;

    @SerializedName("name")
    private String name;

    public int getDate() {
        return date;
    }

    public String getName() {
        return name;
    }
}
