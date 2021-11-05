package no.nav.pensjon.innsyn.tp

import no.nav.security.mock.oauth2.MockOAuth2Server
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.support.GenericApplicationContext
import java.io.IOException
import java.util.function.Supplier


class MockOAuth2ServerInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        val server: MockOAuth2Server = registerMockOAuth2Server(applicationContext)
        val baseUrl = server.baseUrl().toString().replace("/$", "")
        TestPropertyValues
            .of(mapOf(MOCK_OAUTH_2_SERVER_BASE_URL to baseUrl))
            .applyTo(applicationContext)
    }

    private fun registerMockOAuth2Server(applicationContext: ConfigurableApplicationContext): MockOAuth2Server {
        return try {
            val server = MockOAuth2Server()
            server.start()
            (applicationContext as GenericApplicationContext).registerBean(
                MockOAuth2Server::class.java,
                Supplier { server })
            server
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    companion object {
        const val MOCK_OAUTH_2_SERVER_BASE_URL = "mock-oauth2-server.baseUrl"
    }
}