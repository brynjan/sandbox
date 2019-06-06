package com.shunya.reverse.handler.aws;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import org.reactivestreams.Publisher;
import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;

import com.shunya.reverse.domain.NumberInput;
import com.shunya.reverse.domain.NumberOutput;
import reactor.core.publisher.Flux;

import java.util.Collection;

public class ReverseFunctionHandler extends SpringBootRequestHandler<SQSEvent, String> {

    public ReverseFunctionHandler() {
        System.out.println("constructor 0");
        System.out.println("før initialize 0");
        initialize(new AwsContext());
        System.out.println("etter initialize 0");

    }

    public ReverseFunctionHandler(final Class<?> configurationClass) {
        super(configurationClass);
        System.out.println("constructor 1");
    }

    @Override
    public Object handleRequest(SQSEvent event, Context context) {

        System.out.println("context: " + context.getClass().getName());
        System.out.println("functionName: " + context.getFunctionName());
        System.out.println("før initialize");
        initialize(context);
        System.out.println("etter initialize");
        Object input = acceptsInput() ? convertEvent(event) : "";
        Publisher<?> output = apply(extract1(input));
        return result(input, output);

    }

    private Flux<?> extract1(Object input) {
        if (input instanceof Collection) {
            return Flux.fromIterable((Iterable<?>) input);
        }
        return Flux.just(input);
    }

    static class AwsContext implements Context{
        @Override
        public String getAwsRequestId() {
            return null;
        }

        @Override
        public String getLogGroupName() {
            return null;
        }

        @Override
        public String getLogStreamName() {
            return null;
        }

        @Override
        public String getFunctionName() {
            return null;
        }

        @Override
        public String getFunctionVersion() {
            return null;
        }

        @Override
        public String getInvokedFunctionArn() {
            return null;
        }

        @Override
        public CognitoIdentity getIdentity() {
            return null;
        }

        @Override
        public ClientContext getClientContext() {
            return null;
        }

        @Override
        public int getRemainingTimeInMillis() {
            return 0;
        }

        @Override
        public int getMemoryLimitInMB() {
            return 0;
        }

        @Override
        public LambdaLogger getLogger() {
            return null;
        }
    }

}
