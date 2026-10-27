package no.nav.pensjon.innsyn.tp.security

import com.nimbusds.jose.JWSAlgorithm.RS256
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
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
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Component
class OnBehalfOfInterceptor(
    private val clientRepository: OAuth2AuthorizedClientRepository,
    private val authorizedClientManager: OAuth2AuthorizedClientManager
): ClientHttpRequestInterceptor {

    private val OAuth2AuthenticationToken.asJwtAuthenticationToken
        get() = JwtAuthenticationToken(
            clientRepository.loadAuthorizedClient<OAuth2AuthorizedClient>(
                authorizedClientRegistrationId,
                this,
                (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
            )?.run {
                Jwt.withTokenValue(accessToken.tokenValue)
                    .header(ALG, RS256.name)
                    .claim(SUB, name)
                    .build()
            } ?: throw NullPointerException("Authorized client not found."),
            authorities
        )

    private val authentication
        get() = SecurityContextHolder.getContext().authentication?.let {
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

    override fun intercept(
        request: HttpRequest,
        body: ByteArray,
        execution: ClientHttpRequestExecution
    ): ClientHttpResponse {
        request.headers.setBearerAuth(authorizedClientManager.client.accessToken.tokenValue)
        return execution.execute(request, body)
    }
}
