package no.nav.pensjon.innsyn.tp.domain.support

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "T_TSS_TP")
data class TssTp(
        @Id
        @Column(name = "TSS_ID")
        val id: String,
        @Column(name = "NAVN")
        val navn: String
)