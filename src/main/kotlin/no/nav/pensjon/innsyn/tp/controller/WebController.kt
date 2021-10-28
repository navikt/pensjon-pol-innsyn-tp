package no.nav.pensjon.innsyn.tp.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class WebController {

    @GetMapping("/")
    fun index(
        model: Model,
        @RegisteredOAuth2AuthorizedClient authorizedClient: OAuth2AuthorizedClient,
        @AuthenticationPrincipal principal: OAuth2User
    ) = "index"
}