package no.nav.pensjon.innsyn.common.repository

import no.nav.pensjon.innsyn.common.domain.Domain
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface DomainRepository<T: Domain> : CrudRepository<T, Int> {
    fun findAllByPersonId(id: Int): List<T>
}