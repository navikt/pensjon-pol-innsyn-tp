package no.nav.pensjon.innsyn.tp.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.client.JwtBearerOAuth2AuthorizedClientProvider
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.client.endpoint.RestClientJwtBearerTokenResponseClient
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun configure(http: HttpSecurity): SecurityFilterChain = http.run {
            authorizeHttpRequests {
                it.requestMatchers(("/actuator/**")).permitAll()
                    .anyRequest().authenticated()
            }
            oauth2Login { }
            logout {
                it.logoutSuccessUrl("/")
            }
        build()
    }

    @Bean
    fun oAuth2AuthorizedClientManager(
        clientRegistrationRepository: ClientRegistrationRepository,
        oauth2AuthorizedClientRepository: OAuth2AuthorizedClientRepository
    ): OAuth2AuthorizedClientManager = DefaultOAuth2AuthorizedClientManager(
        clientRegistrationRepository,
        oauth2AuthorizedClientRepository
    ).apply {
        setAuthorizedClientProvider(OAuth2AuthorizedClientProviderBuilder.builder()
            .authorizationCode()
            .refreshToken()
            .clientCredentials()
            .provider(JwtBearerOAuth2AuthorizedClientProvider().apply {
                setAccessTokenResponseClient(RestClientJwtBearerTokenResponseClient().apply {
                    setParametersCustomizer { params ->
                        params["requested_token_use"] = "on_behalf_of"
                    }
                })
                setJwtAssertionResolver { context ->
                    when (val auth = context.principal) {
                        is OAuth2AuthenticationToken -> Jwt.withTokenValue((auth.credentials as OAuth2AccessToken).tokenValue).build()
                        is JwtAuthenticationToken -> auth.token
                        else -> throw IllegalStateException("Cannot resolve JWT assertion from principal: ${auth.javaClass}")
                    }
                }
            })
            .build()
        )
    }
}
