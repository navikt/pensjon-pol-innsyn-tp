package no.nav.pensjon.innsyn.tp.domain

import no.nav.pensjon.innsyn.tp.service.CellValueSetter
import no.nav.pensjon.innsyn.tp.service.DomainRowFiller

abstract class DomainContainer<X : Any, T : Domain>(
    val entityName: String,
    val propertyNames: Array<String>,
    val map: (Iterable<X>) -> Iterable<T>
) {
    val rowFiller: (CellValueSetter, T) -> Unit = DomainRowFiller<T>()::setCellValues
}
