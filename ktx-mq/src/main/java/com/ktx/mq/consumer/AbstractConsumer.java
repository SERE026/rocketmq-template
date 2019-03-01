package com.ktx.mq.consumer;

import com.ktx.mq.config.ConsumerConfig;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractConsumer implements MQConsumer {
	
	public final Logger LOG = LoggerFactory.getLogger(getClass());
	
	private DefaultMQPushConsumer consumer;
	
	@Override
	public void start() {
		try {
			ConsumerConfig cfg = (ConsumerConfig) config();
			consumer = new DefaultMQPushConsumer(cfg.getGroup());
			consumer.setConsumerGroup(cfg.getGroup());
			consumer.setNamesrvAddr(cfg.getNameServerAddr());
			consumer.setMessageModel(MessageModel.CLUSTERING);
			consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
			
			consumer.subscribe(cfg.getTopic(), cfg.getTags());
			
			if (StringUtils.isNotBlank(cfg.getInstanceName())) {
				consumer.setInstanceName(cfg.getInstanceName());
			}
			
			if (cfg.isOrderMsg()) {
				consumer.registerMessageListener(
					(List<MessageExt> list, ConsumeOrderlyContext context) -> {
						context.setAutoCommit(true);
						return onMessage(list, context);
					}
				);
			} else {
				consumer.registerMessageListener(
					(List<MessageExt> list, ConsumeConcurrentlyContext context) ->
						onMessage(list, context));
			}
			consumer.start();
		} catch (MQClientException e) {
			e.printStackTrace();
			LOG.error("消费端启动失败：{} ---->异常：{}", e.getErrorMessage());
		}
	}
	
	
	/**
	 * 可以重写此方法自定义序列化和返回消费成功的相关逻辑
	 *
	 * @param list 消息列表
	 * @param consumeConcurrentlyContext 上下文
	 * @return 消费状态
	 */
	public ConsumeConcurrentlyStatus onMessage(List<MessageExt> list,
		ConsumeConcurrentlyContext consumeConcurrentlyContext) {
		for (MessageExt messageExt : list) {
			LOG.debug("receive msgId: {}, tags : {}", messageExt.getMsgId(), messageExt.getTags());
			boolean check = before(messageExt);
			if (!check) {
				return ConsumeConcurrentlyStatus.RECONSUME_LATER;
			}
			
			if (!process(messageExt, messageExt.getProperties())) {
				LOG.warn("consume fail , ask for re-consume , msgId: {}", messageExt.getMsgId());
				return ConsumeConcurrentlyStatus.RECONSUME_LATER;
			}
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
	
	
	/**
	 * 原生dealMessage方法，可以重写此方法自定义序列化和返回消费成功的相关逻辑
	 *
	 * @param list 消息列表
	 * @param consumeOrderlyContext 上下文
	 * @return 处理结果
	 */
	public ConsumeOrderlyStatus onMessage(List<MessageExt> list,
		ConsumeOrderlyContext consumeOrderlyContext) {
		
		for (MessageExt messageExt : list) {
			LOG.info("receive msgId: {}, tags : {}", messageExt.getMsgId(), messageExt.getTags());
			
			boolean check = before(messageExt);
			if (!check) {
				return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
			}
			
			if (!process(messageExt, messageExt.getProperties())) {
				LOG.warn("consume fail , ask for re-consume , msgId: {}", messageExt.getMsgId());
				return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
			}
		}
		return ConsumeOrderlyStatus.SUCCESS;
	}
	
	@Override
	public void destory(){
		this.consumer.shutdown();
	}
	
}
