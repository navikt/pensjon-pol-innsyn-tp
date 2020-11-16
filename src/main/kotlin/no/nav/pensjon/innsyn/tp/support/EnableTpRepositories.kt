package no.nav.pensjon.innsyn.tp.support

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean
import org.springframework.data.repository.config.BootstrapMode
import org.springframework.data.repository.config.DefaultRepositoryBaseClass
import org.springframework.data.repository.query.QueryLookupStrategy
import java.lang.annotation.Inherited
import kotlin.reflect.KClass

@Suppress("unused")
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@Import(TpRepositoriesRegistrar::class)
annotation class EnableTpRepositories(
        vararg val value: String = [],

        val basePackages: Array<String> = [],

        val basePackageClasses: Array<KClass<*>> = [],

        val includeFilters: Array<ComponentScan.Filter> = [],

        val excludeFilters: Array<ComponentScan.Filter> = [],

        val repositoryImplementationPostfix: String = "Impl",

        val namedQueriesLocation: String = "",

        val queryLookupStrategy: QueryLookupStrategy.Key = QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND,

        val repositoryFactoryBeanClass: KClass<*> = JpaRepositoryFactoryBean::class,

        val repositoryBaseClass: KClass<*> = DefaultRepositoryBaseClass::class,  // JPA specific configuration

        val entityManagerFactoryRef: String = "entityManagerFactory",

        val transactionManagerRef: String = "transactionManager",

        val considerNestedRepositories: Boolean = false,

        val enableDefaultTransactions: Boolean = true,

        val bootstrapMode: BootstrapMode = BootstrapMode.DEFAULT,

        val escapeCharacter: Char = '\\')