package co.techmagic.hr.presentation.pojo

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class UserReportViewModel(
        val id: String,
        val client: String,
        val project: String,
        val lockDate: Boolean,
        val task: ReportNameViewModel,
        val note: String,
        var minutes: Int,
        var isCurrentlyTracking: Boolean,
        val isLocked: Boolean,
        val weekReportId: String,
        val status: String,
        val date: Date
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            1 == source.readInt(),
            source.readParcelable<ReportNameViewModel>(ReportNameViewModel::class.java.classLoader),
            source.readString(),
            source.readInt(),
            1 == source.readInt(),
            1 == source.readInt(),
            source.readString(),
            source.readString(),
            source.readSerializable() as Date
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(client)
        writeString(project)
        writeInt((if (lockDate) 1 else 0))
        writeParcelable(task, 0)
        writeString(note)
        writeInt(minutes)
        writeInt((if (isCurrentlyTracking) 1 else 0))
        writeInt((if (isLocked) 1 else 0))
        writeString(weekReportId)
        writeString(status)
        writeSerializable(date)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<UserReportViewModel> = object : Parcelable.Creator<UserReportViewModel> {
            override fun createFromParcel(source: Parcel): UserReportViewModel = UserReportViewModel(source)
            override fun newArray(size: Int): Array<UserReportViewModel?> = arrayOfNulls(size)
        }
    }
}