package no.nav.pensjon.innsyn.tp.service

import no.nav.pensjon.innsyn.common.PersonNotFoundException
import no.nav.pensjon.innsyn.tp.domain.Forhold
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

@Service
class TpService(@Value("\${tp.url}") tpURL: String) {
    private val webClient = WebClient.create(tpURL)

    fun getData(fnr: String, auth: String): List<Forhold> = webClient.get()
        .uri("/api/pol/$fnr")
        .headers {
            it.setBearerAuth(auth)
        }
        .retrieve()
        .onStatus(HttpStatus.NOT_FOUND::equals) { Mono.error(PersonNotFoundException()) }
        .onStatus({ !it.is2xxSuccessful }) {
            Mono.error(
                ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Error fetching data from TP. Status code: ${it.statusCode()}"
                )
            )
        }
        .bodyToFlux<Forhold>()
        .collectList().block()!!
}