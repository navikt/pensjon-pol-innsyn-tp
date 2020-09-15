package no.nav.pensjon.innsyn.tp.domain

import no.nav.pensjon.innsyn.tp.domain.support.Person
import no.nav.pensjon.innsyn.tp.domain.support.TssTp
import java.time.LocalDate

object TpObjects {
    private val tssTp1  = TssTp(
            id = "11111111111",
            navn = "TP1"
    )


    private val ytelser = listOf(
            Ytelse(
                    id = 1,
                    datoInnmYtelseFom = LocalDate.of(1900,1,1),
                    datoYtelseIverFom = LocalDate.of(2001,1,1),
                    datoYtelseIverTom = null,
                    datoOpprettet = LocalDate.of(2001,1,1),
                    datoEndret = LocalDate.of(2001,1,1),
                    datoBrukFom = LocalDate.of(2001,1,1),
                    datoBrukTom = null
            ),
            Ytelse(
                    id = 2,
                    datoInnmYtelseFom = LocalDate.of(1900,1,1),
                    datoYtelseIverFom = LocalDate.of(2001,1,1),
                    datoYtelseIverTom = null,
                    datoOpprettet = LocalDate.of(2001,1,1),
                    datoEndret = LocalDate.of(2001,1,1),
                    datoBrukFom = LocalDate.of(2001,1,1),
                    datoBrukTom = null
            )
    )

    val person = Person(1, "01029312345")

    val forhold = listOf(
            Forhold(
                    id = 1,
                    personId = person.personId,
                    tssTp = tssTp1,
                    ytelser = ytelser,
                    datoSamtykkeGitt = null,
                    datoOpprettet = LocalDate.of(2001,1,1),
                    samtykkeSimT = "N",
                    datoBrukFom = LocalDate.of(2001,1,1),
                    datoBrukTom = null
            ).apply {
                ytelser.forEach { it.forhold = this }
            }
    )
}