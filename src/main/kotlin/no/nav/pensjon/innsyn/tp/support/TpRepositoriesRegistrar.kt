package no.nav.pensjon.innsyn.tp.support

import org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension
import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport
import org.springframework.data.repository.config.RepositoryConfigurationExtension

internal class TpRepositoriesRegistrar : RepositoryBeanDefinitionRegistrarSupport() {

    override fun getAnnotation(): Class<out Annotation?> {
        return EnableTpRepositories::class.java
    }

    override fun getExtension(): RepositoryConfigurationExtension {
        return JpaRepositoryConfigExtension()
    }
}