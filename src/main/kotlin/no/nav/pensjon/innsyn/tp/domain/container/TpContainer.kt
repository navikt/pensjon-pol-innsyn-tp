package no.nav.pensjon.innsyn.tp.domain.container

import no.nav.pensjon.innsyn.common.domain.Domain
import no.nav.pensjon.innsyn.common.domain.DomainContainer
import no.nav.pensjon.innsyn.tp.domain.Forhold
import reactor.core.publisher.Flux

abstract class TpContainer<T : Domain>(
    entityName: String,
    propertyNames: Array<String>,
    mapper: (Flux<Forhold>) -> Flux<T>
) : DomainContainer<Forhold, T>(entityName, propertyNames, mapper)