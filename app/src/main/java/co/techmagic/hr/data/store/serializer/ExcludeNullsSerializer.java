package co.techmagic.hr.data.store.serializer;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import co.techmagic.hr.data.request.EditProfileRequest;


public class ExcludeNullsSerializer implements JsonSerializer<EditProfileRequest> {


    @Override
    public JsonElement serialize(EditProfileRequest src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();

        if (src == null) {
            return jsonObject;
        }

        Gson gson = new Gson();

        jsonObject.addProperty("_id", src.getId());

        if (src.getDepartment() != null) {
            jsonObject.addProperty("_department", gson.toJson(src.getDepartment()));
        }

        if (src.getRoom() != null) {
            jsonObject.addProperty("_room", src.getRoom().toString());
        }

        if (src.getLead() != null) {
            jsonObject.addProperty("_lead", src.getLead().toString());
        }

        if (src.getReason() != null) {
            jsonObject.addProperty("_reason", src.getReason().toString());
        }

        if (src.getEmergencyContact() != null) {
            jsonObject.addProperty("emergencyContact", src.getEmergencyContact().toString());
        }

        if (src.getLastWorkingDay() != null && src.getLastWorkingDay() != 0) {
            jsonObject.addProperty("lastWorkingDay", src.getLastWorkingDay());
        }

        jsonObject.addProperty("_company", src.getCompany());
        jsonObject.addProperty("birthday", src.getBirthday());
        jsonObject.addProperty("description", src.getDescription());
        jsonObject.addProperty("email", src.getEmail());
        jsonObject.addProperty("firstName", src.getFirstName());
        jsonObject.addProperty("lastName", src.getLastName());
        jsonObject.addProperty("firstWorkingDay", src.getFirstWorkingDay());
        jsonObject.addProperty("generalFirstWorkingDay", src.getGeneralFirstWorkingDay());
        jsonObject.addProperty("lastWorkingDay", src.getLastWorkingDay());
        jsonObject.addProperty("gender", src.getGender());
        jsonObject.addProperty("phone", src.getPhone());
        jsonObject.addProperty("photo", src.getPhoto());
        jsonObject.addProperty("photoOrigin", src.getPhotoOrigin());
        jsonObject.addProperty("relocationCity", src.getRelocationCity());
        jsonObject.addProperty("role", src.getRole());
        jsonObject.addProperty("skype", src.getSkype());
        jsonObject.addProperty("isActive", src.isActive());
        jsonObject.addProperty("trialPeriodEnds", src.getTrialPeriodEnds());
        jsonObject.addProperty("_pdp", src.getPdpLink());
        jsonObject.addProperty("_oneToOne", src.getOneToOneLink());
        jsonObject.addProperty("reason_comments", src.getReasonComments());
        jsonObject.addProperty("password", src.getPassword());

        jsonObject.toString().replaceAll("\"", "");
        return jsonObject;
    }
}