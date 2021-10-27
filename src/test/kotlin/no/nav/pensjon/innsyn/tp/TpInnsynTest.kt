package no.nav.pensjon.innsyn.tp

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.okForJson
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.common.Json
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import com.github.tomakehurst.wiremock.matching.EqualToPattern
import no.nav.pensjon.innsyn.tp.CONTENT_TYPE_EXCEL
import no.nav.pensjon.innsyn.tp.assertEqualsTestData
import no.nav.pensjon.innsyn.tp.domain.Forhold
import no.nav.pensjon.innsyn.tp.domain.TpObjects.forhold
import no.nav.pensjon.innsyn.tp.domain.TpObjects.person
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation
import no.nav.security.token.support.test.JwtTokenGenerator
import no.nav.security.token.support.test.spring.TokenGeneratorConfiguration
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.io.ByteArrayInputStream

@SpringBootTest
@WireMockTest(httpPort = 8089)
@AutoConfigureMockMvc
@EnableJwtTokenValidation
@Import(TokenGeneratorConfiguration::class)
internal class TpInnsynTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `Generates TP worksheet`() {
        Json.getObjectMapper().registerModule(JavaTimeModule())
        val token = JwtTokenGenerator.signedJWTAsString(null)
        stubFor(
            get("/api/pol/$person")
                .withHeader("Authorization", EqualToPattern("Bearer $token"))
                .willReturn(okForJson(forhold))
        )
        mockMvc.get("/innsyn/$person") {
            headers {
                setBearerAuth(token)
            }
        }.andExpect {
            status { isOk() }
            content { contentType(CONTENT_TYPE_EXCEL) }
        }.andReturn().response.run {
            XSSFWorkbook(ByteArrayInputStream(contentAsByteArray))
        }.assertEqualsTestData()
    }

    @Test
    fun `Handles missing data`() {
        Json.getObjectMapper().registerModule(JavaTimeModule())
        val token = JwtTokenGenerator.signedJWTAsString(null)
        stubFor(
            get("/api/pol/0")
                .withHeader("Authorization", EqualToPattern("Bearer $token"))
                .willReturn(okForJson(emptyList<Forhold>()))
        )
        mockMvc.get("/innsyn/0") {
            headers {
                setBearerAuth(token)
            }
        }.andExpect {
            status {
                isOk()
            }
        }
    }

    @Test
    fun `Handles error response`() {
        Json.getObjectMapper().registerModule(JavaTimeModule())
        val token = JwtTokenGenerator.signedJWTAsString(null)
        stubFor(
            get("/api/pol/1")
                .withHeader("Authorization", EqualToPattern("Bearer $token"))
                .willReturn(serviceUnavailable())
        )
        mockMvc.get("/innsyn/1") {
            headers {
                setBearerAuth(token)
            }
        }.andExpect {
            status {
                isBadGateway()
                reason("Error fetching data from TP. Status code: 503 SERVICE_UNAVAILABLE")
            }
        }
    }

    @Test
    fun `Denies unauthorized`() {
        mockMvc.get("/innsyn/$person")
            .andExpect {
                status { isUnauthorized() }
            }
    }
}