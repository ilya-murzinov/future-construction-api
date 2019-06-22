package futureconstruction.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers

@Configuration
open class Config {

    @Bean
    open fun scheduler(): Scheduler = Schedulers.newParallel("heavy")
}