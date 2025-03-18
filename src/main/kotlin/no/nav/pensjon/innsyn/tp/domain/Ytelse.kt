package no.nav.pensjon.innsyn.tp.domain

import java.time.LocalDate

data class Ytelse(
    val navn: String,
    val datoInnmYtelseFom: LocalDate,
    val datoYtelseIverFom: LocalDate,
    val datoYtelseIverTom: LocalDate?,
    val datoOpprettet: LocalDate,
    val datoEndret: LocalDate?,
    val datoBrukFom: LocalDate,
    val datoBrukTom: LocalDate?
) : Domain {
    override fun fields() = setOf(
        ::navn,
        ::datoInnmYtelseFom,
        ::datoYtelseIverFom,
        ::datoYtelseIverTom,
        ::datoOpprettet,
        ::datoEndret,
        ::datoBrukFom,
        ::datoBrukTom
    )
}