package futureconstruction

import futureconstruction.domain.Band
import futureconstruction.domain.Band.BAND_2
import futureconstruction.domain.Band.BAND_3
import futureconstruction.domain.Band.BAND_4
import futureconstruction.domain.Band.BAND_5
import futureconstruction.domain.Band.BAND_6
import futureconstruction.domain.Band.BAND_7
import futureconstruction.domain.Band.BAND_9
import futureconstruction.domain.Mode
import futureconstruction.service.FileFinder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class FileFinderTest {
    private val time = "000000_"

    @Test
    fun `should find image set for VISIBLE mode`() {
        // setup
        val testDir = createTempDir()
        val subj = FileFinder(testDir.toPath())

        createFile(testDir, BAND_2)
        createFile(testDir, BAND_3)
        createFile(testDir, BAND_4)

        // when
        val imageSet = subj.findImageSet(prefix).block()!!

        // then
        assertThat(imageSet).isPresent
        assertThat(imageSet).get().matches { it.isValidFor(Mode.VISIBLE) }
    }

    @Test
    fun `should find image set for VEGETATION mode`() {
        // setup
        val testDir = createTempDir()
        val subj = FileFinder(testDir.toPath())

        createFile(testDir, BAND_5)
        createFile(testDir, BAND_6)
        createFile(testDir, BAND_7)

        // when
        val imageSet = subj.findImageSet(prefix).block()!!

        // then
        assertThat(imageSet).isPresent
        assertThat(imageSet).get().matches { it.isValidFor(Mode.VEGETATION) }
    }

    @Test
    fun `should find image set for WATER_VAPOR mode`() {
        // setup
        val testDir = createTempDir()
        val subj = FileFinder(testDir.toPath())

        createFile(testDir, BAND_9)

        // when
        val imageSet = subj.findImageSet(prefix).block()!!

        // then
        assertThat(imageSet).isPresent
        assertThat(imageSet).get().matches { it.isValidFor(Mode.WATER_VAPOR) }
    }

    @Test
    fun `should not return image set when files do not exist`() {
        // setup
        val testDir = createTempDir()
        val subj = FileFinder(testDir.toPath())

        createFile(testDir, BAND_5)
        createFile(testDir, BAND_6)
        createFile(testDir, BAND_7)

        // when
        val imageSet = subj.findImageSet("ANOTHER_PREFIX").block()!!

        // then
        assertThat(imageSet).isEmpty
    }

    @Test
    fun `should return invalid image set when it is not full`() {
        // setup
        val testDir = createTempDir()
        val subj = FileFinder(testDir.toPath())

        createFile(testDir, BAND_2)
        createFile(testDir, BAND_5)
        createFile(testDir, BAND_6)

        // when
        val imageSet = subj.findImageSet(prefix).block()!!

        // then
        assertThat(imageSet).isPresent
        assertThat(imageSet).get().matches { !it.isValidFor(Mode.VISIBLE) }
        assertThat(imageSet).get().matches { !it.isValidFor(Mode.VEGETATION) }
        assertThat(imageSet).get().matches { !it.isValidFor(Mode.WATER_VAPOR) }
    }

    private fun createFile(dir: File, band: Band) {
        dir.toPath()
            .resolve("$prefix$time${band.value}.tif")
            .toFile()
            .createNewFile()
    }
}
