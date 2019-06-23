package futureconstruction.integration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.IMAGE_JPEG

class GenerateImageControllerTest : AbstractIntegrationTest() {

    private val uri = "/generate-images"
    private val result = ClassPathResource("sample-granule/output/image.jpg").file

    @Test
    fun `should return image when it can be generated`() {
        webClient
            .post()
            .uri(uri)
            .contentType(APPLICATION_JSON)
            .syncBody("""
                {
                  "utmZone": 33,
                  "latitudeBand": "U",
                  "gridSquare": "UP",
                  "date": "2018-08-04",
                  "channelMap": "visible"
                }
            """.trimIndent())
            .exchange()
            .expectHeader()
            .contentType(IMAGE_JPEG)
            .expectBody()
            .consumeWith {
                assertThat(it.responseBody).containsExactly(*result.readBytes())
            }
    }

    @Test
    fun `should return error when image can not be generated`() {
        webClient
            .post()
            .uri(uri)
            .contentType(APPLICATION_JSON)
            .syncBody("""
                {
                  "utmZone": 33,
                  "latitudeBand": "U",
                  "gridSquare": "UP",
                  "date": "2018-08-05",
                  "channelMap": "visible"
                }
            """.trimIndent())
            .exchange()
            .expectStatus()
            .isNotFound
            .expectBody()
            .json("""
                {
                  "message": "Image can not be generated using specified parameters"
                }
            """.trimIndent())
    }

    @Test
    fun `should return error when 'channelMap' is invalid`() {
        webClient
            .post()
            .uri(uri)
            .contentType(APPLICATION_JSON)
            .syncBody("""
                {
                  "utmZone": 33,
                  "latitudeBand": "U",
                  "gridSquare": "UP",
                  "date": "2018-08-04",
                  "channelMap": "visible1"
                }
            """.trimIndent())
            .exchange()
            .expectStatus()
            .is5xxServerError
            .expectBody()
            .jsonPath("message")
            .isEqualTo("Invalid channelMap: visible1")
    }
}