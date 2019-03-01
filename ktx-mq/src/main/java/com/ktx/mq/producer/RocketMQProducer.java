package com.ktx.mq.producer;

import com.ktx.mq.config.ProducerConfig;
import com.ktx.mq.exception.MQException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RocketMQProducer implements MQProducer {
	
	private static final Logger LOG = LoggerFactory.getLogger(RocketMQProducer.class);
	
	// 有序消息使用
	private static MessageQueueSelector messageQueueSelector = new SelectMessageQueueByHash();
	
	private ProducerConfig config;
	
	private final Map<String, DefaultMQProducer> cache = new ConcurrentHashMap();
	private final List<String> producerKeys = new ArrayList<>();
	private volatile int indexProducer = 0;
	
	public RocketMQProducer() {
	}
	
	private TransactionMQProducer stageMQProducer(String name)
		throws MQException {
		TransactionMQProducer cachedProducer = (TransactionMQProducer)this.cache.get(name);
		if (cachedProducer == null) {
			throw new MQException(String.format("Can not found MQProducer '%s' in cache! please define @MQTransactionListener class or invoke createOrGetStartedTransactionMQProducer() to create it firstly", name));
		} else {
			return cachedProducer;
		}
	}
	
	private DefaultMQProducer stageMQProducer() throws MQException {
		if(this.producerKeys.size() == 1){
			return this.cache.get(producerKeys.get(indexProducer));
		}
		DefaultMQProducer cachedProducer = null;
		for(;indexProducer < this.producerKeys.size(); indexProducer ++){
			
			cachedProducer = this.cache.get(producerKeys.get(indexProducer));
			//恢复原位置
			if(indexProducer >= this.cache.size()){
				indexProducer = 0;
			}
			break;
		}
		if (cachedProducer == null) {
			throw new MQException("Can not found MQProducer in cache! ");
		} else {
			return cachedProducer;
		}
	}
	
	@Override
	public void start() {
		//默认启动一个ProducerConfig 的生产端
		try {
			this.createAndStartMQProducer(this.config.getGroup());
		} catch (MQException e) {
			e.printStackTrace();
		}
	}
	
	public ProducerConfig getConfig() {
		return config;
	}
	
	public void setConfig(ProducerConfig config) {
		this.config = config;
	}
	
	/**
	 * 同步发送消息
	 *
	 * @param message 消息体
	 * @throws MQException 消息异常
	 */
	@Override
	public SendResult syncSend(Message message) throws MQException {
		try {
			SendResult sendResult = stageMQProducer().send(message);
			LOG.debug("send rocketmq message ,messageId : {}", sendResult.getMsgId());
			return sendResult;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("消息发送失败，topic : {}, msgObj {}", message.getTopic(), message);
			throw new MQException("消息发送失败，topic :" + message.getTopic() + ",e:" + e.getMessage());
		}
	}
	
	/**
	 * 同步发送消息
	 *
	 * @param message 消息体
	 * @param orderKey 用于hash后选择queue的key ，无需生产者二手动hash，交由Selector 处理 如同一个用户可以使用用户ID
	 * @throws MQException 消息异常
	 */
	@Override
	public SendResult syncSendOrderly(Message message, Object orderKey) throws MQException {
		try {
			SendResult sendResult = stageMQProducer().send(message, messageQueueSelector, orderKey);
			LOG.debug("send rocketmq message orderly ,messageId : {}", sendResult.getMsgId());
			return sendResult;
		} catch (Exception e) {
			LOG.error("顺序消息发送失败，topic : {}, msgObj {}", message.getTopic(), message);
			throw new MQException("顺序消息发送失败，topic :" + message.getTopic() + ",e:" + e.getMessage());
		}
	}
	
	/**
	 * 异步发送消息
	 *
	 * @param message msgObj
	 * @throws MQException 消息异常
	 */
	@Override
	public void asyncSend(Message message, SendCallback sendCallback) throws MQException {
		try {
			stageMQProducer().send(message, sendCallback);
			LOG.debug("send rocketmq message async");
		} catch (Exception e) {
			LOG.error("消息发送失败，topic : {}, msgObj {}", message.getTopic(), message);
			throw new MQException("消息发送失败，topic :" + message.getTopic() + ",e:" + e.getMessage());
		}
	}
	@Override
	public void ayncSendOrderly(Message message, Object orderKey, SendCallback sendCallback)throws MQException{
		try {
			stageMQProducer().send(message, messageQueueSelector, orderKey,sendCallback);
			LOG.debug("send rocketmq message async");
		} catch (Exception e) {
			LOG.error("消息发送失败，topic : {}, msgObj {}", message.getTopic(), message);
			throw new MQException("消息发送失败，topic :" + message.getTopic() + ",e:" + e.getMessage());
		}
	}
	
	
	/**
	 * OneWay 模式发送消息 ，如短信、邮件等无需返回的消息
	 *
	 * @param message message
	 * @throws MQException 消息异常
	 */
	@Override
	public void sendOneWay(Message message) throws MQException {
		try {
			stageMQProducer().sendOneway(message);
			LOG.debug("send rocketmq message async");
		} catch (Exception e) {
			LOG.error("消息发送失败，topic : {}, msgObj {}", message.getTopic(), message);
			throw new MQException("消息发送失败，topic :" + message.getTopic() + ",e:" + e.getMessage());
		}
	}
	
	@Override
	public void destory() {
		Iterable<Entry<String,DefaultMQProducer>> iterable = (Iterable) this.cache.entrySet().iterator();
		for(Entry<String,DefaultMQProducer> p : iterable) {
			try {
				this.removeMQProducer(p.getKey());
			} catch (MQException e) {
				e.printStackTrace();
			}
		}
	}
	
	public TransactionSendResult sendMessageInTransaction(String txProducerGroup, Message message, Object arg) throws MQException {
		try {
			TransactionMQProducer txProducer = this.stageMQProducer(txProducerGroup);
			return txProducer.sendMessageInTransaction(message, arg);
		} catch (MQClientException e) {
			throw new MQException(e.getErrorMessage());
		}
	}
	
	private void removeMQProducer(String txProducerGroup) throws MQException {
		if (this.cache.containsKey(txProducerGroup)) {
			DefaultMQProducer cachedProducer = (DefaultMQProducer)this.cache.get(txProducerGroup);
			cachedProducer.shutdown();
			this.cache.remove(txProducerGroup);
		}
		
	}
	
	public boolean createAndStartTransactionMQProducer(String txProducerGroup, TransactionListener transactionListener, ExecutorService executorService) throws MQException {
		if (this.cache.containsKey(txProducerGroup)) {
			LOG.info(String.format("get TransactionMQProducer '%s' from cache", txProducerGroup));
			return false;
		} else {
			TransactionMQProducer txProducer = this.createTransactionMQProducer(txProducerGroup, transactionListener, executorService);
			return startMQProducer(txProducerGroup, txProducer);
		}
	}
	
	private boolean startMQProducer(String txProducerGroup, DefaultMQProducer txProducer)
		throws MQException {
		try {
			txProducer.start();
			this.cache.put(txProducerGroup, txProducer);
			this.producerKeys.add(txProducerGroup);
			return true;
		} catch (MQClientException e) {
			throw new MQException(e.getErrorMessage());
		}
	}
	
	private TransactionMQProducer createTransactionMQProducer(String name, TransactionListener transactionListener, ExecutorService executorService) {
		TransactionMQProducer txProducer = new TransactionMQProducer(name);
		txProducer.setTransactionListener(transactionListener);
		if (executorService != null) {
			txProducer.setExecutorService(executorService);
		}
		configProducer(txProducer);
		return txProducer;
	}
	
	private void configProducer(DefaultMQProducer producer){
		producer.setNamesrvAddr(this.config.getNameServerAddr());
		producer.setSendMsgTimeout(this.config.getSendMessageTimeout());
		producer.setRetryTimesWhenSendFailed(this.config.getRetryTimesWhenSendFailed());
		producer.setRetryTimesWhenSendAsyncFailed(this.config.getRetryTimesWhenSendAsyncFailed());
		producer.setMaxMessageSize(this.config.getMaxMessageSize());
		producer.setCompressMsgBodyOverHowmuch(this.config.getCompressMessageBodyThreshold());
		producer.setRetryAnotherBrokerWhenNotStoreOK(this.config.isRetryNextServer());
	}
	
	
	public boolean createAndStartMQProducer(String producerGroup) throws MQException {
		if (this.cache.containsKey(producerGroup)) {
			LOG.info(String.format("get DefaultMQProducer '%s' from cache", producerGroup));
			return false;
		} else {
			DefaultMQProducer producer = this.createMQProducer(producerGroup);
			return startMQProducer(producerGroup,producer);
		}
	}
	private DefaultMQProducer createMQProducer(String name){
		DefaultMQProducer producer = new DefaultMQProducer(name);
		configProducer(producer);
		return producer;
	}
	
	
	
}
