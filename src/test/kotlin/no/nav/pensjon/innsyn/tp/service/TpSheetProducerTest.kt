package no.nav.pensjon.innsyn.tp.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import no.nav.pensjon.innsyn.tp.assertEqualsTestData
import no.nav.pensjon.innsyn.tp.domain.TpObjects.forhold
import no.nav.pensjon.innsyn.tp.domain.TpObjects.person
import no.nav.pensjon.innsyn.tp.domain.container.ForholdContainer
import no.nav.pensjon.innsyn.tp.domain.container.YtelseContainer
import no.nav.pensjon.innsyn.tp.repository.ForholdRepository
import no.nav.pensjon.innsyn.tp.repository.PersonRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(classes = [TpSheetProducer::class, ForholdContainer::class, YtelseContainer::class])
@ActiveProfiles("TP")
internal class TpSheetProducerTest {
    @MockkBean
    lateinit var forholdRepository: ForholdRepository
    @MockkBean
    lateinit var personRepository: PersonRepository

    @Autowired
    private lateinit var tpSheetProducer: TpSheetProducer

    @Test
    fun `Produces TP poppSheet`() {
        every { forholdRepository.findAllByPersonId(person.personId) } returns forhold
        every { personRepository.findByFnr(person.fnr) } returns person
        tpSheetProducer.produceWorksheet(person.fnr).assertEqualsTestData()
    }
}