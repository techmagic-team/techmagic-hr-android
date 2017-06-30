package co.techmagic.hr.data.entity;


public class EmployeeGridYitem {

    private String id;
    private String name;
    private String photoUrl;


    public EmployeeGridYitem(String id, String name, String photoUrl) {
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
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


    public String getPhotoUrl() {
        return photoUrl;
    }

}