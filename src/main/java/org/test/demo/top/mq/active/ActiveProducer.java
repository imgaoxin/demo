package org.test.demo.top.mq.active;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author gx
 * @create 2019-08-02 14:14
 */
public class ActiveProducer{
    private static final String url = "tcp://118.24.2.124:61616";
    // private static final String queueName = "test_queue";
    private static final String topicName = "test_topic";

    public static void main(String[] args) {

        //new Thread(() -> produceQueue()).start();
        new Thread(() -> produceTopic()).start();

    }

    // private static void produceQueue(){
    //     try {
    //         ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(url);
    //         Connection connection = activeMQConnectionFactory.createConnection("admin", "admin");
    //         connection.start();
    //         Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    //         Destination destination = session.createQueue(queueName);
    //         MessageProducer producer = session.createProducer(destination);
    //         for (int i = 0;i<100;i++) {
    //             Message message = session.createTextMessage("produce" + i);
    //             System.out.println(Thread.currentThread().getName()+ message.toString());
    //             producer.send(message);
    //         }
    //         session.close();
    //         connection.close();

    //     } catch (Exception e){
    //         throw new RuntimeException(e.getMessage());
    //     }
    // }

    private static void produceTopic(){
        try {
            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(url);
            Connection connection = activeMQConnectionFactory.createConnection("admin", "admin");
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic(topicName);
            MessageProducer producer = session.createProducer(destination);
            for (int i = 0;i<100;i++) {
                Message message = session.createTextMessage("produce" + i);
                System.out.println(Thread.currentThread().getName() + message.toString());
                producer.send(message);
            }
            session.close();
            connection.close();

        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
