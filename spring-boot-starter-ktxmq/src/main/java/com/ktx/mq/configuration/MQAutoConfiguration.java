package com.ktx.mq.configuration;

import com.ktx.mq.producer.RocketMQProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnExpression("${ktx.rocketmq.enabled:true}")
@EnableConfigurationProperties({MQProperties.class})
public class MQAutoConfiguration {
	
	public MQAutoConfiguration(){}
	
	@Bean
	RocketMQProducer producer(MQProperties properties){
		RocketMQProducer producer = new RocketMQProducer();
//		producer.setConfig(producerConfig());
		producer.setConfig(properties.getProducer());
		return producer;
	}
}
