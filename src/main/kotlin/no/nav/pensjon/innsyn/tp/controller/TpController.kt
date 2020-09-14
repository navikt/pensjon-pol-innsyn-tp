package no.nav.pensjon.innsyn.tp.controller

import no.nav.pensjon.innsyn.common.CONTENT_TYPE_EXCEL
import no.nav.pensjon.innsyn.tp.service.TpSheetProducer
import no.nav.security.token.support.core.api.Protected
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.springframework.http.HttpHeaders.CONTENT_DISPOSITION
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.text.SimpleDateFormat
import java.util.*
import javax.servlet.http.HttpServletResponse

@Protected
@RestController
@RequestMapping("/innsyn")
class TpController(private val worksheetProducer: TpSheetProducer) {

    private val contentDisposition: String
        get() = "attachment; filename=TP-${SimpleDateFormat("yyyy-MM-dd").format(Date())}"

    @GetMapping
    fun getTpInnsyn(@RequestHeader("pid") pid: Int, response: HttpServletResponse) {
        response.apply {
            addHeader("Content-Description", "File Transfer")
            addHeader(CONTENT_DISPOSITION, contentDisposition)
            addHeader("Content-Transfer-Encoding", "binary")
            addHeader("Connection", "Keep-Alive")
            contentType = CONTENT_TYPE_EXCEL
            SXSSFWorkbook(worksheetProducer.produceWorksheet(pid)).write(outputStream)
        }
    }
}