package no.nav.pensjon.innsyn

import no.nav.pensjon.innsyn.common.CONTENT_TYPE_EXCEL
import no.nav.pensjon.innsyn.tp.assertEqualsTestData
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation
import no.nav.security.token.support.test.JwtTokenGenerator
import no.nav.security.token.support.test.spring.TokenGeneratorConfiguration
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.io.ByteArrayInputStream

@SpringBootTest
@AutoConfigureMockMvc
@EnableJwtTokenValidation
@Import(TokenGeneratorConfiguration::class)
internal class TpInnsynTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `Generates TP worksheet`() {
        mockMvc.get("/innsyn") {
            headers {
                this["pid"] = "1"
                setBearerAuth(JwtTokenGenerator.signedJWTAsString(null))
            }
        }.andExpect {
            status { isOk }
            content { contentType(CONTENT_TYPE_EXCEL) }
        }.andReturn().response.run {
            XSSFWorkbook(ByteArrayInputStream(contentAsByteArray))
        }.assertEqualsTestData()
    }

    @Test
    fun `Denies unauthorized`() {
        mockMvc.get("/innsyn") {
            headers {
                this["pid"] = "1"
            }
        }.andExpect {
            status { isUnauthorized }
        }
    }

    @Test
    @Disabled("No exception thrown on empty datasets, possible implementation required depending on PID handling.")
    fun `Handles missing data`(){
        mockMvc.get("/innsyn") {
            headers {
                this["pid"] = "2"
            }
        }.andExpect {
            status { isNotFound }
            content { string("") }
        }
    }
}