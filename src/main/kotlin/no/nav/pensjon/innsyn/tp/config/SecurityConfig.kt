package no.nav.pensjon.innsyn.tp.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun configure(http: HttpSecurity): SecurityFilterChain = http.run {
            authorizeHttpRequests {
                it.requestMatchers(AntPathRequestMatcher("/actuator/**")).permitAll()
                    .anyRequest().authenticated()
            }
            oauth2Login { }
            logout {
                it.logoutSuccessUrl("/")
            }
        build()
    }
}
