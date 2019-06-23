package futureconstruction.api

import futureconstruction.domain.Mode
import futureconstruction.service.ImageService
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.IMAGE_JPEG
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RestController
class GenerateImageEndpoint(
    private val imageService: ImageService
) {

    @PostMapping("/generate-images")
    fun generateImage(
        @RequestBody request: GenerateImageRequest
    ): Mono<ResponseEntity<*>> =
        imageService.generateImage(request.filePrefix(), request.mode())
            .map {
                when {
                    it.isPresent ->
                        ok().contentType(IMAGE_JPEG).body(it.get())
                    else ->
                        ok().contentType(APPLICATION_JSON).body(ErrorResponse())
                }
            }
}

data class GenerateImageRequest(
    val utmZone: Int,
    val latitudeBand: String,
    val gridSquare: String,
    val date: LocalDate,
    val channelMap: String
) {

    companion object {
        private val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    }

    fun filePrefix(): String =
        "T$utmZone$latitudeBand${gridSquare}_${date.format(formatter)}T"

    fun mode(): Mode =
        when (channelMap) {
            "visible" -> Mode.VISIBLE
            "vegetation" -> Mode.VEGETATION
            "waterVapor" -> Mode.WATER_VAPOR
            else -> throw IllegalArgumentException("Invalid channelMap: $channelMap")
        }
}

data class ErrorResponse(
    val message: String = "Image can not be generated using specified parameters"
)