package no.nav.pensjon.innsyn.tp.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import no.nav.pensjon.innsyn.tp.domain.TpObjects
import no.nav.pensjon.innsyn.tp.domain.container.ForholdContainer
import no.nav.pensjon.innsyn.tp.domain.container.YtelseContainer
import no.nav.pensjon.innsyn.tp.repository.ForholdRepository
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.io.File
import java.io.FileInputStream

@SpringBootTest(classes = [TpSheetProducer::class, ForholdContainer::class, YtelseContainer::class])
@ActiveProfiles("TP")
internal class TpSheetProducerTest {
    @MockkBean
    lateinit var forholdRepository: ForholdRepository

    @Autowired
    private lateinit var tpSheetProducer: TpSheetProducer

    @Test
    fun `Produces TP poppSheet`() {
        every { forholdRepository.findAllByPersonId(1) } returns TpObjects.forhold
        val testBook = XSSFWorkbook(FileInputStream(File("tp-test-worksheet.xlsx")))
        tpSheetProducer.produceWorksheet(1).apply {
            assertEquals(testBook.numberOfSheets, numberOfSheets)
            this.forEachIndexed { si, sheet ->
                val testSheet = testBook.getSheetAt(si)
                assertEquals(testSheet.physicalNumberOfRows, sheet.physicalNumberOfRows)
                sheet.forEachIndexed { ri, row ->
                    val testRow = testSheet.getRow(ri)
                    assertEquals(testRow.physicalNumberOfCells, row.physicalNumberOfCells)
                    row.forEachIndexed { ci, cell ->
                        when(cell.cellType){
                            CellType.NUMERIC -> assertEquals(
                                    testRow.getCell(ci)?.numericCellValue,
                                    cell.numericCellValue
                            )
                            CellType.STRING -> assertEquals(
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