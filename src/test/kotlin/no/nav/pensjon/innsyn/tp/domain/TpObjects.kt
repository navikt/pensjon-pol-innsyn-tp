package no.nav.pensjon.innsyn.tp.domain

import java.time.LocalDate

object TpObjects {
    const val person = "01029312345"
    private val ytelser = listOf(
        Ytelse(
            navn = "TP1",
            datoInnmYtelseFom = LocalDate.of(1900, 1, 1),
            datoYtelseIverFom = LocalDate.of(2001, 1, 1),
            datoYtelseIverTom = null,
            datoOpprettet = LocalDate.of(2001, 1, 1),
            datoEndret = LocalDate.of(2001, 1, 1),
            datoBrukFom = LocalDate.of(2001, 1, 1),
            datoBrukTom = null
        ),
        Ytelse(
            navn = "TP1",
            datoInnmYtelseFom = LocalDate.of(1900, 1, 1),
            datoYtelseIverFom = LocalDate.of(2001, 1, 1),
            datoYtelseIverTom = null,
            datoOpprettet = LocalDate.of(2001, 1, 1),
            datoEndret = LocalDate.of(2001, 1, 1),
            datoBrukFom = LocalDate.of(2001, 1, 1),
            datoBrukTom = null
        )
    )

    val forhold = listOf(
        Forhold(
            navn = "TP1",
            ytelser = ytelser,
            datoSamtykkeGitt = null,
            datoOpprettet = LocalDate.of(2001, 1, 1),
            samtykkeSimT = "N",
            datoBrukFom = LocalDate.of(2001, 1, 1),
            datoBrukTom = null
        )
    )
}