package futureconstruction

import futureconstruction.domain.Mode
import futureconstruction.service.ImageGenerator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ImageGeneratorTest {

    private val subj = ImageGenerator()

    @Test
    fun `should generate VISIBLE image from image set`() {
        // setup
        val set = imageSet(band3 = whiteRectangle)

        // when
        val result = subj.generateImage(set, Mode.VISIBLE).block()!!

        // then
        assertThat(result).containsExactly(*greenRectangle.readBytes())
    }

    @Test
    fun `should generate VEGETATION image from image set`() {
        // setup
        val set = imageSet(band7 = whiteRectangle)

        // when
        val result = subj.generateImage(set, Mode.VEGETATION).block()!!

        // then
        assertThat(result).containsExactly(*redRectangle.readBytes())
    }

    @Test
    fun `should generate WATER_VAPOR image from image set`() {
        // setup
        val set = imageSet(band9 = whiteRectangle)

        // when
        val result = subj.generateImage(set, Mode.WATER_VAPOR).block()!!

        // then
        assertThat(result).containsExactly(*blueRectangle.readBytes())
    }

    @Test
    fun `should mix all channels`() {
        // setup
        val set = imageSet(
            band2 = whiteRectangle,
            band3 = whiteRectangle,
            band4 = whiteRectangle
        )

        // when
        val result = subj.generateImage(set, Mode.VISIBLE).block()!!

        // then
        assertThat(result).containsExactly(*whiteRectangleJpg.readBytes())
    }
}
