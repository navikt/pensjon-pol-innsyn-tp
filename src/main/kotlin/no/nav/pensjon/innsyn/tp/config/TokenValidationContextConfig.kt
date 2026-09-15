package no.nav.pensjon.innsyn.tp.config

import com.nimbusds.oauth2.sdk.auth.JWTAuthentication
import no.nav.security.token.support.core.context.TokenValidationContext
import no.nav.security.token.support.core.context.TokenValidationContextHolder
import no.nav.security.token.support.core.jwt.JwtToken
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder

@Configuration
class TokenValidationContextConfig {

    @Bean
    fun tokenValidationContextHolder(): TokenValidationContextHolder = object: TokenValidationContextHolder {

        private val SecurityContext.token
            get() = (authentication as? JWTAuthentication)
                ?.clientAssertion?.parsedString?.let {
                    JwtToken(it)
                }

        override fun getTokenValidationContext() = TokenValidationContext(
            SecurityContextHolder.getContext().token?.let {
                mapOf("azure" to it)
            } ?: emptyMap()
        )

        override fun setTokenValidationContext(tokenValidationContext: TokenValidationContext?) {
            throw NotImplementedError()
        }
    }

}
