package com.example.productivity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
class SleepTracker (
    var journal: String = "Default journal entry",
    var date: String = "2000-0-0",
    var time: String = "Default time",
    var bedtime: String = "Default bedtime",
    var ownerId: String? = null,
    var objectId: String? = null
): Parcelable {
}