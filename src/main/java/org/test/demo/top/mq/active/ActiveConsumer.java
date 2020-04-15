package org.test.demo.top.mq.active;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author gx
 * @create 2019-08-02 14:14
 */
public class ActiveConsumer {
    private static final String url = "tcp://118.24.2.124:61616";
    // private static final String queueName = "test_queue";
    private static final String topicName = "test_topic";

    public static void main(String[] args) {

        //new Thread(() -> consumeQueue()).start();
        //new Thread(() -> consumeQueue()).start();
        new Thread(() -> consumeTopic()).start();
        new Thread(() -> consumeTopic()).start();

    }

    // private static void consumeQueue() {
    //     try {
    //         ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(url);
    //         Connection connection = activeMQConnectionFactory.createConnection("admin", "admin");
    //         connection.start();
    //         Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    //         Destination destination = session.createQueue(queueName);
    //         MessageConsumer consumer = session.createConsumer(destination);
    //         consumer.setMessageListener(message -> {
    //             try {
    //                 System.out.println(Thread.currentThread().getName() + ((TextMessage) message).getText());
    //             } catch (JMSException e) {
    //                 e.printStackTrace();
    //             }
    //         });
    //     } catch (Exception e) {
    //         throw new RuntimeException(e.getMessage());
    //     }
    // }

    private static void consumeTopic() {
        try {
            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(url);
            Connection connection = activeMQConnectionFactory.createConnection("admin", "admin");
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic(topicName);
            MessageConsumer consumer = session.createConsumer(destination);
            consumer.setMessageListener(message -> {
                try {
                    System.out.println(Thread.currentThread().getName() + ((TextMessage) message).getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
