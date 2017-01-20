package com.coder.dream.guice.persist.jpa;

import com.google.inject.matcher.AbstractMatcher;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by freeway on 16/1/6.
 */
public class TransactionalMatcher extends AbstractMatcher<Method> {

    public static final TransactionalMatcher INSTANCE = new TransactionalMatcher();

    private TransactionalMatcher() {
    }

    @Override
    public boolean matches(Method method) {

        return Modifier.isPublic(method.getModifiers()) &&
                (method.getName().startsWith("update") ||
                        method.getName().startsWith("save") ||
                        method.getName().startsWith("delete")) &&
                !method.isSynthetic();
    }
}