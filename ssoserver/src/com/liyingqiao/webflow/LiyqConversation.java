package com.liyingqiao.webflow;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.webflow.conversation.Conversation;
import org.springframework.webflow.conversation.ConversationId;
import org.springframework.webflow.conversation.ConversationLockException;

import redis.clients.jedis.JedisCommands;

public class LiyqConversation implements Conversation, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String PRE_KEY = "LiyqConversation:";
	
	private static int conversationTimeout;

	private ConversationId id;

	public LiyqConversation(ConversationId id) {
		this.id = id;
	}
	
	public static void setConversationTimeout(int conversationTimeout) {
		LiyqConversation.conversationTimeout = conversationTimeout;
	}

	@Override
	public ConversationId getId() {
		return id;
	}

	@Override
	public void lock() throws ConversationLockException {
	}

	@Override
	public void unlock() {
	}

	@Override
	public void end() {
		JedisCommands jc = RedisManagement.getJedisCommands();
		jc.del(PRE_KEY + this.id.toString());
		try {
			RedisManagement.close(jc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean equals(Object obj) {
		return obj instanceof LiyqConversation && this.id.equals(((LiyqConversation) obj).id);
	}

	public int hashCode() {
		return this.id.hashCode();
	}

	@Override
	public Object getAttribute(Object name) {
		String key = isWrapClass(name.getClass()) || name instanceof String ? name.toString() : null;
		if (null == key && null == (key = obj2String(name))) {
			return null;
		}
		Long timeout = (long) conversationTimeout;
		String hashKey = PRE_KEY + id.toString();
		JedisCommands jc = RedisManagement.getJedisCommands();
		String value = jc.hget(hashKey, key);
		timeout = jc.expire(hashKey, timeout.intValue());
		try {
			RedisManagement.close(jc);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (null == value) {
			return null;
		}

		Object obj = null;
		try (ByteArrayInputStream bais = new ByteArrayInputStream(
				Base64.getDecoder().decode(value));
				ObjectInputStream ois = new ObjectInputStream(bais);) {
			obj = ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			obj = value;
		}

		return obj;
	}

	@Override
	public void putAttribute(Object name, Object value) {
		String key = isWrapClass(name.getClass()) || name instanceof String ? name.toString() : null;
		if (null == key && null == (key = obj2String(name))) {
			return ;
		}
		if (null == value) {
			return ;
		}
		String val = isWrapClass(value.getClass()) || value instanceof String ? value.toString() : null;
		if ((null == val && null == (val = obj2String(value))) || null == val) {
			return ;
		}
		
		String hashKey = PRE_KEY + id.toString();
		JedisCommands jc = RedisManagement.getJedisCommands();
		Long timeout = jc.hset(hashKey, key, val);
		timeout = (long) conversationTimeout;
		timeout = jc.expire(hashKey, timeout.intValue());
		try {
			RedisManagement.close(jc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void removeAttribute(Object name) {
		String key = isWrapClass(name.getClass()) || name instanceof String ? name.toString() : null;
		if (null == key && null == (key = obj2String(name))) {
			return ;
		}
		JedisCommands jc = RedisManagement.getJedisCommands();
		jc.hdel(PRE_KEY + this.id.toString(), key);
		try {
			RedisManagement.close(jc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String obj2String(Object obj) {
		String key = null;
		try (final ByteArrayOutputStream baos = new ByteArrayOutputStream();
				final ObjectOutputStream oos = new ObjectOutputStream(baos);) {
			oos.writeObject(obj);
			oos.flush();
			baos.flush();
			key = Base64.getEncoder().encodeToString(baos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return key;
	}

	private static boolean isWrapClass(Class<?> clz) {
		try {
			return ((Class<?>) clz.getField("TYPE").get(null)).isPrimitive();
		} catch (Exception e) {
			return false;
		}
	}

}
