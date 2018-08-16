
package no.progconsult.common;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;
import com.amazonaws.services.sqs.model.ChangeMessageVisibilityRequest;
import com.amazonaws.services.sqs.model.ChangeMessageVisibilityResult;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteMessageResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.PurgeQueueRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import org.awaitility.Awaitility;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class SqsAsyncClientMock extends AmazonSQSAsyncClient {
    @SuppressWarnings("unchecked")
    private final List<SendMessageRequest> sendMessageRequests = new ArrayList();
    @SuppressWarnings("unchecked")
    private final List<DeleteMessageRequest> deleteMessageRequests = new ArrayList();
    @SuppressWarnings("unchecked")
    private final List<ChangeMessageVisibilityRequest> changeMessageVisibilityRequests = new ArrayList();

    public SqsAsyncClientMock() {
        super(new BasicAWSCredentials("x", "x"));
        this.setEndpoint("http://localhost:55667");
    }

    public SqsAsyncClientMock(final int port) {
        super(new BasicAWSCredentials("x", "x"));
        this.setEndpoint("http://localhost:" + port);
    }

    public Future<SendMessageResult> sendMessageAsync(final SendMessageRequest sendMessageRequest) {
        final Future<SendMessageResult> sendMessageResultFuture = super.sendMessageAsync(sendMessageRequest);
        this.sendMessageRequests.add(sendMessageRequest);
        return sendMessageResultFuture;
    }

    public Future<DeleteMessageResult> deleteMessageAsync(final DeleteMessageRequest deleteMessageRequest) {
        final Future<DeleteMessageResult> deleteMessageResultFuture = super.deleteMessageAsync(deleteMessageRequest);
        this.deleteMessageRequests.add(deleteMessageRequest);
        return deleteMessageResultFuture;
    }

    public Future<ChangeMessageVisibilityResult> changeMessageVisibilityAsync(final ChangeMessageVisibilityRequest changeMessageVisibilityRequest) {
        final Future<ChangeMessageVisibilityResult> changeMessageVisibilityResultFuture = super.changeMessageVisibilityAsync(changeMessageVisibilityRequest);
        this.changeMessageVisibilityRequests.add(changeMessageVisibilityRequest);
        return changeMessageVisibilityResultFuture;
    }

    public List<DeleteMessageRequest> getDeleteMessageRequests() {
        return Collections.unmodifiableList(this.deleteMessageRequests);
    }

    public List<ChangeMessageVisibilityRequest> getChangeMessageVisibilityRequests() {
        return Collections.unmodifiableList(this.changeMessageVisibilityRequests);
    }

    public List<SendMessageRequest> getSendMessageRequests() {
        return Collections.unmodifiableList(this.sendMessageRequests);
    }

    public List<SendMessageRequest> getSendMessageRequestsForQueueUrl(final String queueUrl) {
        return Collections.unmodifiableList((List) this.sendMessageRequests.stream().filter((request) -> request.getQueueUrl().equals(queueUrl)).collect(Collectors.toList()));
    }

    public void clearState() {
        this.sendMessageRequests.clear();
        this.deleteMessageRequests.clear();
        this.changeMessageVisibilityRequests.clear();
        this.listQueues().getQueueUrls().forEach((queueUrl) -> this.purgeQueue(new PurgeQueueRequest(queueUrl)));
    }

    public void sendMessage(final String queueUrl, final String body, final String messageAttributeKey, final String messageAttributeValue) {
        final MessageAttributeValue mav = (new MessageAttributeValue()).withDataType("String").withStringValue(messageAttributeValue);
        this.sendMessage((new SendMessageRequest(queueUrl, body)).addMessageAttributesEntry(messageAttributeKey, mav));
    }

    public Message receiveSingleMessage(final String queueUrl) {
        return (Message) this.receiveMessages(queueUrl, 1).get(0);
    }

    public List<Message> receiveMessages(final String queueUrl, final int numberOfExpectedMessages) {
        final List<Message> messages = new ArrayList();
        Awaitility.await().until(() -> {
            messages.addAll(this.receiveMessage(
                    (new ReceiveMessageRequest(queueUrl)).withMaxNumberOfMessages(10).withWaitTimeSeconds(10).withMessageAttributeNames(new String[]{"All"})
                                                         .withAttributeNames(new String[]{"All"})).getMessages());
            return messages;
        }, Matchers.hasSize(numberOfExpectedMessages));
        return messages;
    }

    public void waitUntilMessageDeleted(final String queueUrl) {
        this.waitUntilMessageDeleted(queueUrl, 1);
    }

    public void waitUntilMessageDeleted(final String queueUrl, final int count) {
        final Matcher<Collection<?>> collectionMatcher = Matchers.hasSize(Matchers.greaterThanOrEqualTo(count));
        final Callable<Collection<?>> listCallable = () -> (List) this.deleteMessageRequests.stream().filter((req) -> req.getQueueUrl().equals(queueUrl)).collect(Collectors.toList());

        Awaitility.await().until(listCallable, collectionMatcher);
    }

    public void waitUntilMessageRetried(final String queueUrl, final int count) {
        final Callable<Collection<?>> listCallable = () -> (List) this.changeMessageVisibilityRequests.stream().filter((req) -> req.getQueueUrl().equals(queueUrl)).collect(Collectors.toList());
        final Matcher<Collection<?>> collectionMatcher = Matchers.hasSize(Matchers.greaterThanOrEqualTo(count));

        Awaitility.await().until(listCallable, collectionMatcher);
    }

    public String createQueueIfNotExists(final String queueName) {
        return this.createQueue(queueName).getQueueUrl();
    }

    public String queueUrl(final String queueName) {
        return this.getQueueUrl(queueName).getQueueUrl();
    }

    public String getMessageAttribute(final String key, final Message message) {
        return message.getMessageAttributes().get(key).getStringValue();
    }
}
