package no.nav.pensjon.innsyn.tp.domain.container

import no.nav.pensjon.innsyn.common.domain.Domain
import no.nav.pensjon.innsyn.common.domain.DomainContainer
import no.nav.pensjon.innsyn.tp.domain.Forhold

abstract class TpContainer<T : Domain>(
    entityName: String,
    propertyNames: Array<String>,
    mapper: (List<Forhold>) -> List<T>
) : DomainContainer<Forhold, T>(entityName, propertyNames, mapper)