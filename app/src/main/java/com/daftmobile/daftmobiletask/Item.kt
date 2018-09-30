package com.daftmobile.daftmobiletask

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Item(val color: Color, var number: Int) : Parcelable

enum class Color{
    RED, BLUE
}
