package futureconstruction

import futureconstruction.domain.ImageSet
import futureconstruction.domain.Mode
import futureconstruction.service.FileFinder
import futureconstruction.service.ImageGenerator
import futureconstruction.service.ImageService
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import reactor.core.publisher.toMono
import java.util.Optional
import java.util.UUID

class ImageServiceTest {

    private val finder: FileFinder = mockk()
    private val imageGenerator: ImageGenerator = mockk()

    private val subj = ImageService(finder, imageGenerator)

    @Test
    fun `should return image when it's available`() {
        // setup
        val imageSet = imageSet()
        every { finder.findImageSet(prefix) }
            .returns(Optional.of(imageSet).toMono())

        every { imageGenerator.generateImage(imageSet, Mode.VISIBLE) }
            .returns(whiteRectangleJpg.readBytes().toMono())

        // when
        val result = subj.generateImage(prefix, Mode.VISIBLE).block()!!

        // then
        assertThat(result).isPresent
    }

    @Test
    fun `should return empty when image set is not available`() {
        // setup
        every { finder.findImageSet(prefix) }
            .returns(Optional.empty<ImageSet>().toMono())

        // when
        val result = subj.generateImage(prefix, Mode.VISIBLE).block()!!

        // then
        assertThat(result).isEmpty
    }

    @Test
    fun `should return empty when image set is invalid`() {
        // setup
        val nonExistingFile = createTempDir().toPath().resolve(UUID.randomUUID().toString()).toFile()
        val imageSet = imageSet(band2 = nonExistingFile)
        every { finder.findImageSet(prefix) }
            .returns(Optional.of(imageSet).toMono())

        // when
        val result = subj.generateImage(prefix, Mode.VISIBLE).block()!!

        // then
        assertThat(result).isEmpty
    }
}