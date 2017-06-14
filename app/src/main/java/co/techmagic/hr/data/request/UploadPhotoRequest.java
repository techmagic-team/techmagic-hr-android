package co.techmagic.hr.data.request;


import okhttp3.MultipartBody;

public class UploadPhotoRequest {

    private String userId;
    private MultipartBody.Part multipartBody;

    public UploadPhotoRequest(String userId, MultipartBody.Part multipartBody) {
        this.userId = userId;
        this.multipartBody = multipartBody;
    }

    public String getUserId() {
        return userId;
    }

    public MultipartBody.Part getMultipartBody() {
        return multipartBody;
    }
}
