package futureconstruction.service

import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import futureconstruction.domain.ImageSet
import futureconstruction.domain.Mode
import futureconstruction.domain.Mode.*
import org.springframework.stereotype.Component
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO

@Component
class ImageGenerator {

    private val s = Schedulers.newElastic("heavy")

    fun generateImage(imageSet: ImageSet, mode: Mode): Mono<ByteArray> =
        Mono.fromSupplier {
            return@fromSupplier when (mode) {
                VISIBLE -> imageFrom3Channels(imageSet.band2, imageSet.band3, imageSet.band4)
                VEGETATION -> imageFrom3Channels(imageSet.band5, imageSet.band6, imageSet.band7)
                WATER_VAPOR -> imageFromSingleChannel(imageSet.band9)
            }
        }.publishOn(s)

    private fun imageFrom3Channels(
        channel1: File,
        channel2: File,
        channel3: File
    ): ByteArray {
        val blue = ImageIO.read(channel1)
        val green = ImageIO.read(channel2)
        val red = ImageIO.read(channel3)

        val width = blue.width
        val height = blue.height

        val combined = BufferedImage(width, height, TYPE_INT_RGB)

        (0 until width).forEach { x ->
            (0 until height).forEach { y ->
                val b = blue.getRGB(x, y) and 0xff
                val g = green.getRGB(x, y) and 0xff00
                val r = red.getRGB(x, y) and 0xff0000

                combined.setRGB(x, y, b or g or r)
            }
        }

        return exportJPEG(combined)
    }

    private fun imageFromSingleChannel(channel: File): ByteArray {
        val blue = ImageIO.read(channel)

        val width = blue.width
        val height = blue.height

        val combined = BufferedImage(width, height, TYPE_INT_RGB)

        (0 until width).forEach { x ->
            (0 until height).forEach { y ->
                combined.setRGB(x, y, blue.getRGB(x, y) and 0xff)
            }
        }

        return exportJPEG(combined)
    }

    private fun exportJPEG(bytes: BufferedImage): ByteArray {
        val out = ByteArrayOutputStream()
        ImageIO.write(bytes, "jpg", out)
        out.flush()
        val imageInBytes = out.toByteArray()
        out.close()

        return imageInBytes
    }
}
