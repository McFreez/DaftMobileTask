package com.daftmobile.daftmobiletask

import android.os.Parcel
import android.os.Parcelable

class Item() : Parcelable {

    var mColor: Color = Color.RED
    var mNumber: Int = 0

    constructor(parcel: Parcel) : this() {
        val color = parcel.readString()
        mColor = if(color == "red")
            Color.RED
        else
            Color.BLUE
        mNumber = parcel.readInt()

    }

    constructor(color: Color, number: Int) : this() {
        mColor = color
        mNumber = number
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        if(mColor == Color.RED)
            dest?.writeString("red")
        else
            dest?.writeString("blue")
        dest?.writeInt(mNumber)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }
}

enum class Color{
    RED, BLUE
}
