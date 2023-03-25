package com.example.note_homework

import android.os.Parcel
import android.os.Parcelable

class NoteModel (var id: Int? = 0,
                 var name : String?=null,
                 var content:String? = null,
                 var letters:Int?=0,
                 var image:String?=null,
                 var catId: Int?=0) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(content)
        parcel.writeValue(letters)
        parcel.writeString(image)
        parcel.writeValue(catId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NoteModel> {
        override fun createFromParcel(parcel: Parcel): NoteModel {
            return NoteModel(parcel)
        }

        override fun newArray(size: Int): Array<NoteModel?> {
            return arrayOfNulls(size)
        }
    }
}