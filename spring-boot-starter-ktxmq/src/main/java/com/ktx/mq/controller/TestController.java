package com.ktx.mq.controller;

import com.ktx.mq.examples.TestUser;
import com.ktx.mq.exception.MQException;
import com.ktx.mq.producer.RocketMQProducer;
import com.ktx.mq.util.MessageBuilder;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	
	@Autowired
	private RocketMQProducer producer;
	
	@GetMapping("/test")
	public ResponseEntity test(){
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
		try {
			producer.sendMessageInTransaction("testTransactionListener",message,"test-transaction-arg");
		} catch (MQException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok("sucess");
	}
}
