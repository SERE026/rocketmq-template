package com.ktx.mq.config;

public class ProducerConfig extends MQConfig{
	
	private int sendMessageTimeout = 3000;
	private int compressMessageBodyThreshold = 4096;
	private int retryTimesWhenSendFailed = 2;
	private int retryTimesWhenSendAsyncFailed = 2;
	private boolean retryNextServer = false;
	private int maxMessageSize = 1024 * 1024 * 4;
	
	public int getSendMessageTimeout() {
		return sendMessageTimeout;
	}
	
	public void setSendMessageTimeout(int sendMessageTimeout) {
		this.sendMessageTimeout = sendMessageTimeout;
	}
	
	public int getCompressMessageBodyThreshold() {
		return compressMessageBodyThreshold;
	}
	
	public void setCompressMessageBodyThreshold(int compressMessageBodyThreshold) {
		this.compressMessageBodyThreshold = compressMessageBodyThreshold;
	}
	
	public int getRetryTimesWhenSendFailed() {
		return retryTimesWhenSendFailed;
	}
	
	public void setRetryTimesWhenSendFailed(int retryTimesWhenSendFailed) {
		this.retryTimesWhenSendFailed = retryTimesWhenSendFailed;
	}
	
	public int getRetryTimesWhenSendAsyncFailed() {
		return retryTimesWhenSendAsyncFailed;
	}
	
	public void setRetryTimesWhenSendAsyncFailed(int retryTimesWhenSendAsyncFailed) {
		this.retryTimesWhenSendAsyncFailed = retryTimesWhenSendAsyncFailed;
	}
	
	public boolean isRetryNextServer() {
		return retryNextServer;
	}
	
	public void setRetryNextServer(boolean retryNextServer) {
		this.retryNextServer = retryNextServer;
	}
	
	public int getMaxMessageSize() {
		return maxMessageSize;
	}
	
	public void setMaxMessageSize(int maxMessageSize) {
		this.maxMessageSize = maxMessageSize;
	}
}
