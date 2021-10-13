package no.nav.pensjon.innsyn.common.service

import no.nav.pensjon.innsyn.common.domain.Domain
import no.nav.pensjon.innsyn.common.domain.DomainContainer
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.IndexedColors.RED
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import reactor.core.publisher.Flux

class SheetFiller<X : Domain, T : Domain> internal constructor(
    private val container: DomainContainer<X, T>
) {

    fun populateSheet(domain: Flux<X>, workbook: Workbook) {
        val sheet = workbook.createSheet(container.entityName)
        createHeaderRow(sheet, createHeaderCellStyle(workbook))
        createRows(domain, sheet, createDateCellStyle(workbook))
        fitColumnsToContentSize(sheet)
    }

    private fun createRows(domain: Flux<X>, sheet: Sheet, dateCellStyle: CellStyle) {
        container.map(domain).index().subscribe { t ->
            container.rowFiller(RowFiller(sheet, dateCellStyle, t.t1.toInt() + 1), t.t2)
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