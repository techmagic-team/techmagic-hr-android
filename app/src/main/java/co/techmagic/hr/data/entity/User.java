package co.techmagic.hr.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by techmagic on 8/29/16.
 */

public class User {

    @SerializedName("accessToken")
    private String accessToken;

    @SerializedName("_department")
    private String department;

    @SerializedName("_lead")
    private String lead;

    @SerializedName("_id")
    private String id;

    @SerializedName("_company")
    private Filter company;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("firstWorkingDay")
    private String firstWorkingDay;

    @SerializedName("gender")
    private int gender;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("photo")
    private String photo;

    @SerializedName("role")
    private int role;

    @SerializedName("isActive")
    private boolean isActive;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getLead() {
        return lead;
    }

    public void setLead(String lead) {
        this.lead = lead;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Filter getCompany() {
        return company;
    }

    public void setCompany(Filter company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUsername() {
        return firstName + " " + lastName;
    }

    public String getFirstWorkingDay() {
        return firstWorkingDay;
    }

    public void setFirstWorkingDay(String firstWorkingDay) {
        this.firstWorkingDay = firstWorkingDay;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
