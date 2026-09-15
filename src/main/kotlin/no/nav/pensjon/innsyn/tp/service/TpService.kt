package no.nav.pensjon.innsyn.tp.service

import no.nav.pensjon.innsyn.tp.controller.FNR
import no.nav.pensjon.innsyn.tp.domain.Forhold
import no.nav.security.token.support.client.spring.oauth2.OAuth2ClientRequestInterceptor
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestClientException
import org.springframework.web.client.requiredBody
import org.springframework.web.server.ResponseStatusException

@Service
class TpService(
    @Value("\${tp.url}") tpURL: String,
    oAuth2ClientRequestInterceptor: OAuth2ClientRequestInterceptor
) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val restClient = RestClient.builder()
        .baseUrl(tpURL)
        .requestInterceptor(oAuth2ClientRequestInterceptor)
        .build()

    fun getData(fnr: String): Iterable<Forhold> = try {
        restClient.get()
            .uri("/api/pol")
            .headers {
                it.set(FNR, fnr)
            }
            .retrieve()
            .requiredBody<Iterable<Forhold>>().also {
                log.info("Successfully fetched data.")
            }
    } catch (e: RestClientException) {
        throw badGateway(e.message)
    }

    fun badGateway(logMessage: String?): ResponseStatusException {
        log.error("Error fetching data from TP: $logMessage")
        return ResponseStatusException(HttpStatus.BAD_GATEWAY)
    }
}
