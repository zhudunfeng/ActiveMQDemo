package com.atguigu.consumer;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class ActiveMQConsumer {
    public static final String MQ_URL = "tcp://192.168.136.129:61616";
    public static final String QUEUE_NAME="queue0805";
    public static void main(String[] args) throws JMSException {
        //1 获得ActiveMQConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(MQ_URL);
        //2 由ActiveMQConnectionFactory获得Connection
        Connection connection = connectionFactory.createConnection();
        //3 启动连接准备建立会话
        connection.start();
        //4 获得session，两个参数先用默认
        //4.1 是否开启事务，如果消费者的事务不进行提交，会出现重复消费的情况，使用默认false就好
        //事务的等级大于签收
        //4.2 签收模式
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5 获得目的地，此例为队列
        Queue queue = session.createQueue(QUEUE_NAME);
        //6 获得消息消费者,消费什么内容？从哪里消费？
        MessageConsumer messageConsumer = session.createConsumer(queue);

        /*
        异步非阻塞方式(监听器onMessage())
        订阅者或接收者通过MessageConsumer的setMessageListener(MessageListener listener)注册一个消息监听器，
        当消息到达之后，系统自动调用监听器MessageListener的onMessage(Message message)方法。*/
        messageConsumer.setMessageListener(message-> {
            if(message!=null&&message instanceof  TextMessage){
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println("*****messageConsumer:"+textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });





        /*同步阻塞方式receive() ，订阅者或接收者调用MessageConsumer的receive()方法来接收消息，
        receive()将一直阻塞
        receive(long timeout) 按照给定的时间阻塞，到时间自动退出*/
       /* while (true){
            // 这种是一直处于接收，不会退出，不用释放资源
            TextMessage textMessage = (TextMessage) messageConsumer.receive();
             TextMessage textMessage = (TextMessage) messageConsumer.receive(4000);
            if(null!=textMessage){
                System.out.println("*****messageConsumer: "+textMessage.getText());

            }else{
                break;
            }
        }
       //8 释放各种资源
        messageConsumer.close();
        session.close();
        connection.close();
        System.out.println("******msg Consumer ok,O(∩_∩)O");*/
    }
}
