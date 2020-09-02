package no.nav.pensjon.innsyn.common.controller

import no.nav.security.token.support.core.api.Unprotected
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Unprotected
@RestController
class ProbeController {
    @get:GetMapping("/isAlive")
    val isAlive: Nothing? = null

    @get:GetMapping("/isReady")
    val isReady: Nothing? = null
}