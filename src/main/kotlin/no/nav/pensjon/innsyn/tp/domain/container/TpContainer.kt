package no.nav.pensjon.innsyn.tp.domain.container

import no.nav.pensjon.innsyn.tp.domain.Domain
import no.nav.pensjon.innsyn.tp.domain.DomainContainer
import no.nav.pensjon.innsyn.tp.domain.Forhold

abstract class TpContainer<T : Domain>(
    entityName: String,
    propertyNames: Array<String>,
    mapper: (Iterable<Forhold>) -> Iterable<T>
) : DomainContainer<Forhold, T>(entityName, propertyNames, mapper)
