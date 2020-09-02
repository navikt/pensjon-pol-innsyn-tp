package no.nav.pensjon.innsyn.tp.config

import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories
@Profile("!test")
class TpConfig