package no.nav.pensjon.innsyn.tp.service

import no.nav.pensjon.innsyn.tp.domain.Forhold
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

@Service
class TpService(@Value("\${tp.url}") tpURL: String) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val webClient = WebClient.create(tpURL)

    fun getData(fnr: String, auth: String): List<Forhold> = webClient.get()
        .uri("/api/pol/$fnr")
        .headers {
            it.setBearerAuth(auth)
        }
        .retrieve()
        .onStatus({ it.value() == 404 }) {
            log.warn("Person not found, returning empty data.")
            Mono.error(NoSuchElementException())
        }
        .onStatus({ !it.is2xxSuccessful }) {
            "Error fetching data from TP. Status code: ${it.statusCode()}".let { err ->
                log.error(err)
                Mono.error(ResponseStatusException(HttpStatus.BAD_GATEWAY, err))
            }
        }
        .bodyToFlux<Forhold>()
        .onErrorMap {
                if(it !is NoSuchElementException && it !is ResponseStatusException) {
                    log.error("Error fetching data from TP: ${it.message}")
                    ResponseStatusException(HttpStatus.BAD_GATEWAY)
                } else it
        }
        .collectList().block()!!.also {
            log.info("Successfully fetched data.")
        }
}
