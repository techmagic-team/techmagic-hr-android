package co.techmagic.hr.presentation.pojo

import android.os.Parcel
import android.os.Parcelable

data class ReportNameViewModel(val name: String) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ReportNameViewModel> = object : Parcelable.Creator<ReportNameViewModel> {
            override fun createFromParcel(source: Parcel): ReportNameViewModel = ReportNameViewModel(source)
            override fun newArray(size: Int): Array<ReportNameViewModel?> = arrayOfNulls(size)
        }
    }
}
