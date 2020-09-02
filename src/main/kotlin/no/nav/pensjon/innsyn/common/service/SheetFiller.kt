package no.nav.pensjon.innsyn.common.service

import no.nav.pensjon.innsyn.common.domain.Domain
import no.nav.pensjon.innsyn.common.domain.DomainContainer
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.IndexedColors.RED
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook

class SheetFiller<T : Domain> internal constructor(
        private val container: DomainContainer<T>
) {

    fun populateSheet(pid: Int, workbook: Workbook) {
        val sheet = workbook.createSheet(container.entityName)
        createHeaderRow(sheet, createHeaderCellStyle(workbook))
        createRows(pid, sheet, createDateCellStyle(workbook))
        fitColumnsToContentSize(sheet)
    }

    private fun createRows(pid: Int, sheet: Sheet, dateCellStyle: CellStyle) {
        container.source(pid).forEachIndexed { i, e ->
            container.rowFiller(RowFiller(sheet, dateCellStyle, i + 1), e)
        }
    }

    private fun createHeaderRow(sheet: Sheet, cellStyle: CellStyle) {
        createCells(sheet.createRow(0), cellStyle)
    }

    private fun createCells(row: Row, style: CellStyle) = container.propertyNames.forEachIndexed { i, o ->
        row.createCell(i).apply {
            setCellValue(o)
            cellStyle = style
        }
    }

    private fun fitColumnsToContentSize(sheet: Sheet) {
        (0..container.propertyNames.size).forEach(sheet::autoSizeColumn)
    }

    companion object {
        private const val DATE_FORMAT = "dd-MM-yyyy"
        private fun createHeaderCellStyle(workbook: Workbook) = workbook.createCellStyle().apply {
            setFont(createHeaderFont(workbook))
        }

        private fun createDateCellStyle(workbook: Workbook) = workbook.createCellStyle().apply {
            dataFormat = workbook.creationHelper.createDataFormat().getFormat(DATE_FORMAT)
        }

        private fun createHeaderFont(workbook: Workbook) = workbook.createFont().apply {
            bold = true
            fontHeightInPoints = 14
            color = RED.getIndex()
        }
    }

}