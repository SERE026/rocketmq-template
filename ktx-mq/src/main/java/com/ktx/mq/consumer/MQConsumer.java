package com.ktx.mq.consumer;

import com.ktx.mq.config.MQConfig;
import com.ktx.mq.exception.MQException;
import org.apache.rocketmq.common.message.Message;

public interface MQConsumer {
	
	void start();
	/**
	 * 1. 消费消息前检查 根据该检查是否重新消费， true 表示消费成功、不重新消费，false 稍后再消费
	 *
	 * @param message 消息
	 * @return 默认返回true
	 */
	boolean before(Message message);
	
	/**
	 * 消费成功处理
	 */
	boolean process(Message message, Object extInfo);
	
	
	void destory();
	/**
	 * 获取当前的配置
	 */
	MQConfig config();
	/**
	 * 获取 泛型 T 的T.class
	 * @return
	 */
//	default Class<T> getEntityClass(){
//		Class < T >  entityClass  =  (Class < T > ) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[ 0 ];
//		return entityClass;
//	}
}
