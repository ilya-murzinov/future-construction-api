package futureconstruction

import futureconstruction.domain.ImageSet
import org.springframework.core.io.ClassPathResource
import java.io.File

const val prefix = "TEST_PREFIX"

val blackRectangle: File = ClassPathResource("test-images/input/black-rectangle.tiff").file
val whiteRectangle: File = ClassPathResource("test-images/input/white-rectangle.tiff").file
val blueRectangle : File = ClassPathResource("test-images/output/blue-rectangle.jpg").file
val greenRectangle : File = ClassPathResource("test-images/output/green-rectangle.jpg").file
val redRectangle : File = ClassPathResource("test-images/output/red-rectangle.jpg").file
val whiteRectangleJpg : File = ClassPathResource("test-images/output/white-rectangle.jpg").file

fun imageSet(
    band2: File = blackRectangle,
    band3: File = blackRectangle,
    band4: File = blackRectangle,
    band5: File = blackRectangle,
    band6: File = blackRectangle,
    band7: File = blackRectangle,
    band9: File = blackRectangle
) = ImageSet(
    band2,
    band3,
    band4,
    band5,
    band6,
    band7,
    band9
)