package com.example.springretryoverview.listener;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.listener.MethodInvocationRetryListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class DefaultListenerSupport extends MethodInvocationRetryListenerSupport {

    @Override
    public <T, E extends Throwable> void close(RetryContext context,
                                               RetryCallback<T, E> callback, Throwable throwable) {
        System.out.println("onClose: completed configured number of retries. ");
        super.close(context, callback, throwable);
    }

    @Override
    public <T, E extends Throwable> void onError(RetryContext context,
                                                 RetryCallback<T, E> callback, Throwable throwable) {
        System.out.println("onError: Error occurred during retry "+context.getRetryCount());
        super.onError(context, callback, throwable);
    }

    @Override
    public <T, E extends Throwable> boolean open(RetryContext context,
                                                 RetryCallback<T, E> callback) {
        System.out.println("onOpen: staring retry now.");
        return super.open(context, callback);
    }
}