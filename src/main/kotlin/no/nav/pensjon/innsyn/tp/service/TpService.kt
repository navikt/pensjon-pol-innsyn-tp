package no.nav.pensjon.innsyn.tp.service

import no.nav.pensjon.innsyn.tp.controller.FNR
import no.nav.pensjon.innsyn.tp.domain.Forhold
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestClientException
import org.springframework.web.client.requiredBody
import org.springframework.web.server.ResponseStatusException

@Service
class TpService(
    @Value($$"${tp.url}") tpURL: String,
    authorizedClientManager: OAuth2AuthorizedClientManager
) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val authentication
        get() = SecurityContextHolder.getContext().authentication ?: throw ResponseStatusException(UNAUTHORIZED)
    private val authorizeRequest
        get() = OAuth2AuthorizeRequest.withClientRegistrationId("tp")
            .principal(authentication)
            .build()
    private val OAuth2AuthorizedClientManager.client
        get() = authorize(authorizeRequest) ?: throw ResponseStatusException(
            INTERNAL_SERVER_ERROR,
            "Client registration for downstream server authorization not found."
        )

    private val restClient = RestClient.builder()
        .baseUrl(tpURL)
        .requestInterceptor { request, bytes, execution ->
            request.headers.setBearerAuth(authorizedClientManager.client.accessToken.tokenValue)
            execution.execute(request, bytes)
        }
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
