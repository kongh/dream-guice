package com.coder.dream.guice.persist.jpa;

import com.google.inject.Inject;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * See @UnitOfWork.
 *
 * This interceptor tracks and opens and closes your database connections.
 *
 * @author Raphael A. Bauer
 */
public class UnitOfWorkInterceptor implements MethodInterceptor {

    @Inject
    com.google.inject.persist.UnitOfWork unitOfWork;

    // ThreadLocal<Boolean> tracks if the unit of work was begun
    // implicitly by this thread.
    // According to the docs we can start and end the UnitOfWork as often
    // as we want. But this has to be balanced in some way. Otherwise we get:
    //
    // java.lang.IllegalStateException: Work already begun on this thread. Looks like you have called UnitOfWork.begin() twice without a balancing call to end() in between.
    // at com.google.inject.internal.util.$Preconditions.checkState(Preconditions.java:142) ~[guice-3.0.jar:na]
    // at com.google.inject.persist.jpa.JpaPersistService.begin(JpaPersistService.java:66) ~[guice-persist-3.0.jar:na]
    //
    // That way all begin() and end() calls are balanced
    // because we only have one unit for this thread.
    final ThreadLocal<Boolean> didWeStartWork = new ThreadLocal<>();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        if (null == didWeStartWork.get()) {
            try {
                unitOfWork.begin();
            } catch (IllegalStateException e) {
                ;
            }
            didWeStartWork.set(Boolean.TRUE);

        } else {
            // If unit of work already started we don't do anything here...
            // another UnitOfWorkInterceptor point point will take care...
            // This happens if you are nesting your calls.
            return invocation.proceed();
        }

        try {

            return invocation.proceed();

        } finally {

            if (null != didWeStartWork.get()) {

                didWeStartWork.remove();
                unitOfWork.end();

            }

        }

    }

}