package no.nav.pensjon.innsyn.tp.config

import no.nav.security.token.support.spring.api.EnableJwtTokenValidation
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories
@EnableJwtTokenValidation
@Profile("!test")
@Configuration
class TpConfig