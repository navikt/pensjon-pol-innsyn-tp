package no.nav.pensjon.innsyn

import no.nav.pensjon.innsyn.common.CONTENT_TYPE_EXCEL
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream

@SpringBootTest
@AutoConfigureMockMvc
internal class TpInnsynTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `Generates TP worksheet`() {
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