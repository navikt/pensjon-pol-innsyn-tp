package no.nav.pensjon.innsyn.tp.repository

import no.nav.pensjon.innsyn.tp.domain.TpObjects
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.PropertySource
import kotlin.test.assertEquals

@DataJpaTest
class TpRepositoriesTest{
    @Autowired
    lateinit var forholdRepository: ForholdRepository

    @Test
    fun `Get all Beholdninger by PID`(){
        assertEquals(
                TpObjects.forhold,
                forholdRepository.findAllByPersonId(1)
        )
    }
}