package no.nav.pensjon.innsyn.tp.service

import no.nav.pensjon.innsyn.common.service.SheetFiller
import no.nav.pensjon.innsyn.tp.domain.container.TpContainer
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service

@Service
class TpSheetProducer(private val containers: List<TpContainer<*>>) {
    fun produceWorksheet(pid: Int) = XSSFWorkbook().apply {
        containers.map { SheetFiller(it) }.forEach {
            it.populateSheet(pid, this)
        }
    }
}