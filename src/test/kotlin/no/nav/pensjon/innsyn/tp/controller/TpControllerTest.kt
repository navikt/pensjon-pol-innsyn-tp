package no.nav.pensjon.innsyn.tp.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import no.nav.pensjon.innsyn.tp.CONTENT_TYPE_EXCEL
import no.nav.pensjon.innsyn.tp.TestConfig
import no.nav.pensjon.innsyn.tp.assertEqualsTestData
import no.nav.pensjon.innsyn.tp.domain.TpObjects.forhold
import no.nav.pensjon.innsyn.tp.service.TpService
import no.nav.pensjon.innsyn.tp.service.TpSheetProducer
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream

@WebMvcTest(TpController::class, TestConfig::class)
internal class TpControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var tpService: TpService

    @MockkBean
    private lateinit var tpSheetProducer: TpSheetProducer

    @Test
    @WithMockUser
    fun `Returns 200 and valid worksheet`() {
        every { tpService.getData("0", "") } returns forhold
        every { tpSheetProducer.produceWorksheet(forhold) } returns XSSFWorkbook(FileInputStream(File("tp-test-worksheet.xlsx")))
        mockMvc.get("/innsyn/0") {
            headers {
                setBearerAuth("")
            }
        }.andExpect {
            status { isOk() }
            content { contentType(CONTENT_TYPE_EXCEL) }
        }.andReturn().response.run {
            XSSFWorkbook(ByteArrayInputStream(contentAsByteArray))
        }.assertEqualsTestData()
    }
}