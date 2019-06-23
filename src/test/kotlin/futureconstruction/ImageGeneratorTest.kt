package futureconstruction

import futureconstruction.domain.ImageSet
import futureconstruction.domain.Mode
import futureconstruction.service.ImageGenerator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import java.io.File

class ImageGeneratorTest(
    @Value("\${test.images.path}/input/white-rectangle.tiff")
    val whiteRectangle: File,
    @Value("\${test.images.path}/input/black-rectangle.tiff")
    val blackRectangle: File,
    @Value("\${test.images.path}/output/blue-rectangle.jpg")
    val blueRectangle : File,
    @Value("\${test.images.path}/output/green-rectangle.jpg")
    val greenRectangle : File,
    @Value("\${test.images.path}/output/red-rectangle.jpg")
    val redRectangle : File,
    @Value("\${test.images.path}/output/white-rectangle.jpg")
    val whiteRectangleJpg : File
) {

    private val subj = ImageGenerator()

    @Test
    fun `should generate VISIBLE image from image set`() {
        // setup
        val set = ImageSet(
            blackRectangle,
            whiteRectangle,
            blackRectangle,
            blackRectangle,
            blackRectangle,
            blackRectangle,
            blackRectangle
        )

        // when
        val result = subj.generateImage(set, Mode.VISIBLE).block()!!

        // then
        assertThat(result).containsExactly(*greenRectangle.readBytes())
    }

    @Test
    fun `should generate VEGETATION image from image set`() {
        // setup
        val set = ImageSet(
            blackRectangle,
            blackRectangle,
            blackRectangle,
            blackRectangle,
            blackRectangle,
            whiteRectangle,
            blackRectangle
        )

        // when
        val result = subj.generateImage(set, Mode.VEGETATION).block()!!

        // then
        assertThat(result).containsExactly(*redRectangle.readBytes())
    }

    @Test
    fun `should generate WATER_VAPOR image from image set`() {
        // setup
        val set = ImageSet(
            blackRectangle,
            blackRectangle,
            blackRectangle,
            blackRectangle,
            blackRectangle,
            blackRectangle,
            whiteRectangle
        )

        // when
        val result = subj.generateImage(set, Mode.WATER_VAPOR).block()!!

        // then
        assertThat(result).containsExactly(*blueRectangle.readBytes())
    }

    @Test
    fun `should mix all channels`() {
        // setup
        val set = ImageSet(
            whiteRectangle,
            whiteRectangle,
            whiteRectangle,
            blackRectangle,
            blackRectangle,
            blackRectangle,
            blackRectangle
        )

        // when
        val result = subj.generateImage(set, Mode.VISIBLE).block()!!

        // then
        assertThat(result).containsExactly(*whiteRectangleJpg.readBytes())
    }
}
