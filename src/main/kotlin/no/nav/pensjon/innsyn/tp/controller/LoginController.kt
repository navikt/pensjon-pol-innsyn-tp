package no.nav.pensjon.innsyn.tp.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController("/security")
class LoginController {

    @GetMapping("/user")
    fun user(@AuthenticationPrincipal principal: OAuth2User) =
        mapOf<String, Any>("name" to principal.getAttribute("name"))
}