package com.example.productivity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class Avatar(
    var name: String = "Default name",
    var title: String = "Default title",
    var description: String = "Default desc",
    var image: String = "Default image"
) : Parcelable, Comparable<Avatar> {
    override fun compareTo(other: Avatar): Int {
        return this.name.compareTo(other.name)
    }
}