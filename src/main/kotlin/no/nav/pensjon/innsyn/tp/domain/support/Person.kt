package no.nav.pensjon.innsyn.tp.domain.support

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = "T_PERSON")
data class Person(
        @Id
        @Column(name = "PERSON_ID")
        val personId: Int,
        @Column(name = "FNR_FK")
        val fnr: String
)