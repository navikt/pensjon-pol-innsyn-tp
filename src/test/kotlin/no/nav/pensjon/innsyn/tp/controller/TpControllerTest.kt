package no.nav.pensjon.innsyn.tp.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import no.nav.pensjon.innsyn.tp.CONTENT_TYPE_EXCEL
import no.nav.pensjon.innsyn.tp.MockOAuth2ServerInitializer
import no.nav.pensjon.innsyn.tp.assertEqualsTestData
import no.nav.pensjon.innsyn.tp.domain.TpObjects.forhold
import no.nav.pensjon.innsyn.tp.service.TpService
import no.nav.pensjon.innsyn.tp.service.TpSheetProducer
import no.nav.security.mock.oauth2.MockOAuth2Server
import no.nav.security.mock.oauth2.token.DefaultOAuth2TokenCallback
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultHandler
import org.springframework.test.web.servlet.get
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream


@WebMvcTest(TpController::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(initializers = [MockOAuth2ServerInitializer::class])
internal class TpControllerTest {

    @Autowired
    private lateinit var mockOAuth2Server: MockOAuth2Server

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var tpService: TpService

    @MockkBean
    private lateinit var tpSheetProducer: TpSheetProducer

    @BeforeAll
    fun setup() {
    }

    @Test
    fun `Returns 200 and valid worksheet`() {
        mockOAuth2Server.enqueueCallback(DefaultOAuth2TokenCallback("issuer1", "foo"))
        every { tpService.getData("0", "") } returns forhold
        every { tpSheetProducer.produceWorksheet(forhold) } returns XSSFWorkbook(FileInputStream(File("tp-test-worksheet.xlsx")))
        mockMvc.get("/innsyn/0") {
            headers { setBearerAuth(mockOAuth2Server.issueToken().serialize()) }
        }.andDo {
            this.handle(ResultHandler {  })
        }.andExpect {

            status { isOk() }
            content { contentType(CONTENT_TYPE_EXCEL) }
        }.andReturn().response.run {
            XSSFWorkbook(ByteArrayInputStream(contentAsByteArray))
        }.assertEqualsTestData()
    }
}