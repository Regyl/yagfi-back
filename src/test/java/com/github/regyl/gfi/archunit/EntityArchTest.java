package com.github.regyl.gfi.archunit;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.github.regyl.gfi.annotation.DefaultArchUnitTest;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noMethods;

@DefaultArchUnitTest
@SuppressWarnings("unused")
class EntityArchTest {

    private static final String PACKAGE = "..entity..";

    @ArchTest
    static final ArchRule namingTest =
            ArchRuleDefinition.classes()
                    .that().resideInAPackage(PACKAGE).and().areNotEnums().and().areNotInterfaces()
                    .should()
                    .haveSimpleNameEndingWith("Entity")
                    .orShould().haveSimpleNameEndingWith("EntityBuilder")
                    .orShould().haveSimpleNameEndingWith("EntityBuilderImpl")
                    .because("Entity should be called Entity")
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule noStaticMethods =
            noMethods()
                    .that()
                    .areDeclaredInClassesThat().resideInAPackage(PACKAGE)
                    .and().haveNameNotContaining("builder")
                    .and().haveNameNotContaining("$")
                    .and().areDeclaredInClassesThat().areNotEnums()
                    .should().beStatic()
                    .because("These classes should not declare static methods. Use util package")
                    .allowEmptyShould(true);
}
