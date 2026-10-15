package no.nav.pensjon.innsyn.tp.controller

import io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE
import jakarta.servlet.http.HttpServletResponse
import no.nav.pensjon.innsyn.tp.CONTENT_TYPE_EXCEL
import no.nav.pensjon.innsyn.tp.service.TpService
import no.nav.pensjon.innsyn.tp.service.TpSheetProducer
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.http.HttpHeaders.CONNECTION
import org.springframework.http.HttpHeaders.CONTENT_DISPOSITION
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@Controller
class TpController(
    private val worksheetProducer: TpSheetProducer,
    private val tpService: TpService
) {
    val log: Logger = getLogger(javaClass)

    @GetMapping("/api/innsyn")
    fun getTpInnsyn(
        @RequestHeader(FNR) fnr: String,
        response: HttpServletResponse
    ) {
        log.info("Fetching data for $fnr.")
        val forhold = tpService.getData(fnr)
        val worksheet = SXSSFWorkbook(worksheetProducer.produceWorksheet(forhold))
        log.info("Worksheet produced.")
        response.apply {
            addHeader(CONTENT_DISPOSITION, "attachment; filename=\"$fnr - TP.xlsx\"")
            addHeader(CONNECTION, KEEP_ALIVE.toString())
            contentType = CONTENT_TYPE_EXCEL
            worksheet.write(outputStream)
        }
    }
}
