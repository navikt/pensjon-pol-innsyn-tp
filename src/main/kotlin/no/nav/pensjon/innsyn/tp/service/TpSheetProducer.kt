package no.nav.pensjon.innsyn.tp.service

import no.nav.pensjon.innsyn.tp.domain.Forhold
import no.nav.pensjon.innsyn.tp.domain.container.TpContainer
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service

@Service
class TpSheetProducer(
    private val containers: List<TpContainer<*>>
) {
    fun produceWorksheet(forhold: Iterable<Forhold>) = XSSFWorkbook().apply {
        containers.map { SheetFiller(it) }.forEach { filler ->
            filler.populateSheet(forhold, this)
        }
    }
}
