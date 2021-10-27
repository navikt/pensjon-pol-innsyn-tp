package no.nav.pensjon.innsyn.tp.service

import java.time.LocalDate

interface CellValueSetter {
    fun setCellValue(cellIndex: Int, value: Boolean)
    fun setCellValue(cellIndex: Int, value: LocalDate)
    fun setCellValue(cellIndex: Int, value: Double)
    fun setCellValue(cellIndex: Int, value: String)
}