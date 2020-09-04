package no.nav.pensjon.innsyn.tp.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import no.nav.pensjon.innsyn.common.CONTENT_TYPE_EXCEL
import no.nav.pensjon.innsyn.tp.assertEqualsTestData
import no.nav.pensjon.innsyn.tp.service.TpSheetProducer
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream

@WebMvcTest(TpController::class)
internal class TpControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var tpSheetProducer: TpSheetProducer

    @Test
    fun `Returns 200 and valid worksheet`() {
        every { tpSheetProducer.produceWorksheet(1) } returns XSSFWorkbook(FileInputStream(File("tp-test-worksheet.xlsx")))
        mockMvc.get("/innsyn") {
            headers {
                this["pid"] = "1"
            }
        }.andExpect {
            status { isOk }
            content { contentType(CONTENT_TYPE_EXCEL) }
        }.andReturn().response.run {
            XSSFWorkbook(ByteArrayInputStream(contentAsByteArray))
        }.assertEqualsTestData()
    }

    @Test
    fun `Returns 404 on missing object`(){
        every { tpSheetProducer.produceWorksheet(2) } throws EmptyResultDataAccessException(1)
        mockMvc.get("/innsyn") {
            headers {
                this["pid"] = "2"
            }
        }.andExpect {
            status { isNotFound }
            content { string("") }
        }
    }

    @Test
    fun `Returns 501 on database error`(){
        every { tpSheetProducer.produceWorksheet(3) } throws DataIntegrityViolationException("Generic SQL error")
        mockMvc.get("/innsyn") {
            headers {
                this["pid"] = "3"
            }
        }.andExpect {
            status { isInternalServerError }
            content { string("Database error.") }
        }
    }
}