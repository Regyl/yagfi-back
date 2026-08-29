package com.github.regyl.gfi.archunit;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.github.regyl.gfi.annotation.DefaultArchUnitTest;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noMethods;

@DefaultArchUnitTest
@SuppressWarnings("unused")
class ModelArchTest {

    private static final String PACKAGE = "..model..";

    @ArchTest
    static final ArchRule namingTest =
            ArchRuleDefinition.classes()
                    .that().resideInAPackage(PACKAGE).and().areNotEnums()
                    .should()
                    .haveSimpleNameEndingWith("Model")
                    .orShould().haveSimpleNameEndingWith("ModelBuilder")
                    .orShould().haveSimpleNameEndingWith("ModelBuilderImpl")
                    .orShould().haveSimpleNameEndingWith("Constant")
                    .because("Model should be called Model")
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule noStaticMethods =
            noMethods()
                    .that()
                    .areDeclaredInClassesThat().resideInAPackage(PACKAGE)
                    .and().haveNameNotContaining("builder")
                    .and().areDeclaredInClassesThat().areNotEnums()
                    .should().beStatic()
                    .because("These classes should not declare static methods. Use util package")
                    .allowEmptyShould(true);
}
