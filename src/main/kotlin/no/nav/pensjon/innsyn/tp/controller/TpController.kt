package no.nav.pensjon.innsyn.tp.controller

import no.nav.pensjon.innsyn.tp.CONTENT_TYPE_EXCEL
import no.nav.pensjon.innsyn.tp.service.AzureTokenService
import no.nav.pensjon.innsyn.tp.service.TpService
import no.nav.pensjon.innsyn.tp.service.TpSheetProducer
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.slf4j.LoggerFactory.getLogger
import org.springframework.http.HttpHeaders.CONTENT_DISPOSITION
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/innsyn/{fnr:\\d{11}}")
class TpController(
    private val worksheetProducer: TpSheetProducer,
    private val tpService: TpService,
    private val azureTokenService: AzureTokenService
) {
    val log = getLogger(javaClass)

    @GetMapping
    fun getTpInnsyn(
        @PathVariable("fnr") fnr: String,
        @RegisteredOAuth2AuthorizedClient("azure") authorizedClient: OAuth2AuthorizedClient,
        response: HttpServletResponse
    ) {
        log.info("Fetching data for ${fnr.substring(0, 5) + "*****"}")
        val forhold = try {
            tpService.getData(fnr, azureTokenService.getOnBehalOfToken(authorizedClient))
        } catch (_: NoSuchElementException) {
            emptyList()
        }
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
        log.info("Worksheet produced.")
    }
}
