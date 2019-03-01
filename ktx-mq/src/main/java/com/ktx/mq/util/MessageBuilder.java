package com.ktx.mq.util;

import java.nio.charset.Charset;
import org.apache.rocketmq.common.message.Message;

public class MessageBuilder {
	
	/**
	 * 对象类型消息
	 */
	public static final String IS_OBJ_MSG = "true";
	
	private String topic;
	
	private String tags;
	
	private String keys;
	
	private Object body;
	
	
	public static MessageBuilder create() {
		return new MessageBuilder();
	}
	
	public static boolean isObjMsg(Message message) {
		if (!message.getProperties().isEmpty()) {
			return IS_OBJ_MSG.equals(message.getProperties().get("IS_OBJ_MSG"));
		}
		return false;
	}
	
	public MessageBuilder topic(String topic) {
		this.topic = topic;
		return this;
	}
	
	public MessageBuilder tags(String tags) {
		this.tags = tags;
		return this;
	}
	
	public MessageBuilder keys(String keys) {
		this.keys = keys;
		return this;
	}
	
	public MessageBuilder body(Object body) {
		this.body = body;
		return this;
	}
	
	public Message build() {
		Message message = new Message();
		byte[] bytes = null;
		if (body instanceof String) {
			bytes = ((String) body).getBytes(Charset.forName("UTF-8"));
		} else {
			bytes = ConvertUtil.objConvertBytes(body);
			message.putUserProperty("IS_OBJ_MSG", IS_OBJ_MSG);
		}
		message.setTopic(topic);
		message.setTags(tags);
		message.setKeys(keys);
		message.setBody(bytes);
		return message;
	}
	
	
}
