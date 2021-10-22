package no.nav.pensjon.innsyn.common.domain

import no.nav.pensjon.innsyn.common.service.CellValueSetter
import no.nav.pensjon.innsyn.common.service.DomainRowFiller

abstract class DomainContainer<X : Any, T : Domain>(
    val entityName: String,
    val propertyNames: Array<String>,
    val map: (List<X>) -> List<T>
) {
    val rowFiller: (CellValueSetter, T) -> Unit = DomainRowFiller<T>()::setCellValues
}