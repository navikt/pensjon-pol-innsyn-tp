package no.nav.pensjon.innsyn.common

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class PersonNotFoundException :
    ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found. Verify FNR is correct.")