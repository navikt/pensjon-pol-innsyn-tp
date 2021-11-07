package no.nav.pensjon.innsyn.tp.controller

import no.nav.pensjon.innsyn.tp.CONTENT_TYPE_EXCEL
import no.nav.pensjon.innsyn.tp.service.TpService
import no.nav.pensjon.innsyn.tp.service.TpSheetProducer
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.springframework.http.HttpHeaders.CONTENT_DISPOSITION
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/innsyn/{fnr}")
class TpController(private val worksheetProducer: TpSheetProducer, private val tpService: TpService) {

    @GetMapping
    fun getTpInnsyn(
        @PathVariable("fnr") fnr: String,
        @RegisteredOAuth2AuthorizedClient("azure") authorizedClient: OAuth2AuthorizedClient,
        response: HttpServletResponse
    ) {
        // TODO: Use on behalf of flow and replace token with token for TP
        val forhold = tpService.getData(fnr, authorizedClient.accessToken.tokenValue)

        response.apply {
            addHeader("Content-Description", "File Transfer")
            addHeader(CONTENT_DISPOSITION, "attachment; filename=$fnr.xlsx")
            addHeader("Content-Transfer-Encoding", "binary")
            addHeader("Connection", "Keep-Alive")
            contentType = CONTENT_TYPE_EXCEL
            SXSSFWorkbook(
                worksheetProducer.produceWorksheet(forhold)
            ).write(outputStream)
        }
    }
}
