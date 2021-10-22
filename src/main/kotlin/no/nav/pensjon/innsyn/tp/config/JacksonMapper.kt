package no.nav.pensjon.innsyn.tp.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Configuration

@Configuration
class JacksonMapper(objectMapper: ObjectMapper) {
    init {
        objectMapper.registerKotlinModule()
        objectMapper.registerModule(JavaTimeModule())
    }
}