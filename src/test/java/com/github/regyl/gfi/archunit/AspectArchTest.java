package com.github.regyl.gfi.archunit;

import com.github.regyl.gfi.annotation.DefaultArchUnitTest;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noMethods;

@DefaultArchUnitTest
@SuppressWarnings("unused")
class AspectArchTest {

    private static final String PACKAGE = "..aop..";

    @ArchTest
    static final ArchRule namingTest =
            ArchRuleDefinition.classes()
                    .that().resideInAPackage(PACKAGE)
                    .should()
                    .haveSimpleNameEndingWith("Aspect")
                    .because("Aspect should be called Aspect")
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule noStaticMethods =
            noMethods()
                    .that().areDeclaredInClassesThat().resideInAPackage(PACKAGE)
                    .should().beStatic()
                    .because("These classes should not declare static methods. Use util package")
                    .allowEmptyShould(true);
}
