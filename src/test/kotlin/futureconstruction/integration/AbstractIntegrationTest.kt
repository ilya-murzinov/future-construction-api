package futureconstruction.integration

import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import java.time.Duration

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
open class AbstractIntegrationTest {

    @Autowired
    lateinit var webClient: WebTestClient

    @BeforeEach
    fun setup() {
        webClient = webClient.mutate()
            .responseTimeout(Duration.ofMinutes(2))
            .build()
    }
}