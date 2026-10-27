package no.nav.pensjon.innsyn.tp

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.okForJson
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import com.github.tomakehurst.wiremock.matching.EqualToPattern
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import no.nav.pensjon.innsyn.tp.controller.FNR
import no.nav.pensjon.innsyn.tp.domain.Forhold
import no.nav.pensjon.innsyn.tp.domain.TpObjects.forhold
import no.nav.pensjon.innsyn.tp.domain.TpObjects.person
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.io.ByteArrayInputStream
import java.time.Instant

@SpringBootTest
@WireMockTest(httpPort = 8089)
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class TpInnsynTest {
    @Autowired
    private lateinit var context: WebApplicationContext

    @MockkBean
    private lateinit var oAuth2AuthorizedClientRepository: OAuth2AuthorizedClientRepository

    @MockkBean
    private lateinit var oAuth2AuthorizedClient: OAuth2AuthorizedClient

    private lateinit var mockMvc: MockMvc

    @BeforeAll
    fun setup() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply<DefaultMockMvcBuilder>(springSecurity())
            .build()
    }

    @BeforeEach
    fun mock() {
        every { oAuth2AuthorizedClientRepository.loadAuthorizedClient<OAuth2AuthorizedClient>(any(), any(), any()) } returns oAuth2AuthorizedClient
        every { oAuth2AuthorizedClient.accessToken } returns OAuth2AccessToken(
            OAuth2AccessToken.TokenType.BEARER,
            "token",
            Instant.now(),
            Instant.now().plusSeconds(50)
        )
    }

    @Test
    fun `Generates TP worksheet`() {
        stubFor(
            get("/api/pol").withHeader(FNR, EqualToPattern(person))
                .willReturn(okForJson(forhold))
        )
        mockMvc.get("/api/innsyn") {
            header(FNR, person
            )
            with(oauth2Login())
        }.andExpect {
            status { isOk() }
            content { contentType(CONTENT_TYPE_EXCEL) }
        }.andReturn().response.run {
            XSSFWorkbook(ByteArrayInputStream(contentAsByteArray))
        }.assertEqualsTestData()
    }

    @Test
    fun `Handles missing data`() {
        stubFor(
            get("/api/pol").withHeader(FNR, EqualToPattern("00000000000"))
                .willReturn(okForJson(emptyList<Forhold>()))
        )
        mockMvc.get("/api/innsyn") {
            header(FNR, "00000000000")
            with(oauth2Login())
        }.andExpect {
            status {
                isOk()
            }
        }
    }

    @Test
    fun `Handles not found response`() {
        stubFor(
            get("/api/pol").withHeader(FNR, EqualToPattern("11111111111"))
                .willReturn(notFound())
        )
        mockMvc.get("/api/innsyn") {
            header(FNR, "11111111111")
            with(oauth2Login())
        }.andExpect {
            status {
                isBadGateway()
            }
        }
    }

    @Test
    fun `Handles error response`() {
        stubFor(
            get("/api/pol").withHeader(FNR, EqualToPattern("11111111111"))
                .willReturn(serviceUnavailable())
        )
        mockMvc.get("/api/innsyn") {
            header(FNR, "11111111111")
            with(oauth2Login())
        }.andExpect {
            status {
                isBadGateway()
            }
        }
    }

    @Test
    fun `Redirects unauthorized to login`() {
        mockMvc.get("/api/innsyn") {
            header(FNR, person)
        }.andExpect {
            status { is3xxRedirection() }
        }
    }
}
