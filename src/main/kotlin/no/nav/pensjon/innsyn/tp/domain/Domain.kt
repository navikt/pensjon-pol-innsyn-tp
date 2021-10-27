package no.nav.pensjon.innsyn.tp.domain

import kotlin.reflect.KProperty

interface Domain {
    val fields: Set<KProperty<*>>
}