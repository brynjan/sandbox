package no.krakow.oracle.aq;

import oracle.AQ.AQAgent;
import oracle.AQ.AQQueue;
import oracle.AQ.AQSession;
import oracle.jms.AQjmsConnectionFactory;
import oracle.jms.AQjmsQueueConnectionFactory;
import oracle.jms.AQjmsTopicConnectionFactory;
import org.testng.annotations.Test;

import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDateTime;

/**
 */
public class AQTest {


    @Test
    public void testSend() throws Exception {

        Connection db_conn = DriverManager.getConnection("jdbc:oracle:thin:@local.db:1521:xe", "corporate", "corporate");
//        db_conn.setAutoCommit(false);
        QueueConnection queueConnection = AQjmsQueueConnectionFactory.createQueueConnection(db_conn);
        QueueSession queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = queueSession.createQueue("queue_quant");
        QueueSender queueSender = queueSession.createSender(queue);

//        TextMessage textMessage = queueSession.createTextMessage("Dette er en test melding");
        TextMessage textMessage = queueSession.createTextMessage();

        queueSender.send(null);
    }

    @Test
    public void testReceive() throws Exception {

        Connection db_conn = DriverManager.getConnection("jdbc:oracle:thin:@local.db:1521:xe", "corporate", "corporate");
//        db_conn.setAutoCommit(false);
        QueueConnection queueConnection = AQjmsQueueConnectionFactory.createQueueConnection(db_conn);
        QueueSession queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = queueSession.createQueue("queue_quant");
        QueueReceiver queueReceiver = queueSession.createReceiver(queue);

        queueConnection.start();
        TextMessage textMessage = (TextMessage) queueReceiver.receive();
        System.out.println(textMessage.getText());
    }


    @Test
    public void testSendTopic() throws Exception {

        Connection db_conn = DriverManager.getConnection("jdbc:oracle:thin:@local.db:1521:xe", "corporate", "corporate");
        TopicConnection topicConnection = AQjmsTopicConnectionFactory.createTopicConnection(db_conn);
        TopicSession topicSession = topicConnection.createTopicSession(true, Session.AUTO_ACKNOWLEDGE);
        Topic topic = topicSession.createTopic("qf_topic");

        TopicPublisher topicPublisher = topicSession.createPublisher(topic);

        TextMessage textMessage = topicSession.createTextMessage("Dette er en test melding " + LocalDateTime.now());

        topicPublisher.publish(textMessage);
        topicSession.commit();
    }


    @Test
    public void testReceiveTopic() throws Exception {

        Connection db_conn = DriverManager.getConnection("jdbc:oracle:thin:@local.db:1521:xe", "corporate", "corporate");
        TopicConnection topicConnection = AQjmsTopicConnectionFactory.createTopicConnection(db_conn);
        TopicSession topicSession = topicConnection.createTopicSession(true, Session.AUTO_ACKNOWLEDGE);
        Topic topic = topicSession.createTopic("qf_topic");

        TopicSubscriber topicSubscriber = topicSession.createDurableSubscriber(topic, "QF2");

        topicConnection.start();
        TextMessage textMessage = (TextMessage) topicSubscriber.receive(1000);
        topicSession.commit();
        System.out.println(textMessage.getText().toString());
    }
}
