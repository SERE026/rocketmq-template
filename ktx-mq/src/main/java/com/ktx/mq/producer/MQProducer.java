package com.ktx.mq.producer;

import com.ktx.mq.exception.MQException;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

public interface MQProducer {
	void start();
	
	/**
	 * 同步发送消息
	 * @param message
	 * @return
	 */
	SendResult syncSend(Message message) throws MQException;
	
	/**
	 * 同步顺序发送消息
	 * @param message
	 * @param orderKey 选择队列使用的关键值 有序只有在同一个队列才能保证
	 * @return
	 */
	SendResult syncSendOrderly(Message message, Object orderKey) throws MQException;
	
	
	
	/**
	 * 异步发送消息
	 * @param message
	 * @param sendCallback
	 */
	void asyncSend(Message message, SendCallback sendCallback) throws MQException;
	
	/**
	 * 异步顺序发送消息
	 * @param message
	 * @param orderKey 选择队列使用的关键值 有序只有在同一个队列才能保证
	 * @param sendCallback
	 */
	void ayncSendOrderly(Message message, Object orderKey,SendCallback sendCallback )throws MQException;
	/**
	 * OneWay 模式发送消息 ，如短信、邮件等无需返回的消息
	 * @param message
	 */
	void sendOneWay(Message message) throws MQException;
	void destory();
}
