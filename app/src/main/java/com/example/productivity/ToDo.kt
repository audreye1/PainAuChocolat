package com.example.productivity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
class ToDo (
    var name: String = "Default name",
    var length: String = EMOJI.SHORT.name,
    var ownerId: String? = null,
    var objectId: String? = null
): Parcelable {
    enum class EMOJI(var emoji: String) {
        LONG("Long 🟥"),
        //🟥
        MEDIUM ("Medium " + "\uD83D\uDFE8"),
        //🟨
        SHORT ("Short " + "\uD83D\uDFE9")
        //🟩
    }
}
