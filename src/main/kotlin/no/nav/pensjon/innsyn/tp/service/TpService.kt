package no.nav.pensjon.innsyn.tp.service

import no.nav.pensjon.innsyn.tp.controller.FNR
import no.nav.pensjon.innsyn.tp.domain.Forhold
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux

@Service
class TpService(@Value("\${tp.url}") tpURL: String) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val webClient = WebClient.builder().baseUrl(tpURL).build()

    fun getData(fnr: String, auth: String): Iterable<Forhold> = webClient.get()
        .uri("/api/pol")
        .headers {
            it.set(FNR, fnr)
            it.setBearerAuth(auth)
        }
        .exchangeToFlux {
            when (it.statusCode().value()) {
                200 -> it.bodyToFlux<Forhold>()
                404 -> {
                    log.warn("Person not found, returning empty data.")
                    Flux.empty()
                }
                else -> it.bodyToFlux<String>().defaultIfEmpty("<NULL>").flatMap { body ->
                    Flux.error(badGateway("Status code ${it.statusCode()} with message: $body}"))
                }
            }
        }.onErrorMap {
            if (it !is ResponseStatusException) badGateway(it.message) else it
        }.doOnComplete {
            log.info("Successfully fetched data.")
        }.toIterable()

    fun badGateway(logMessage: String?): ResponseStatusException {
        log.error("Error fetching data from TP: $logMessage")
        return ResponseStatusException(HttpStatus.BAD_GATEWAY)
    }
}
