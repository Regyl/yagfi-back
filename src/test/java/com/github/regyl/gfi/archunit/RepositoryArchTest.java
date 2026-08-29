package com.github.regyl.gfi.archunit;

import com.github.regyl.gfi.annotation.DefaultMyBatisMapper;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.github.regyl.gfi.annotation.DefaultArchUnitTest;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noMethods;

@DefaultArchUnitTest
@SuppressWarnings("unused")
class RepositoryArchTest {

    private static final String PACKAGE = "..repository..";

    @ArchTest
    static final ArchRule repositoriesShouldNotDependOnOtherLayers =
            noClasses()
                    .that().resideInAPackage(PACKAGE)
                    .should().dependOnClassesThat()
                    .resideInAnyPackage(
                            "..service..",
                            "..mapper..",
                            "..feign..",
                            "..configuration.."
                    )
                    .because("Repositories should not depend on service, mapper, feign or configuration layers")
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule repositoryAnnotationTest =
            ArchRuleDefinition.classes()
                    .that().resideInAPackage(PACKAGE).and().haveSimpleNameNotStartingWith("Abstract")
                    .should().beAnnotatedWith(DefaultMyBatisMapper.class)
                    .because("All repositories should be annotated with @DefaultMyBatisMapper")
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule interfaceTest =
            ArchRuleDefinition.classes()
                    .that().resideInAPackage(PACKAGE)
                    .should().beInterfaces()
                    .because("All members should be interfaces")
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule namingTest =
            ArchRuleDefinition.classes()
                    .that().resideInAPackage(PACKAGE)
                    .should()
                    .haveSimpleNameEndingWith("Repository")
                    .because("Repository should be called Repository")
                    .allowEmptyShould(true);

    @ArchTest
    static final ArchRule noStaticMethods =
            noMethods()
                    .that().areDeclaredInClassesThat().resideInAPackage(PACKAGE)
                    .should().beStatic()
                    .because("These classes should not declare static methods. Use util package")
                    .allowEmptyShould(true);
}