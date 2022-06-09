package com.test.my.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomLogger {

    /**
     * This is constructor.
     */
    private CustomLogger() {
        // keep this empty
    }

    /**
     * Construct a new logger object.
     * @param className the name of the class
     * @return Logger obj
     */
    public static Logger getLogger(Class className) {
        Logger logger = LoggerFactory.getLogger(className);
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        LoggerConfig rootLoggerConfig = config.getLoggerConfig("src/main/resources/log4j2.xml");
        FileAppender appender = FileAppender.newBuilder().setConfiguration(config).setName(className.getSimpleName()).
                setLayout(PatternLayout.newBuilder().withPattern("%d{HH:mm:ss.SSS} %-5level %logger - %msg%n").build()).
                withFileName("target/log4j2/" + className.getSimpleName() + ".log").build();
        rootLoggerConfig.addAppender(appender, Level.INFO, null);
        appender.start();
        return logger;
    }
}
