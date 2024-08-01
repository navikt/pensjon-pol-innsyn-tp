package no.nav.pensjon.innsyn.tp.domain

import kotlin.reflect.KProperty

interface Domain {
    fun fields(): Set<KProperty<*>>
}