package no.nav.pensjon.innsyn.tp.repository

import no.nav.pensjon.innsyn.common.repository.DomainRepository
import no.nav.pensjon.innsyn.tp.domain.Forhold
import org.springframework.stereotype.Repository

@Repository
interface ForholdRepository : DomainRepository<Forhold>