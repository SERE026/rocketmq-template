package com.ktx;

import com.ktx.mq.config.ProducerConfig;
import com.ktx.mq.examples.DefaultConsumer;
import com.ktx.mq.examples.TestUser;
import com.ktx.mq.exception.MQException;
import com.ktx.mq.producer.RocketMQProducer;
import com.ktx.mq.producer.listener.TransactionListenerImpl;
import com.ktx.mq.util.MessageBuilder;
import java.io.File;
import java.io.IOException;
import javax.annotation.Resource;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.junit.Before;
import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class KtxMqApplicationTests {

	
//	@Resource
	private RocketMQProducer producer;
	
	private DefaultConsumer consumer;
	
	@Before
	public void init() throws MQException {
//		producer = new DefaultProducer();
		producer = new RocketMQProducer();
		ProducerConfig config = new ProducerConfig();
		config.setGroup("groupB");
		config.setNameServerAddr("localhost:9876");
		config.setInstanceName("instance-nameA");
		producer.setConfig(config);
		
		//启动事务消息
		producer.createAndStartTransactionMQProducer("groupA",new TransactionListenerImpl(),null);
		
		consumer = new DefaultConsumer();
//		producer.start();
		producer.start();
		consumer.start();
	}
	
	@Test
	public void contextLoads() throws MQException, InterruptedException {
		
		
		send();
		//consumer.LOG.info("consumer 参数配置：{}", ToStringBuilder.reflectionToString(consumer.getMQConfig()));
		
		while(true){
			Thread.sleep(30*1000);
			send();
		}
	}

	void send() throws MQException {
		for(int i=0; i< 1; i++) {
			Message message = new Message("test-topic", "test-tag-1", ("hello-word"+i).getBytes());
			producer.syncSend(message);
		}
		
		
		TestUser testUser = new TestUser();
		testUser.setAge(10);
		testUser.setId(1);
		testUser.setMobile("13520398355");
		testUser.setName("test-wjw");
		Message message = MessageBuilder.create()
			.topic("test-topic")
			.tags("test-tag-tx")
			.keys("1")
			.body(testUser)
			.build();
		
		
		TransactionSendResult result = producer.sendMessageInTransaction("groupA",message,"transaction-arg");
		System.out.println(ToStringBuilder.reflectionToString(result));
	}
	
	@Test
	public void test() throws MQException {
		send();
		while(true){}
	}
	
	
	
}

