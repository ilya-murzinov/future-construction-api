package futureconstruction.service

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
                        dataPath.resolve("${prefixWithTime}B02.tif").toFile(),
                        dataPath.resolve("${prefixWithTime}B03.tif").toFile(),
                        dataPath.resolve("${prefixWithTime}B04.tif").toFile(),
                        dataPath.resolve("${prefixWithTime}B05.tif").toFile(),
                        dataPath.resolve("${prefixWithTime}B06.tif").toFile(),
                        dataPath.resolve("${prefixWithTime}B07.tif").toFile(),
                        dataPath.resolve("${prefixWithTime}B09.tif").toFile()
                    )
                }
        }
}
