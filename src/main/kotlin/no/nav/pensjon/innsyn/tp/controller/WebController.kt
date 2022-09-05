package no.nav.pensjon.innsyn.tp.controller

import org.slf4j.LoggerFactory.getLogger
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping


@Controller
class WebController {
    private val log = getLogger(javaClass)

    @GetMapping("/")
    fun index(
        model: Model,
        @RegisteredOAuth2AuthorizedClient authorizedClient: OAuth2AuthorizedClient,
        @AuthenticationPrincipal principal: OidcUser
    ): String {
        log.info("New session established.")
        model.addAttribute("user", principal.getClaimAsString("NAVident"))
        return "index"
    }
}
