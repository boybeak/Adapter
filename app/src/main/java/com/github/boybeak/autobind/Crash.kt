package com.github.boybeak.autobind

import android.os.Parcel
import android.os.Parcelable
import android.support.v4.text.HtmlCompat
import android.text.style.StyleSpan
import java.util.regex.Pattern

class Crash() : Parcelable {

    var sdk_int: Int = 0
    var id: String? = null
    var incermental: String? = null
    var model: String? = null
    var simple_crash_info: String? = null
    var manufacturer: String? = null
    var code_name: String? = null
    var app_version: String? = null
    var role: Int? = null
    var admin_id: Int = 0
    var name: String? = null
    var crash_info: String? = null
    var crash_time: String? = null
    var studio_id: String? = null
    var studio_name: String? = null

    val causeBy: String
        get() = "Cause by:$simple_crash_info".trim()

    val manufacturerAndModel: String
        get() = "$manufacturer / $model"

    val nameWithRole: String
        get() = "$name · ${when(role){1->"T" 2->"S" 3->"F" else -> "Unknown"}}"

    fun formattedCrashInfo(): CharSequence {
        if (crash_info == null) {
            return ""
        }

        return HtmlCompat.fromHtml(
                crash_info!!.trim().replace("\n", "<p>").replace(":", ":<b>")
                        .replace(")", "</b>)"), HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

    constructor(parcel: Parcel) : this() {
        sdk_int = parcel.readInt()
        id = parcel.readString()
        incermental = parcel.readString()
        model = parcel.readString()
        simple_crash_info = parcel.readString()
        manufacturer = parcel.readString()
        code_name = parcel.readString()
        app_version = parcel.readString()
        role = parcel.readValue(Int::class.java.classLoader) as? Int
        admin_id = parcel.readInt()
        name = parcel.readString()
        crash_info = parcel.readString()
        crash_time = parcel.readString()
        studio_id = parcel.readString()
        studio_name = parcel.readString()
    }

    fun sdk(): String {
        return sdk_int.toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(sdk_int)
        parcel.writeString(id)
        parcel.writeString(incermental)
        parcel.writeString(model)
        parcel.writeString(simple_crash_info)
        parcel.writeString(manufacturer)
        parcel.writeString(code_name)
        parcel.writeString(app_version)
        parcel.writeValue(role)
        parcel.writeInt(admin_id)
        parcel.writeString(name)
        parcel.writeString(crash_info)
        parcel.writeString(crash_time)
        parcel.writeString(studio_id)
        parcel.writeString(studio_name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Crash> {
        override fun createFromParcel(parcel: Parcel): Crash {
            return Crash(parcel)
        }

        override fun newArray(size: Int): Array<Crash?> {
            return arrayOfNulls(size)
        }
    }

}