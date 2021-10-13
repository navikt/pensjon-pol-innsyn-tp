package no.nav.pensjon.innsyn.tp.service

import no.nav.pensjon.innsyn.common.service.SheetFiller
import no.nav.pensjon.innsyn.tp.domain.Forhold
import no.nav.pensjon.innsyn.tp.domain.container.TpContainer
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class TpSheetProducer(
    private val containers: List<TpContainer<*>>
) {
    fun produceWorksheet(forhold: Flux<Forhold>) = XSSFWorkbook().apply {
        containers.map { SheetFiller(it) }.forEach {
            it.populateSheet(forhold, this)
        }
    }
}