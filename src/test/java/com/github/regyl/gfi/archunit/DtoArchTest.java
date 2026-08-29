package com.github.regyl.gfi.archunit;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.github.regyl.gfi.annotation.DefaultArchUnitTest;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noMethods;

@DefaultArchUnitTest
@SuppressWarnings("unused")
class DtoArchTest {

    private static final String PACKAGE = "..dto..";

    @ArchTest
    static final ArchRule namingTest =
            ArchRuleDefinition.classes()
                    .that().resideInAPackage("..dto..").and().areNotEnums()
                    .should()
                    .haveSimpleNameEndingWith("Dto")
                    .orShould().haveSimpleNameEndingWith("DtoBuilder")
                    .orShould().haveSimpleNameEndingWith("DtoBuilderImpl")
                    .because("Dto should be called Dto")
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule noStaticMethods =
            noMethods()
                    .that().areDeclaredInClassesThat().resideInAPackage(PACKAGE)
                    .and().areDeclaredInClassesThat().areNotEnums()
                    .and().haveNameNotContaining("builder")
                    .and().haveNameNotContaining("$")
                    .should().beStatic()
                    .because("These classes should not declare static methods. Use util package")
                    .allowEmptyShould(true);
}
