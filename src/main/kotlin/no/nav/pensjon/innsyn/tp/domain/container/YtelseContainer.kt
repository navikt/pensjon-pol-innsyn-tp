package no.nav.pensjon.innsyn.tp.domain.container

import no.nav.pensjon.innsyn.tp.domain.Forhold
import no.nav.pensjon.innsyn.tp.domain.Ytelse
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
class YtelseContainer(forholdContainer: ForholdContainer) : TpContainer<Ytelse>("Ytelse",
        arrayOf(
                "NAVN",
                "DATO_INNM_YTEL_FOM",
                "DATO_YTEL_IVER_FOM",
                "DATO_YTEL_IVER_TOM",
                "DATO_OPPRETTET",
                "DATO_ENDRET",
                "DATO_BRUK_FOM",
                "DATO_BRUK_TOM"
        ),
        { pid: Int -> forholdContainer.source(pid).flatMap(Forhold::ytelser) }
)