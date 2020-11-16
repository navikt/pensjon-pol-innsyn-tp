package no.nav.pensjon.innsyn.tp.domain.container

import no.nav.pensjon.innsyn.tp.domain.Forhold
import no.nav.pensjon.innsyn.tp.repository.ForholdRepository
import org.springframework.stereotype.Component

@Component
class ForholdContainer(repository: ForholdRepository) : TpContainer<Forhold>("Forhold",
        arrayOf(
                "NAVN",
                "DATO_SAMTYKKE_GITT",
                "DATO_OPPRETTET",
                "K_SAMTYKKE_SIM_T",
                "DATO_BRUK_FOM",
                "DATO_BRUK_TOM"
        ),
        repository::findAllByPersonId
)