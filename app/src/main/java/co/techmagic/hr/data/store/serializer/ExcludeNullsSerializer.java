package co.techmagic.hr.data.store.serializer;

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

        jsonObject.addProperty("_id", src.getId());

        if (src.getDepartment() != null) {
            JsonObject department = new JsonObject();
            department.addProperty("_id", src.getDepartment().getId());
            department.addProperty("name", src.getDepartment().getName());

            jsonObject.add("_department", department);
        }

        if (src.getRoom() != null) {
            JsonObject room = new JsonObject();
            room.addProperty("_id", src.getRoom().getId());
            room.addProperty("name", src.getRoom().getName());

            jsonObject.add("_room", room);
        }

        if (src.getLead() != null) {
            JsonObject lead = new JsonObject();
            lead.addProperty("_id", src.getLead().getId());
            lead.addProperty("lastWorkingDay", src.getLead().getLastWorkingDay());
            lead.addProperty("firstName", src.getLead().getFirstName());
            lead.addProperty("lastName", src.getLead().getLastName());

            jsonObject.add("_lead", lead);
        }

        if (src.getReason() != null) {
            JsonObject reason = new JsonObject();
            reason.addProperty("_id", src.getReason().getId());
            reason.addProperty("name", src.getReason().getName());

            jsonObject.add("_reason", reason);
        }

        if (src.getEmergencyContact() != null) {
            JsonObject contact = new JsonObject();
            contact.addProperty("name", src.getEmergencyContact().getName());
            contact.addProperty("phone", src.getEmergencyContact().getPhone());

            jsonObject.add("emergencyContact", contact);
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

        if (src.getPassword() != null) {
            jsonObject.addProperty("password", src.getPassword());
        }

        return jsonObject;
    }
}