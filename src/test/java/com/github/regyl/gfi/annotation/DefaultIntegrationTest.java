package com.github.regyl.gfi.annotation;

import com.github.regyl.gfi.model.Tags;
import org.junit.jupiter.api.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Tag(Tags.INTEGRATION)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DefaultIntegrationTest {
}
