package no.nav.pensjon.innsyn.tp.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDate

data class Forhold(
    val navn: String,
    val datoSamtykkeGitt: LocalDate?,
    val datoOpprettet: LocalDate,
    val samtykkeSimT: String,
    val datoBrukFom: LocalDate,
    val datoBrukTom: LocalDate?,
    val ytelser: List<Ytelse>
) : Domain {
    @JsonIgnore
    override val fields = setOf(
        ::navn,
        ::datoSamtykkeGitt,
        ::datoOpprettet,
        ::samtykkeSimT,
        ::datoBrukFom,
        ::datoBrukTom
    )
}