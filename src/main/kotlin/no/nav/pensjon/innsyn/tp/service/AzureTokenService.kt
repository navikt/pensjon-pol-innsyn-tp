package no.nav.pensjon.innsyn.tp.service

import no.nav.common.token_client.builder.AzureAdTokenClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.stereotype.Service

@Service
class AzureTokenService(@Value("\${tp.scope}") private val tpScope: String) {
    private val tokenClient = AzureAdTokenClientBuilder.builder().withNaisDefaults().buildOnBehalfOfTokenClient()

    fun getOnBehalOfToken(authorizedClient: OAuth2AuthorizedClient): String = tokenClient.exchangeOnBehalfOfToken(
        tpScope,
        authorizedClient.accessToken.tokenValue
    )
}
