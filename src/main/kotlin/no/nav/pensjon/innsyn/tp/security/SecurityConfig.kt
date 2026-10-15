package no.nav.pensjon.innsyn.tp.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor
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
    fun oAuth2ClientHttpRequestInterceptor(
        authorizedClientManager: OAuth2AuthorizedClientManager
    ): OAuth2ClientHttpRequestInterceptor = OAuth2ClientHttpRequestInterceptor(authorizedClientManager).also {
        it.setClientRegistrationIdResolver { "azure" }
    }
}
