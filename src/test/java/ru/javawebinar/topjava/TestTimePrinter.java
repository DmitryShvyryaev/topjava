package ru.javawebinar.topjava;

import ch.qos.logback.classic.Logger;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class TestTimePrinter implements TestRule {
    private static final Logger log = (Logger) LoggerFactory.getLogger(TestTimePrinter.class);
    private static final Map<String, Long> statistic = new HashMap<>();
    private static final String template = "%s = %dms";

    @Override
    public Statement apply(Statement statement, Description description) {
        long start = System.currentTimeMillis();
        try {
            statement.evaluate();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        long end = System.currentTimeMillis();
        long runTime = end - start;
        String desc = description.getClassName() + ": " + description.getMethodName();
        log.debug(String.format(template, description.getMethodName(), runTime));
        statistic.put(desc, runTime);
        return statement;
    }

    public static void printStatistic() {
        statistic.forEach((key, value) -> log.debug(String.format(template, key, value)));
    }
}
