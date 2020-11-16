package no.nav.pensjon.innsyn.tp.domain

import no.nav.pensjon.innsyn.common.domain.Domain
import no.nav.pensjon.innsyn.tp.domain.support.TssTp
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "T_FORHOLD")
data class Forhold(
        @Id
        @Column(name = "FORHOLD_ID")
        val id: Int,
        @Column(name = "PERSON_ID")
        val personId: Int,
        @OneToMany(mappedBy = "forhold")
        val ytelser: List<Ytelse>,
        @ManyToOne
        @JoinColumn(name = "TSS_EKSTERN_ID_FK")
        val tssTp: TssTp,
        @Column(name = "DATO_SAMTYKKE_GITT")
        val datoSamtykkeGitt: LocalDate?,
        @Column(name = "DATO_OPPRETTET")
        val datoOpprettet: LocalDate,
        @Column(name = "K_SAMTYKKE_SIM_T")
        val samtykkeSimT: String,
        @Column(name = "DATO_BRUK_FOM")
        val datoBrukFom: LocalDate,
        @Column(name = "DatoBrukTom")
        val datoBrukTom: LocalDate?
) : Domain {
    @get:Transient
    val navn
        get() = tssTp.navn

    @get:Transient
    override val fields
        get() = setOf(::navn, ::datoSamtykkeGitt, ::datoOpprettet, ::samtykkeSimT, ::datoBrukFom, ::datoBrukTom)
}