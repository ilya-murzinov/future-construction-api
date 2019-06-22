package futureconstruction.service

import futureconstruction.domain.Mode
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.scheduler.Scheduler
import java.util.Optional

@Component
class ImageService(
    private val fileFinder: FileFinder,
    private val imageGenerator: ImageGenerator,
    private val heavyScheduler: Scheduler
) {

    fun generateImage(prefix: String, mode: Mode): Mono<Optional<ByteArray>> =
        fileFinder.findImageSet(prefix)
            .flatMap { set ->
                if (set.filter { it.isValidFor(mode) }.isPresent)
                    imageGenerator.generateImage(set.get(), mode)
                        .map { Optional.of(it) }
                else
                    Mono.just(Optional.empty())
            }
            .publishOn(heavyScheduler)
}