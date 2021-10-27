package no.nav.pensjon.innsyn.tp.service

import io.mockk.mockk
import io.mockk.verifySequence
import no.nav.pensjon.innsyn.tp.domain.Domain
import org.junit.jupiter.api.Test
import java.time.LocalDate

class DomainRowFillerTest {
    @Suppress("MemberVisibilityCanBePrivate")
    object TestObject: Domain{
        const val a = 1.0
        const val b = "b"
        val c: LocalDate = LocalDate.of(1,1,1)

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