package com.github.regyl.gfi.repository;

import com.github.regyl.gfi.annotation.ArchUnitTest;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@ArchUnitTest
class RepositoryArchTest {

    @ArchTest
    static final ArchRule repositoriesShouldNotDependOnOtherLayers =
            noClasses()
                    .that().resideInAPackage("..repository..")
                    .should().dependOnClassesThat()
                    .resideInAnyPackage(
                            "..service..",
                            "..mapper..",
                            "..feign..",
                            "..configuration.."
                    )
                    .because("Repositories should not depend on service, mapper, feign or configuration layers");
}
