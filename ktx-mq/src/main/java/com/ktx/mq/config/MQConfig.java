package com.ktx.mq.config;

public class MQConfig {
	private String nameServerAddr;
	
	private String instanceName;
	
	private String group;
	
	
	public String getNameServerAddr() {
		return nameServerAddr;
	}
	
	public MQConfig setNameServerAddr(String nameServerAddr) {
		this.nameServerAddr = nameServerAddr;
		return this;
	}
	
	public String getInstanceName() {
		return instanceName;
	}
	
	public MQConfig setInstanceName(String instanceName) {
		this.instanceName = instanceName;
		return this;
	}
	
	public String getGroup() {
		return group;
	}
	
	public MQConfig setGroup(String group) {
		this.group = group;
		return this;
	}
	
}
