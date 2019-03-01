package com.ktx.mq.examples;

import java.io.Serializable;

public class TestUser implements Serializable {
	
	private static final long serialVersionUID = -6854298504526453726L;
	
	private Integer id;
	
	private String name;
	
	private String mobile;
	
	private Integer age;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getMobile() {
		return mobile;
	}
	
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public Integer getAge() {
		return age;
	}
	
	public void setAge(Integer age) {
		this.age = age;
	}
}
