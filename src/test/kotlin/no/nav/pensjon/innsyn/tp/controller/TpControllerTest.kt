package no.nav.pensjon.innsyn.tp.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import no.nav.pensjon.innsyn.common.CONTENT_TYPE_EXCEL
import no.nav.pensjon.innsyn.tp.service.TpSheetProducer
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
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
            val testBook = XSSFWorkbook(FileInputStream(File("tp-test-worksheet.xlsx")))
            XSSFWorkbook(ByteArrayInputStream(contentAsByteArray)).apply {
                Assertions.assertEquals(testBook.numberOfSheets, numberOfSheets)
                this.forEachIndexed { si, sheet ->
                    val testSheet = testBook.getSheetAt(si)
                    Assertions.assertEquals(testSheet.physicalNumberOfRows, sheet.physicalNumberOfRows)
                    sheet.forEachIndexed { ri, row ->
                        val testRow = testSheet.getRow(ri)
                        Assertions.assertEquals(testRow.physicalNumberOfCells, row.physicalNumberOfCells)
                        row.forEachIndexed { ci, cell ->
                            when(cell.cellType){
                                CellType.NUMERIC -> Assertions.assertEquals(
                                        testRow.getCell(ci)?.numericCellValue,
                                        cell.numericCellValue
                                )
                                CellType.STRING -> Assertions.assertEquals(
                                        testRow.getCell(ci)?.stringCellValue,
                                        cell.stringCellValue
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}