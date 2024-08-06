package no.nav.pensjon.innsyn.tp.domain

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
    override fun fields() = setOf(
        ::navn,
        ::datoSamtykkeGitt,
        ::datoOpprettet,
        ::samtykkeSimT,
        ::datoBrukFom,
        ::datoBrukTom
    )
}