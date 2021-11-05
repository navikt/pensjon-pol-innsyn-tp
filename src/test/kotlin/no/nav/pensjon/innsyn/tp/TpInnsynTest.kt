package no.nav.pensjon.innsyn.tp

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.okForJson
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.common.Json
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import com.github.tomakehurst.wiremock.matching.EqualToPattern
import no.nav.pensjon.innsyn.tp.domain.Forhold
import no.nav.pensjon.innsyn.tp.domain.TpObjects.forhold
import no.nav.pensjon.innsyn.tp.domain.TpObjects.person
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.io.ByteArrayInputStream

@SpringBootTest
@WireMockTest(httpPort = 8089)
@AutoConfigureMockMvc
internal class TpInnsynTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setup() {
        Json.getObjectMapper().registerModule(JavaTimeModule())
    }

    @Test
    @WithMockUser
    fun `Generates TP worksheet`() {
        stubFor(
            get("/api/pol/$person")
                .willReturn(okForJson(forhold))
        )
        mockMvc.get("/api/innsyn/$person")
            .andExpect {
                status { isOk() }
                content { contentType(CONTENT_TYPE_EXCEL) }
            }.andReturn().response.run {
                XSSFWorkbook(ByteArrayInputStream(contentAsByteArray))
            }.assertEqualsTestData()
    }

    @Test
    @WithMockUser
    fun `Handles missing data`() {
        stubFor(
            get("/api/pol/0")
                .willReturn(okForJson(emptyList<Forhold>()))
        )
        mockMvc.get("/api/innsyn/0")
            .andExpect {
                status {
                    isOk()
                }
            }
    }

    @Test
    @WithMockUser
    fun `Handles error response`() {
        stubFor(
            get("/api/pol/1")
                .willReturn(serviceUnavailable())
        )
        mockMvc.get("/api/innsyn/1")
            .andExpect {
                status {
                    isBadGateway()
                    reason("Error fetching data from TP. Status code: 503 SERVICE_UNAVAILABLE")
                }
            }
    }

    @Test
    fun `Denies unauthorized`() {
        mockMvc.get("/api/innsyn/$person")
            .andExpect {
                status { isUnauthorized() }
            }
    }
}