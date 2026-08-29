package com.github.regyl.gfi.archunit;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.github.regyl.gfi.annotation.DefaultArchUnitTest;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noMethods;

@DefaultArchUnitTest
@SuppressWarnings("unused")
class FeignArchTest {

    private static final String PACKAGE = "..feign..";

    @ArchTest
    static final ArchRule namingTest =
            ArchRuleDefinition.classes()
                    .that().resideInAPackage(PACKAGE)
                    .and().resideOutsideOfPackage("..configuration.feign..")
                    .should()
                    .haveSimpleNameEndingWith("Client")
                    .because("Model should be called Model")
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule feignClientsShouldNotDependOnOtherLayers =
            noClasses()
                    .that().resideInAPackage(PACKAGE)
                    .and().resideOutsideOfPackage("..configuration.feign..")
                    .should().dependOnClassesThat()
                    .resideInAnyPackage(
                            "..service..",
                            "..mapper..",
                            PACKAGE
                    )
                    .because("Feign clients should not depend on service, mapper or feign layers")
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule interfaceTest =
            ArchRuleDefinition.classes()
                    .that().resideInAPackage(PACKAGE)
                    .and().resideOutsideOfPackage("..configuration.feign..")
                    .should().beInterfaces()
                    .because("All members should be interfaces")
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule noStaticMethods =
            noMethods()
                    .that().areDeclaredInClassesThat().resideInAPackage(PACKAGE)
                    .should().beStatic()
                    .because("These classes should not declare static methods. Use util package")
                    .allowEmptyShould(true);
}