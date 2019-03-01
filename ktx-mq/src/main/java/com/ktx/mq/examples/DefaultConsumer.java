package com.ktx.mq.examples;

import com.ktx.mq.config.ConsumerConfig;
import com.ktx.mq.config.MQConfig;
import com.ktx.mq.consumer.AbstractConsumer;
import com.ktx.mq.util.ConvertUtil;
import com.ktx.mq.util.MessageBuilder;
import java.util.Map;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.rocketmq.common.message.Message;
//import org.springframework.beans.factory.InitializingBean;

//@Component
public class DefaultConsumer extends AbstractConsumer  {
	
	
	@Override
	public boolean before(Message message) {
		LOG.info("消费检查消息开始=====================================================");
		
		LOG.info("消费检查消息Message：{}", ToStringBuilder.reflectionToString(message));
		if(MessageBuilder.isObjMsg(message)) {
			TestUser user = (TestUser) ConvertUtil
				.bytesConvertObj(message.getBody(), TestUser.class);
			LOG.info("消费检查消息Body：{}", ToStringBuilder.reflectionToString(user));
		}else{
			String msg = new String(message.getBody());
			LOG.info("消费检查消息Body：{}", msg);
		}
		LOG.info("消费检查消息结束=====================================================");
		return true;
	}
	
	@Override
	public boolean process(Message message, Object extInfo) {
		LOG.info("消费处理消息开始=====================================================");
		
		
		LOG.info("消费处理消息：{}", ToStringBuilder.reflectionToString(message));
		
		if(MessageBuilder.isObjMsg(message)) {
			TestUser user = (TestUser) ConvertUtil.bytesConvertObj(message.getBody(),TestUser.class);
			LOG.info("消费处理消息2：{}", ToStringBuilder.reflectionToString(user));
		}else{
			String msg = new String(message.getBody());
			LOG.info("消费处理消息2：{}", msg);
		}
		
		Map map = (Map) extInfo;
		String str = ConvertUtil.objConvertStr(map);
		LOG.info("消费处理消息额外信息：{}",str);
		LOG.info("消费处理消息结束=====================================================");
		return true;
	}
	
	@Override
	public MQConfig config() {
		ConsumerConfig config = ConsumerConfig.create();
		config.setGroup("test-group")
			.setInstanceName("test-instanceName")
			.setNameServerAddr("localhost:9876");
		 	config.setOrderMsg(false)
			.setConsumerThreadMax(10)
			.setConsumerThreadMin(1)
			.setRetryTimes(3)
			.setTopic("test-topic")
			.setTags("test-tag || TagA || test-tag-1 || test-tag-tx")
			;
		 	return config;
	}
	
}
