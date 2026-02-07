package com.github.regyl.gfi.util;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;

import java.time.Duration;

@UtilityClass
public class LogUtil {

    public void log(long start, String action, Logger logger) {
        long seconds = Duration.ofNanos(System.nanoTime() - start).toSeconds();
        logger.info("{} took {} seconds", action, seconds);
    }
}
