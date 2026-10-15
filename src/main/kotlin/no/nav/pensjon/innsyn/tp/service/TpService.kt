package no.nav.pensjon.innsyn.tp.service

import com.nimbusds.jose.JWSAlgorithm.RS256
import no.nav.pensjon.innsyn.tp.controller.FNR
import no.nav.pensjon.innsyn.tp.domain.Forhold
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus.BAD_GATEWAY
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository
import org.springframework.security.oauth2.jwt.JoseHeaderNames.ALG
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtClaimNames.SUB
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestClientException
import org.springframework.web.client.requiredBody
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.server.ResponseStatusException

@Service
class TpService(
    @Value($$"${tp.url}") tpURL: String,
    val clientRepository: OAuth2AuthorizedClientRepository,
    authorizedClientManager: OAuth2AuthorizedClientManager
) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val OAuth2AuthenticationToken.asJwtAuthenticationToken
        get() = JwtAuthenticationToken(
            clientRepository.loadAuthorizedClient<OAuth2AuthorizedClient>(
                authorizedClientRegistrationId,
                this,
                (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
            )?.run {
                Jwt.withTokenValue(accessToken.tokenValue ?: throw NullPointerException("Access token is null"))
                    .header(ALG, RS256.name)
                    .claim(SUB, name)
                    .build()
            } ?: throw NullPointerException("Authorized client not found."),
            authorities
        ).also {
            log.debug("JWT authentication token loaded with ver ${it.token.claims["ver"]}")
        }
    private val authentication
        get() = SecurityContextHolder.getContext().authentication?.let {
            log.debug("Authentication loaded of type ${it::class.qualifiedName}")
            if (it is OAuth2AuthenticationToken) it.asJwtAuthenticationToken
            else it
        } ?: throw NullPointerException("Authentication not found.")
    private val authorizeRequest
        get() = OAuth2AuthorizeRequest.withClientRegistrationId("tp")
            .principal(authentication)
            .build()
    private val OAuth2AuthorizedClientManager.client
        get() = authorize(authorizeRequest)
            ?: throw NullPointerException("Client registration for downstream server authorization not found.")

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
        return ResponseStatusException(BAD_GATEWAY)
    }
}
