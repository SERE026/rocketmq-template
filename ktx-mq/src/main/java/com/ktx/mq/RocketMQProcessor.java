//package com.ktx.mq;
//
//import com.ktx.mq.annotation.MQTransactionLinsenter;
//import com.ktx.mq.consumer.MQConsumer;
//import com.ktx.mq.exception.MQException;
//import com.ktx.mq.producer.RocketMQProducer;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Map.Entry;
//import org.apache.rocketmq.client.producer.TransactionListener;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.BeansException;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.stereotype.Component;
//
//@Component
//public class RocketMQProcessor implements ApplicationContextAware {
//	private static final Logger LOG = LoggerFactory.getLogger(RocketMQProcessor.class);
//
//	@Override
//	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//		RocketMQProducer rocketMQProducer = applicationContext.getBean(RocketMQProducer.class);
//		rocketMQProducer.start();  //启动默认的生产端
//		LOG.info("----------rocketmq默认生产端启动------------");
//		//实例化所有事务消息发送者
//		Map listenerMap = applicationContext.getBeansWithAnnotation(MQTransactionLinsenter.class);
//		Iterator<String> listener = listenerMap.keySet().iterator();
//		while(listener.hasNext()) {
//			String key = listener.next();
//			TransactionListener instance = (TransactionListener) listenerMap.get(key);
//			String txGroup = instance.getClass().getAnnotation(MQTransactionLinsenter.class).mqGroup();
//			try {
//				rocketMQProducer.createAndStartTransactionMQProducer(txGroup,
//					instance,null);
//			} catch (MQException e) {
//				e.printStackTrace();
//			}
//		}
//		LOG.info("----------rocketmq事务生产端启动------------");
//		//TODO 启动消费端
//		Map consumerMap = applicationContext.getBeansOfType(MQConsumer.class);
//		Iterator<Entry<String, MQConsumer>> entries = consumerMap.entrySet()
//			.iterator();
//		while(entries.hasNext()){
//			Entry<String, MQConsumer> entry = entries.next();
//			entry.getValue().start();
//		}
//		LOG.info("----------rocketmq消费端启动------------");
//	}
//
//}