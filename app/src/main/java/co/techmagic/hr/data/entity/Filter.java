package co.techmagic.hr.data.entity;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class Filter implements IFilterModel {

    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Nullable
    @Override
    public String getLastWorkingDay() {
        return null;
    }

    @Nullable
    @Override
    public String getFirstName() {
        return null;
    }

    @Nullable
    @Override
    public String getLastName() {
        return null;
    }
}
