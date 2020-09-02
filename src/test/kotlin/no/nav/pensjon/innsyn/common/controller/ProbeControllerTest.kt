package no.nav.pensjon.innsyn.common.controller

import no.nav.pensjon.innsyn.common.controller.ProbeController
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(ProbeController::class)
internal class ProbeControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `Is Alive`() {
        mockMvc.get("/isAlive").andExpect {
            status { isOk }
        }
    }

    @Test
    fun `Is Ready`() {
        mockMvc.get("/isReady").andExpect {
            status { isOk }
        }
    }
}