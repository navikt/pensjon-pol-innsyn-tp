package no.nav.pensjon.innsyn.common.controller

import io.prometheus.client.Counter
import no.nav.pensjon.innsyn.common.PersonNotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class InnsynExceptionHandler {

    @ExceptionHandler(PersonNotFoundException::class)
    @ResponseStatus(NOT_FOUND)
    @ResponseBody
    fun personNotFound() = "Person not found. Verify FNR is correct."

    @ExceptionHandler(Throwable::class)
    fun logError(e: Throwable) {
        if (e !is PersonNotFoundException) {
            LOG.error("Internal server error", e)
            errorCounter.labels(e::class.simpleName).inc()
        }
        throw e
    }

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(InnsynExceptionHandler::class.java)
        val errorCounter: Counter = Counter.build()
            .help("Interne feil kastet av POL-innsyn.")
            .namespace("pol_innsyn")
            .name("internal_server_errors_total")
            .labelNames("cause")
            .register()
    }
}