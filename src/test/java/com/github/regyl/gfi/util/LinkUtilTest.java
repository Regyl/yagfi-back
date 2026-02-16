package com.github.regyl.gfi.util;


import com.github.regyl.gfi.annotation.DefaultUnitTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

@DefaultUnitTest
class LinkUtilTest {

    @ParameterizedTest
    @MethodSource("source")
    void test(String source, String target) {
        String result = LinkUtil.normalizeRepositoryUrl(source);

        Assertions.assertThat(result).isEqualTo(target);
    }

    static Stream<Arguments> source() {
        return Stream.of(
                Arguments.of("git+https://github.com/emotion-js/emotion.git#main", "https://github.com/emotion-js/emotion"),
                Arguments.of("git@github.com:micrometer-metrics/micrometer.git", "https://github.com/micrometer-metrics/micrometer"),
                Arguments.of("scm:git:git://github.com/HdrHistogram/HdrHistogram.git", "https://github.com/HdrHistogram/HdrHistogram"),
                Arguments.of("http://github.com/FasterXML/jackson-core", "https://github.com/FasterXML/jackson-core"),
                Arguments.of("git+ssh://git@github.com/webpack/tapable.git", "https://github.com/webpack/tapable"),
                Arguments.of("git://github.com/lovell/sharp.git", "https://github.com/lovell/sharp"),
                Arguments.of("git+https://github.com/mdx-js/recma.git#main", "https://github.com/mdx-js/recma"),
                Arguments.of("git://github.com/ckknight/random-js", "https://github.com/ckknight/random-js"),
                Arguments.of("https://github.com/nodelib/nodelib/tree/master/packages/fs/fs.scandir", "https://github.com/nodelib/nodelib"),
                Arguments.of("scm:git@github.com:Netflix/netflix-commons", "https://github.com/Netflix/netflix-commons"),
                Arguments.of("git@github.com:cdi-spec/cdi", "https://github.com/cdi-spec/cdi"),
                Arguments.of("https://github.com/jboss-logging/jboss-logging/tree/main/", "https://github.com/jboss-logging/jboss-logging"),
                Arguments.of("https://github.com/mybatis/spring/", "https://github.com/mybatis/spring"),
                Arguments.of("https://github.com/RustCrypto/formats/tree/master/pkcs1", "https://github.com/RustCrypto/formats"),
                Arguments.of("https://github.com/emotion-js/emotion/tree/main/packages/react", "https://github.com/emotion-js/emotion")
        );
    }
}
