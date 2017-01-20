package com.coder.dream.guice.persist.jpa;

import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.regex.Pattern;

/**
 * 自定义的一些Matcher
 * Created by freeway on 16/1/6.
 */
public class NinjaMatchers {


    public static Matcher<Class> inSubpackageScan(final String regexPackageName) {
        return new InSubpackageScan(regexPackageName);
    }

    private static class InSubpackageScan extends AbstractMatcher<Class> implements Serializable {
        private static final Logger LOGGER = LoggerFactory.getLogger(InSubpackageScan.class);
        private final String regexPackageName;

        public InSubpackageScan(String regexPackageName) {
            this.regexPackageName = regexPackageName;
        }

        public boolean matches(Class c) {
            String classPackageName = c.getPackage().getName();
            Pattern pattern = Pattern.compile(regexPackageName);

            java.util.regex.Matcher matcher = pattern.matcher(classPackageName);
            boolean isMatched = matcher.matches();
            if (isMatched) {
                LOGGER.info(String.format("InSubpackageScan is matched, regex:(%s) class name:%s",
                        regexPackageName, c.getName()));
            }
            return isMatched;
        }

        @Override public boolean equals(Object other) {
            return other instanceof InSubpackageScan
                    && ((InSubpackageScan) other).regexPackageName.equals(regexPackageName);
        }

        @Override public int hashCode() {
            return 37 * regexPackageName.hashCode();
        }

        @Override public String toString() {
            return "inSubpackage(" + regexPackageName + ")";
        }

        private static final long serialVersionUID = 0;
    }

    public static Matcher<Method> jpaTxnMatcher() {
        return new JpaTxnMatcher();
    }

    private static class JpaTxnMatcher extends AbstractMatcher<Method> {

        private static final Logger LOGGER = LoggerFactory.getLogger(JpaTxnMatcher.class);

        private static final Pattern PATTERN = Pattern.compile("^(update|delete|save|find|count)");

        public JpaTxnMatcher() {
        }

        @Override
        public boolean matches(Method method) {

            if (Modifier.isPublic(method.getModifiers()) && !method.isSynthetic() &&
                    (!(method.getName().startsWith("find") | method.getName().startsWith("count")
                            | method.getName().startsWith("save")| method.getName().startsWith("delete")
                            | method.getName().startsWith("update") | method.getName().startsWith("toString")
                            | method.getName().startsWith("equals") | method.getName().startsWith("hashCode")
                    ))) {
                LOGGER.error("invalid dao method:" + method.getDeclaringClass().getName() + "." + method.getName());
            }
            return Modifier.isPublic(method.getModifiers()) &&
                    (PATTERN.matcher(method.getName()).find());
        }
    }
}
