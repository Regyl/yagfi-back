package com.github.regyl.gfi.archunit;

import com.github.regyl.gfi.exception.NonRetryableException;
import com.github.regyl.gfi.exception.RetryableException;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.github.regyl.gfi.annotation.DefaultArchUnitTest;

@DefaultArchUnitTest
@SuppressWarnings("unused")
class ExceptionArchTest {

    @ArchTest
    static final ArchRule instanceOfAnnotationTest =
            ArchRuleDefinition.classes()
                    .that().resideInAPackage("..exception..")
                    .and().resideOutsideOfPackage("..configuration.exception..")
                    .should().beAssignableTo(RetryableException.class)
                    .orShould().beAssignableTo(NonRetryableException.class)
                    .because("All util classes should have private constructors")
                    .allowEmptyShould(true);
}
