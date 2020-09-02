package no.nav.pensjon.innsyn.common.service

import io.mockk.mockk
import io.mockk.verifySequence
import no.nav.pensjon.innsyn.common.domain.Domain
import org.junit.jupiter.api.Test
import java.time.LocalDate

class DomainRowFillerTest {
    object TestObject: Domain{
        val a = 1.0
        val b = "b"
        val c = LocalDate.of(1,1,1)

        override val fields = setOf(::a,::b,::c)
    }

    @Test
    fun `Sets all rows in order`() {
        val sink = mockk<CellValueSetter>(relaxed = true)
        DomainRowFiller<TestObject>().setCellValues(sink, TestObject)
        sink.apply {
            verifySequence {
                setCellValue(0, 1.0)
                setCellValue(1, "b")
                setCellValue(2, LocalDate.of(1,1,1))
            }
        }
    }
}