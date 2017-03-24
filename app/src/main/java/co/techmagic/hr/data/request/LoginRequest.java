package co.techmagic.hr.data.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by techmagic on 3/24/17.
 */

public class LoginRequest {

    @SerializedName("_company")
    private String company;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
