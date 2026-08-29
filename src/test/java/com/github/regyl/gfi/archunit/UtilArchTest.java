package com.github.regyl.gfi.archunit;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.github.regyl.gfi.annotation.DefaultArchUnitTest;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@DefaultArchUnitTest
@SuppressWarnings("unused")
class UtilArchTest {

    @ArchTest
    static final ArchRule servicesShouldNotDependOnControllers =
            noClasses()
                    .that().resideInAPackage("..util..")
                    .should().dependOnClassesThat()
                    .resideInAnyPackage(
                            "..service..",
                            "..mapper..",
                            "..feign..",
                            "..configuration..",
                            "..controller..",
                            "..repository.."
                    )
                    .because("Util should be standalone")
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule utilityClassAnnotationTest =
            ArchRuleDefinition.classes()
                    .that().resideInAPackage("..util..")
                    .should().haveOnlyPrivateConstructors()
                    .because("All util classes should have private constructors")
                    .allowEmptyShould(true);
}