package com.ktx.mq.config;

public class ConsumerConfig extends MQConfig {
	private String topic;
	private String tags;
	
	private Integer retryTimes = 3;
	
	private boolean isOrderMsg = false;
	private Integer consumerThreadMin;
	private Integer consumerThreadMax;
	
	public static ConsumerConfig create(){
		return new ConsumerConfig();
	}
	
	public String getTopic() {
		return topic;
	}
	
	public ConsumerConfig setTopic(String topic) {
		this.topic = topic;
		return this;
	}
	
	public String getTags() {
		return tags;
	}
	
	public ConsumerConfig setTags(String tags) {
		this.tags = tags;
		return this;
	}
	
	public Integer getConsumerThreadMin() {
		return consumerThreadMin;
	}
	
	public ConsumerConfig setConsumerThreadMin(Integer consumerThreadMin) {
		this.consumerThreadMin = consumerThreadMin;
		return this;
	}
	
	public Integer getConsumerThreadMax() {
		return consumerThreadMax;
	}
	
	public ConsumerConfig setConsumerThreadMax(Integer consumerThreadMax) {
		this.consumerThreadMax = consumerThreadMax;
		return this;
	}
	
	public boolean isOrderMsg() {
		return isOrderMsg;
	}
	
	public ConsumerConfig setOrderMsg(boolean orderMsg) {
		isOrderMsg = orderMsg;
		return this;
	}
	
	
	public Integer getRetryTimes() {
		return retryTimes;
	}
	
	public ConsumerConfig setRetryTimes(Integer retryTimes) {
		this.retryTimes = retryTimes;
		return this;
	}
}
