package futureconstruction.domain

import java.io.File

data class ImageSet(
    val band2: File,
    val band3: File,
    val band4: File,
    val band5: File,
    val band6: File,
    val band7: File,
    val band9: File
) {

    fun isValidFor(mode: Mode) =
        when (mode) {
            Mode.VISIBLE -> listOf(band2, band3, band4).all { it.exists() }
            Mode.VEGETATION -> listOf(band5, band6, band7).all { it.exists() }
            Mode.WATER_VAPOR -> band9.exists()
        }
}