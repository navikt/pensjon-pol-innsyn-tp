package no.nav.pensjon.innsyn.tp.domain.container

import no.nav.pensjon.innsyn.common.domain.Domain
import no.nav.pensjon.innsyn.common.domain.DomainContainer

abstract class TpContainer<T: Domain>(
        entityName: String,
        propertyNames: Array<String>,
        source: (Int) -> List<T>
) : DomainContainer<T>(entityName, propertyNames, source)