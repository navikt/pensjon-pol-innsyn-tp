package no.nav.pensjon.innsyn.tp.domain

import no.nav.pensjon.innsyn.common.domain.Domain
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "T_YTELSE")
data class Ytelse(
        @Id
        @Column(name = "YTELSE_ID")
        val id: Int,
        @Column(name = "DATO_INNM_YTEL_FOM")
        val datoInnmYtelseFom: LocalDate,
        @Column(name = "DATO_YTEL_IVER_FOM")
        val datoYtelseIverFom: LocalDate,
        @Column(name = "DATO_YTEL_IVER_TOM")
        val datoYtelseIverTom: LocalDate?,
        @Column(name = "DATO_OPPRETTET")
        val datoOpprettet: LocalDate,
        @Column(name = "DATO_ENDRET")
        val datoEndret: LocalDate?,
        @Column(name = "DATO_BRUK_FOM")
        val datoBrukFom: LocalDate,
        @Column(name = "DatoBrukTom")
        val datoBrukTom: LocalDate?
) : Domain {
    @ManyToOne
    @JoinTable(name = "T_FORHOLD_YTELSE_HISTORIKK",
            inverseJoinColumns = [JoinColumn(name = "FORHOLD_ID_FK")],
            joinColumns = [JoinColumn(name = "YTELSE_ID_FK")]
    )
    lateinit var forhold: Forhold

    @get:Transient
    val navn
        get() = forhold.tssTp.navn

    @get:Transient
    override val fields
        get() = setOf(::navn, ::datoInnmYtelseFom, ::datoYtelseIverFom, ::datoYtelseIverTom, ::datoOpprettet, ::datoEndret, ::datoBrukFom, ::datoBrukTom)
}