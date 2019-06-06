package com.shunya.reverse.service;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ReverseFunction implements Function<SQSEvent, String> {

    private final MathService mathService;

    public ReverseFunction(final MathService mathService) {
        this.mathService = mathService;
    }

    @Override
    public String apply(final SQSEvent sqsEvent) {


        for (SQSEvent.SQSMessage msg : sqsEvent.getRecords()) {
            System.out.println("process message body: " + msg.getBody());
        }


        return "";
    }
}
