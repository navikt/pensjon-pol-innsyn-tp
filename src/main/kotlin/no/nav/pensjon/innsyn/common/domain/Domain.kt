package no.nav.pensjon.innsyn.common.domain

import kotlin.reflect.KProperty

interface Domain {
    val fields: Set<KProperty<*>>
}