package no.nav.pensjon.innsyn.tp.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import no.nav.pensjon.innsyn.tp.CONTENT_TYPE_EXCEL
import no.nav.pensjon.innsyn.tp.assertEqualsTestData
import no.nav.pensjon.innsyn.tp.config.SecurityConfig
import no.nav.pensjon.innsyn.tp.domain.TpObjects.forhold
import no.nav.pensjon.innsyn.tp.service.AzureTokenService
import no.nav.pensjon.innsyn.tp.service.TpService
import no.nav.pensjon.innsyn.tp.service.TpSheetProducer
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream


@WebMvcTest(TpController::class, SecurityConfig::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebAppConfiguration
internal class TpControllerTest {

    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var wac: WebApplicationContext

    @MockkBean
    private lateinit var tpService: TpService

    @MockkBean
    private lateinit var tpSheetProducer: TpSheetProducer

    @MockkBean
    private lateinit var azureTokenService: AzureTokenService

    @BeforeAll
    fun setup() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(wac)
            .apply<DefaultMockMvcBuilder>(springSecurity())
            .build()
    }

    @Test
    fun `Returns 200 and valid worksheet`() {
        every { tpService.getData(eq("00000000000"), any()) } returns forhold
        every { tpSheetProducer.produceWorksheet(forhold) } returns XSSFWorkbook(FileInputStream(File("tp-test-worksheet.xlsx")))
        every { azureTokenService.getOnBehalOfToken(any()) } returns "bogus"
        mockMvc.get("/api/innsyn") {
            with(oauth2Login())
            header(FNR, "00000000000")
        }.andExpect {
            status { isOk() }
            content { contentType(CONTENT_TYPE_EXCEL) }
        }.andReturn().response.run {
            XSSFWorkbook(ByteArrayInputStream(contentAsByteArray))
        }.assertEqualsTestData()
    }
}
