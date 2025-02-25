package futureconstruction.service

import futureconstruction.domain.ImageSet
import futureconstruction.domain.Mode
import futureconstruction.domain.Mode.VEGETATION
import futureconstruction.domain.Mode.VISIBLE
import futureconstruction.domain.Mode.WATER_VAPOR
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO

@Component
class ImageGenerator {

    fun generateImage(imageSet: ImageSet, mode: Mode): Mono<ByteArray> =
        Mono.fromSupplier {
            return@fromSupplier when (mode) {
                VISIBLE -> imageFrom3Channels(imageSet.band2, imageSet.band3, imageSet.band4)
                VEGETATION -> imageFrom3Channels(imageSet.band5, imageSet.band6, imageSet.band7)
                WATER_VAPOR -> imageFromSingleChannel(imageSet.band9)
            }
        }

    private fun imageFrom3Channels(
        blueChannelFile: File,
        greenChannelFile: File,
        redChannelFile: File
    ): ByteArray {
        val blue = ImageIO.read(blueChannelFile)
        val green = ImageIO.read(greenChannelFile)
        val red = ImageIO.read(redChannelFile)

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
