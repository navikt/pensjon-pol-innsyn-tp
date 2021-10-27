package no.nav.pensjon.innsyn.tp.service

import no.nav.pensjon.innsyn.tp.domain.Domain
import java.time.LocalDate

class DomainRowFiller<T : Domain> {
    fun setCellValues(sink: CellValueSetter, source: T) =
        source.fields.forEachIndexed { i, f ->
            when (val p = f.call()) {
                is String -> sink.setCellValue(i, p)
                is Double -> sink.setCellValue(i, p)
                is Int -> sink.setCellValue(i, p.toDouble())
                is LocalDate -> sink.setCellValue(i, p)
                is Boolean -> sink.setCellValue(i, p)
                null -> sink.setCellValue(i, "")
                else -> throw RuntimeException()
            }
        }
}