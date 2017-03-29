package co.techmagic.hr.data.entity;

import com.google.gson.annotations.SerializedName;

public class EmergencyContact {

    @SerializedName("name")
    private String name;

    @SerializedName("phone")
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
