package futureconstruction.service

import futureconstruction.domain.Band.BAND_2
import futureconstruction.domain.Band.BAND_3
import futureconstruction.domain.Band.BAND_4
import futureconstruction.domain.Band.BAND_5
import futureconstruction.domain.Band.BAND_6
import futureconstruction.domain.Band.BAND_7
import futureconstruction.domain.Band.BAND_9
import futureconstruction.domain.ImageSet
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.nio.file.Files
import java.nio.file.Path
import java.util.Optional

@Component
class FileFinder(
    @Value("\${data.path}")
    private val dataPath: Path
) {

    companion object {
        private const val dateLength = 7
        private const val extension = ".tif"
    }

    fun findImageSet(prefix: String): Mono<Optional<ImageSet>> =
        Mono.fromSupplier {
            Files.list(dataPath)
                .map { it.fileName.toString() }
                .filter { it.startsWith(prefix) }
                .findFirst()
                .map {
                    val prefixWithTime = it.substring(0, prefix.length + dateLength)
                    ImageSet(
                        dataPath.resolve("$prefixWithTime${BAND_2.value}$extension").toFile(),
                        dataPath.resolve("$prefixWithTime${BAND_3.value}$extension").toFile(),
                        dataPath.resolve("$prefixWithTime${BAND_4.value}$extension").toFile(),
                        dataPath.resolve("$prefixWithTime${BAND_5.value}$extension").toFile(),
                        dataPath.resolve("$prefixWithTime${BAND_6.value}$extension").toFile(),
                        dataPath.resolve("$prefixWithTime${BAND_7.value}$extension").toFile(),
                        dataPath.resolve("$prefixWithTime${BAND_9.value}$extension").toFile()
                    )
                }
        }
}
