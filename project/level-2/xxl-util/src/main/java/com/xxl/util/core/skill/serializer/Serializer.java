package com.xxl.util.core.skill.serializer;

import com.xxl.util.core.skill.serializer.impl.HessianSerializer;
import com.xxl.util.core.skill.serializer.impl.JacksonSerializer;
import com.xxl.util.core.skill.serializer.impl.ProtostuffSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * 序列化器
 * Tips：模板方法模式：
 * 定义：定义一个操作中算法的骨架（或称为顶级逻辑），将一些步骤（或称为基本方法）的执行延迟到其子类中；
 * 基本方法：抽象方法 + 具体方法final + 钩子方法；
 * @author xuxueli 2015-10-30 21:02:55
 *
 *
 * 	<!-- protostuff -->
	<dependency>
		<groupId>com.dyuproject.protostuff</groupId>
		<artifactId>protostuff-core</artifactId>
		<version>1.0.8</version>
	</dependency>
	<dependency>
	<groupId>com.dyuproject.protostuff</groupId>
		<artifactId>protostuff-runtime</artifactId>
		<version>1.0.8</version>
	</dependency>
	<!-- objenesis(support protostuff) -->
	<dependency>
		<groupId>org.objenesis</groupId>
		<artifactId>objenesis</artifactId>
		<version>2.1</version>
	</dependency>
	<!-- hessian -->
	<dependency>
		<groupId>com.caucho</groupId>
		<artifactId>hessian</artifactId>
		<version>4.0.38</version>
	</dependency>
	<!-- jackson -->
	<dependency>
		<groupId>com.fasterxml.jackson.core</groupId>
		<artifactId>jackson-databind</artifactId>
		<version>2.5.4</version>
	</dependency>
 *
 *
 */
public abstract class Serializer {
	
	public abstract <T> byte[] serialize(T obj);
	public abstract <T> Object deserialize(byte[] bytes, Class<T> clazz);
	
	public enum SerializeType {
		HESSIAN, JSON, PROTOSTUFF;
	}
	
	private static Map<String, Serializer> serializerMap = new HashMap<String, Serializer>();
	static{
		serializerMap.put(SerializeType.HESSIAN.name(), new HessianSerializer());
		serializerMap.put(SerializeType.JSON.name(), new JacksonSerializer());
		serializerMap.put(SerializeType.PROTOSTUFF.name(), new ProtostuffSerializer());
	}
	
	public static Serializer getInstance(String serialize){
		if (serialize != null && serialize.trim().length() > 0) {
			Serializer serializer = serializerMap.get(serialize);
			if (serializer != null) {
				return serializer;
			}
		}
		return serializerMap.get(SerializeType.HESSIAN.name());
	}
	
	public static void main(String[] args) {
		Serializer serializer = Serializer.getInstance(null);
		System.out.println(serializer);
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("aaa", "111");
			map.put("bbb", "222");
			System.out.println(serializer.deserialize(serializer.serialize("ddddddd"), String.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
