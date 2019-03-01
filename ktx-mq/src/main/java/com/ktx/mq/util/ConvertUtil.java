package com.ktx.mq.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;

public class ConvertUtil {
	
	public static ObjectMapper objectMapper = new ObjectMapper();
	
	static {
		// 转换为格式化的json
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		// 如果json中有新增的字段并且是实体类类中不存在的，不报错
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	public static Object bytesConvertObj(byte[] bytes, Class classType) {
		try {
//			RemotingSerializable.encode()
			return objectMapper.readValue(bytes, classType);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static byte[] objConvertBytes(Object data) {
		try {
			return objectMapper.writeValueAsBytes(data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static Object strConvertObj(String str, Class classType) {
		try {
			return objectMapper.readValue(str, classType);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String objConvertStr(Object data) {
		try {
			return objectMapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
