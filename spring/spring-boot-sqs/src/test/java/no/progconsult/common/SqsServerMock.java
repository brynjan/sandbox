package no.progconsult.common;

import org.elasticmq.rest.sqs.SQSRestServer;
import org.elasticmq.rest.sqs.SQSRestServerBuilder;

public class SqsServerMock {
    private static final int PORT = 55667;
    public static final String BASE_URL = "http://localhost:";
    public static final String ELASTICMQ_URL = "http://localhost:55667";
    private final int port;
    private final SQSRestServer sqsRestServer;

    public SqsServerMock() {
        this(55667);
    }

    public SqsServerMock(int port) {
        this.port = port;
        this.sqsRestServer = SQSRestServerBuilder.withPort(port).withInterface("localhost").start();
        this.sqsRestServer.waitUntilStarted();
    }

    public void stopSqsServer() {
        this.sqsRestServer.stopAndWait();
    }

    public int getPort() {
        return this.port;
    }
}