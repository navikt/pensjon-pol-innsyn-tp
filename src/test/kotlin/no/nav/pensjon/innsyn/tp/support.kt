package no.nav.pensjon.innsyn.tp

import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import kotlin.test.assertEquals
import kotlin.test.fail

fun XSSFWorkbook.assertEqualsTestData(){
    val testBook = XSSFWorkbook(FileInputStream(File("tp-test-worksheet.xlsx")))
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
                    else -> fail("Unexpected data type.")
                }
            }
        }
    }
}