package no.nav.pensjon.innsyn.tp

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.okForJson
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.common.Json
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import no.nav.pensjon.innsyn.tp.domain.Forhold
import no.nav.pensjon.innsyn.tp.domain.TpObjects.forhold
import no.nav.pensjon.innsyn.tp.domain.TpObjects.person
import no.nav.pensjon.innsyn.tp.service.AzureTokenService
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.io.ByteArrayInputStream

@SpringBootTest
@WireMockTest(httpPort = 8089)
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class TpInnsynTest {
    @Autowired
    private lateinit var context: WebApplicationContext

    @MockkBean
    private lateinit var azureTokenService: AzureTokenService

    private lateinit var mockMvc: MockMvc

    @BeforeAll
    fun setup() {
        Json.getObjectMapper().registerModule(JavaTimeModule())
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply<DefaultMockMvcBuilder>(springSecurity())
            .build()
    }

    @Test
    fun `Generates TP worksheet`() {
        every { azureTokenService.getOnBehalOfToken(any()) } returns "bogus"
        stubFor(
            get("/api/pol/$person")
                .willReturn(okForJson(forhold))
        )
        mockMvc.get("/api/innsyn/$person") {
            with(oidcLogin())
        }.andExpect {
            status { isOk() }
            content { contentType(CONTENT_TYPE_EXCEL) }
        }.andReturn().response.run {
            XSSFWorkbook(ByteArrayInputStream(contentAsByteArray))
        }.assertEqualsTestData()
    }

    @Test
    fun `Handles missing data`() {
        every { azureTokenService.getOnBehalOfToken(any()) } returns "bogus"
        stubFor(
            get("/api/pol/00000000000")
                .willReturn(okForJson(emptyList<Forhold>()))
        )
        mockMvc.get("/api/innsyn/00000000000") {
            with(oidcLogin())
        }.andExpect {
            status {
                isOk()
            }
        }
    }

    @Test
    fun `Handles not found response`() {
        every { azureTokenService.getOnBehalOfToken(any()) } returns "bogus"
        stubFor(
            get("/api/pol/11111111111")
                .willReturn(notFound())
        )
        mockMvc.get("/api/innsyn/11111111111") {
            with(oidcLogin())
        }.andExpect {
            status {
                isOk()
            }
        }
    }

    @Test
    fun `Handles error response`() {
        every { azureTokenService.getOnBehalOfToken(any()) } returns "bogus"
        stubFor(
            get("/api/pol/11111111111")
                .willReturn(serviceUnavailable())
        )
        mockMvc.get("/api/innsyn/11111111111") {
            with(oidcLogin())
        }.andExpect {
            status {
                isBadGateway()
                reason("Error fetching data from TP. Status code: 503 SERVICE_UNAVAILABLE")
            }
        }
    }

    @Test
    fun `Redirects unauthorized to login`() {
        mockMvc.get("/api/innsyn/$person")
            .andExpect {
                status { is3xxRedirection() }
            }
    }
}
