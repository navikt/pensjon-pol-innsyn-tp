package no.nav.pensjon.innsyn.common.service

import no.nav.pensjon.innsyn.common.domain.Domain
import java.time.LocalDate

class DomainRowFiller<T : Domain> {
    fun setCellValues(sink: CellValueSetter, source: T) =
            source.fields.forEachIndexed { i, p ->
                when (val v = p.call()) {
                    is String -> sink.setCellValue(i, v)
                    is Double -> sink.setCellValue(i, v)
                    is Int -> sink.setCellValue(i, v.toDouble())
                    is LocalDate -> sink.setCellValue(i, v)
                    is Boolean -> sink.setCellValue(i, v)
                    null -> sink.setCellValue(i, "")
                    else -> throw RuntimeException()
                }
            }
}