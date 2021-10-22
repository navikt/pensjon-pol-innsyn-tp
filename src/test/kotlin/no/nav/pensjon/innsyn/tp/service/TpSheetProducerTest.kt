package no.nav.pensjon.innsyn.tp.service

import no.nav.pensjon.innsyn.tp.assertEqualsTestData
import no.nav.pensjon.innsyn.tp.domain.TpObjects.forhold
import no.nav.pensjon.innsyn.tp.domain.container.ForholdContainer
import no.nav.pensjon.innsyn.tp.domain.container.YtelseContainer
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(classes = [TpSheetProducer::class, ForholdContainer::class, YtelseContainer::class])
@ActiveProfiles("TP")
internal class TpSheetProducerTest {

    @Autowired
    private lateinit var tpSheetProducer: TpSheetProducer

    @Test
    fun `Produces TP poppSheet`() {
        tpSheetProducer.produceWorksheet(forhold).assertEqualsTestData()
    }
}