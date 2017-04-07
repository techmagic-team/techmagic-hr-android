package co.techmagic.hr.data.request;


public class GetMyProfileRequest {

    private String userId;

    public GetMyProfileRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
