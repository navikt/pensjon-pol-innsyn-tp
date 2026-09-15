package no.nav.pensjon.innsyn.tp

import no.nav.security.token.support.client.spring.oauth2.EnableOAuth2Client
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableOAuth2Client(cacheEnabled = true)
class TpInnsyn {
    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            runApplication<TpInnsyn>(*args)
        }
    }
}
