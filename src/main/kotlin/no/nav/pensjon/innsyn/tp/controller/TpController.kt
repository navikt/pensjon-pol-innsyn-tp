package no.nav.pensjon.innsyn.tp.controller

import no.nav.pensjon.innsyn.tp.CONTENT_TYPE_EXCEL
import no.nav.pensjon.innsyn.tp.service.TpService
import no.nav.pensjon.innsyn.tp.service.TpSheetProducer
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.springframework.http.HttpHeaders.CONTENT_DISPOSITION
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.web.bind.annotation.*
import java.text.SimpleDateFormat
import java.util.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/innsyn/{fnr}")
class TpController(private val worksheetProducer: TpSheetProducer, private val tpService: TpService) {

    private val contentDisposition: String
        get() = "attachment; filename=TP-${SimpleDateFormat("yyyy-MM-dd").format(Date())}"

    @GetMapping
    fun getTpInnsyn(
        @PathVariable("fnr") fnr: String,
        @RegisteredOAuth2AuthorizedClient authorizedClient: OAuth2AuthorizedClient,
        response: HttpServletResponse
    ) {
        // TODO: Use on behalf of flow and replace token with token for TP
        val forhold = tpService.getData(fnr, authorizedClient.accessToken.tokenValue)

        response.apply {
            addHeader("Content-Description", "File Transfer")
            addHeader(CONTENT_DISPOSITION, contentDisposition)
            addHeader("Content-Transfer-Encoding", "binary")
            addHeader("Connection", "Keep-Alive")
            contentType = CONTENT_TYPE_EXCEL
            SXSSFWorkbook(
                worksheetProducer.produceWorksheet(forhold)
            ).write(outputStream)
        }
    }
}
