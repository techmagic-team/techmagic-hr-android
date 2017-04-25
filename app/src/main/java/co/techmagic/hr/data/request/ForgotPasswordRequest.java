package co.techmagic.hr.data.request;


import com.google.gson.annotations.SerializedName;

public class ForgotPasswordRequest {

    @SerializedName("_company")
    private String company;

    @SerializedName("email")
    private String email;

    public ForgotPasswordRequest(String company, String email) {
        this.company = company;
        this.email = email;
    }

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
}
