package no.nav.pensjon.innsyn

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TpInnsyn {
    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            runApplication<TpInnsyn>(*args)
        }
    }
}