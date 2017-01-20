package com.coder.dream.guice.persist.jpa;

import com.google.inject.matcher.AbstractMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 *
 * is synthetic and is being intercepted by [com.google.inject.persist.jpa.JpaLocalTxnInterceptor@6a3efb1d].
 * This could indicate a bug.  The method may be intercepted twice, or may not be intercepted at all.
 * Created by freeway on 16/1/6.
 */
public class UnitOfWorkMatcher extends AbstractMatcher<Method> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnitOfWorkMatcher.class);

    public static final UnitOfWorkMatcher INSTANCE = new UnitOfWorkMatcher();

    private UnitOfWorkMatcher() {
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
                (method.getName().startsWith("find")|method.getName().startsWith("count")) && (!method.isSynthetic());
    }
}