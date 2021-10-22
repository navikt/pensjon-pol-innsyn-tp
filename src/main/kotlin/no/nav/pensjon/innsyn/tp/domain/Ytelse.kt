package no.nav.pensjon.innsyn.tp.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import no.nav.pensjon.innsyn.common.domain.Domain
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
    @JsonIgnore
    override val fields = setOf(
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