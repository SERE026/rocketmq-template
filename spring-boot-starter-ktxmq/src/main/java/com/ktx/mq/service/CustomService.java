package com.ktx.mq.service;

import org.springframework.stereotype.Service;

@Service("customService")
public class CustomService {
	
	public void saveCustom(){
		System.out.println("----------custom-------------");
	}
}
