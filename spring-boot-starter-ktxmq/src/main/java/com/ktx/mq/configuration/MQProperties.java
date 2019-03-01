package com.ktx.mq.configuration;

import com.ktx.mq.config.ConsumerConfig;
import com.ktx.mq.config.ProducerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ktx.rocketmq")
public class MQProperties {
	
	private Boolean enabled = false;
	
	private ProducerProperties producer;
	
	private ConsumerProperties consumer;
	
	
	public ProducerProperties getProducer() {
		return producer;
	}
	
	public Boolean getEnabled() {
		return enabled;
	}
	
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	public void setProducer(ProducerProperties producer) {
		this.producer = producer;
	}
	
	public ConsumerProperties getConsumer() {
		return consumer;
	}
	
	public void setConsumer(ConsumerProperties consumer) {
		this.consumer = consumer;
	}
	
	
	
	public static class ProducerProperties extends ProducerConfig{
	}
	
	public static class ConsumerProperties extends ConsumerConfig{
	}
}
