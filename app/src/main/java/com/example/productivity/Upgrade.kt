package com.example.productivity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class Upgrade(
    var name: String = "Default name",
    var description: String = "Default desc",
    var image: String = "Default image"
) : Parcelable, Comparable<Avatar> {
    override fun compareTo(other: Avatar): Int {
        return this.name.compareTo(other.name)
    }
}