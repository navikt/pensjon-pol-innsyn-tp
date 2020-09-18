package no.nav.pensjon.innsyn.tp.service

import no.nav.pensjon.innsyn.common.PersonNotFoundException
import no.nav.pensjon.innsyn.common.service.SheetFiller
import no.nav.pensjon.innsyn.tp.domain.container.TpContainer
import no.nav.pensjon.innsyn.tp.repository.PersonRepository
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service

@Service
class TpSheetProducer(
        private val containers: List<TpContainer<*>>,
        private val personRepository: PersonRepository
) {
    fun produceWorksheet(fnr: String) = personRepository.findByFnr(fnr)?.run {
        XSSFWorkbook().apply {
            containers.map { SheetFiller(it) }.forEach {
                it.populateSheet(personId, this)
            }
        }
    } ?: throw PersonNotFoundException()
}