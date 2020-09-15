package no.nav.pensjon.innsyn.tp.repository

import no.nav.pensjon.innsyn.tp.domain.support.Person
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonRepository : CrudRepository<Person, Int> {
    fun findByFnr(fnr: String): Person?
}