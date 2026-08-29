package com.github.regyl.gfi.archunit;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.github.regyl.gfi.annotation.DefaultArchUnitTest;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noMethods;

@DefaultArchUnitTest
@SuppressWarnings("unused")
class ServiceArchTest {

    private static final String PACKAGE = "..service..";

    @ArchTest
    static final ArchRule servicesShouldNotDependOnControllers =
            noClasses()
                    .that().resideInAPackage(PACKAGE)
                    .should().dependOnClassesThat()
                    .resideInAPackage("..controller..")
                    .because("Services should not be dependent on controllers")
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule noStaticMethods =
            noMethods()
                    .that().areDeclaredInClassesThat().resideInAPackage(PACKAGE)
                    .should().beStatic()
                    .because("These classes should not declare static methods. Use util package")
                    .allowEmptyShould(true);
}