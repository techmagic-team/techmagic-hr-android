package co.techmagic.hr.data.entity

import android.os.Parcel
import android.os.Parcelable
import co.techmagic.hr.data.entity.time_report.Delivery
import com.google.gson.annotations.SerializedName

//TODO: try @Parselize annotation. It is considered stable as of version 1.3.30
class UserProfile : Parcelable {

    @Deprecated("")
    @SerializedName("_department")
    private var departmentId: String? = null

    @SerializedName("_delivery")
    private val delivery: Delivery? = null

    @SerializedName("_room")
    var room: Room? = null

    @SerializedName("_lead")
    var lead: Lead? = null

    @SerializedName("_reason")
    var reason: Reason? = null

    @SerializedName("_id")
    var id: String? = null

    @SerializedName("_company")
    var company: String? = null

    @SerializedName("birthday")
    var birthday: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("emergencyContact")
    var emergencyContact: EmergencyContact? = null

    @SerializedName("firstName")
    var firstName: String? = null

    @SerializedName("lastName")
    var lastName: String? = null

    @SerializedName("firstWorkingDay")
    var firstWorkingDay: String? = null

    @SerializedName("generalFirstWorkingDay")
    var generalFirstWorkingDay: String? = null

    @SerializedName("lastWorkingDay")
    var lastWorkingDay: String? = null

    @SerializedName("gender")
    var gender: Int = 0

    @SerializedName("phone")
    var phone: String? = null

    @SerializedName("photo")
    var photo: String? = null

    @SerializedName("photoOrigin")
    var photoOrigin: String? = null

    @SerializedName("relocationCity")
    var relocationCity: String? = null

    @SerializedName("role")
    var role: Int = 0

    @SerializedName("skype")
    var skype: String? = null

    @SerializedName("isActive")
    var isActive: Boolean = false

    @SerializedName("trialPeriodEnds")
    var trialPeriodEnds: String? = null

    @SerializedName("_pdp")
    var pdpLink: String? = null

    @SerializedName("_oneToOne")
    var oneToOneLink: String? = null

    @SerializedName("reason_comments")
    var reasonComments: String? = null

    var password: String? = null

    @Deprecated("Quick fix due to unexpectedly changed UserProfile model on the backend. User Delivery instead.")
    var department: Department
        get() = Department(delivery!!.id, String.format("%s (%s %s)", delivery!!.name, delivery!!.manager.firstName, delivery!!.manager.lastName))
        set(department) {
            //FIXME: update Department feature is broken. Should be changed to Delivery
            this.departmentId = department.id
        }

    constructor() {}

    protected constructor(`in`: Parcel) {
        departmentId = `in`.readString()
        room = `in`.readValue(Room::class.java.classLoader) as Room
        lead = `in`.readValue(Lead::class.java.classLoader) as Lead
        reason = `in`.readValue(Reason::class.java.classLoader) as Reason
        id = `in`.readString()
        company = `in`.readString()
        birthday = `in`.readString()
        description = `in`.readString()
        email = `in`.readString()
        emergencyContact = `in`.readValue(EmergencyContact::class.java.classLoader) as EmergencyContact
        firstName = `in`.readString()
        lastName = `in`.readString()
        firstWorkingDay = `in`.readString()
        generalFirstWorkingDay = `in`.readString()
        lastWorkingDay = `in`.readString()
        gender = `in`.readInt()
        phone = `in`.readString()
        photo = `in`.readString()
        photoOrigin = `in`.readString()
        relocationCity = `in`.readString()
        role = `in`.readInt()
        skype = `in`.readString()
        isActive = `in`.readByte().toInt() != 0x00
        trialPeriodEnds = `in`.readString()
        pdpLink = `in`.readString()
        oneToOneLink = `in`.readString()
        reasonComments = `in`.readString()
        password = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(departmentId)
        dest.writeValue(room)
        dest.writeValue(lead)
        dest.writeValue(reason)
        dest.writeString(id)
        dest.writeString(company)
        dest.writeString(birthday)
        dest.writeString(description)
        dest.writeString(email)
        dest.writeValue(emergencyContact)
        dest.writeString(firstName)
        dest.writeString(lastName)
        dest.writeString(firstWorkingDay)
        dest.writeString(generalFirstWorkingDay)
        dest.writeString(lastWorkingDay)
        dest.writeInt(gender)
        dest.writeString(phone)
        dest.writeString(photo)
        dest.writeString(photoOrigin)
        dest.writeString(relocationCity)
        dest.writeInt(role)
        dest.writeString(skype)
        dest.writeByte((if (isActive) 0x01 else 0x00).toByte())
        dest.writeString(trialPeriodEnds)
        dest.writeString(pdpLink)
        dest.writeString(oneToOneLink)
        dest.writeString(reasonComments)
        dest.writeString(password)
    }

    companion object CREATOR : Parcelable.Creator<UserProfile> {
        override fun createFromParcel(parcel: Parcel): UserProfile {
            return UserProfile(parcel)
        }

        override fun newArray(size: Int): Array<UserProfile?> {
            return arrayOfNulls(size)
        }
    }
}