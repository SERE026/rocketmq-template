package com.ktx.mq.producer.listener;

import com.ktx.mq.examples.TestUser;
import com.ktx.mq.util.ConvertUtil;
import com.ktx.mq.util.MessageBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 此监听需要注意的地方是， executeLocalTransaction 和 checkLocalTransaction 是两个不同的流程
 * checkLocalTransaction 流程不一定走，只有当excuteLocalTransaction超过阈值才会执行checkLocalTransaction流程
 * https://rocketmq.apache.org/rocketmq/the-design-of-transactional-message/
 */
public class TransactionListenerImpl implements TransactionListener {
	
	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	@Override
	public LocalTransactionState executeLocalTransaction(Message message, Object arg) {
		
		//TODO
		
		LOG.info("生产者：处理消息开始=====================================================");
		LOG.info("生产者：处理消息：{}", ToStringBuilder.reflectionToString(message));
		if (MessageBuilder.isObjMsg(message)) {
			TestUser user = (TestUser) ConvertUtil
				.bytesConvertObj(message.getBody(), TestUser.class);
			LOG.info("生产者：处理消息2：{}", ToStringBuilder.reflectionToString(user));
		} else {
			String body = new String(message.getBody());
			LOG.info("生产者：处理消息2：{}", body);
		}
		LOG.info("生产者：处理消息额外信息：{}", ToStringBuilder.reflectionToString(arg));
		LOG.info("生产者：处理消息技术=====================================================");
		
		return LocalTransactionState.COMMIT_MESSAGE;
	}
	
	@Override
	public LocalTransactionState checkLocalTransaction(MessageExt message) {
		//TODO
		
		LOG.info("生产者：检查消息开始=====================================================");
		LOG.info("生产者：检查消息：{}", ToStringBuilder.reflectionToString(message));
		if (MessageBuilder.isObjMsg(message)) {
			TestUser user = (TestUser) ConvertUtil
				.bytesConvertObj(message.getBody(), TestUser.class);
			LOG.info("生产者：检查消息Body：{}", ToStringBuilder.reflectionToString(user));
		} else {
			String body = new String(message.getBody());
			LOG.info("生产者：处理消息2：{}", body);
		}
		LOG.info("生产者：检查消息技术=====================================================");
		
		return LocalTransactionState.COMMIT_MESSAGE;
	}
}
