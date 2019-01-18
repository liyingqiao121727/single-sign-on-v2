package com.liyingqiao.webflow;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.jasig.cas.jedis.RedisManagement;
import org.springframework.webflow.conversation.Conversation;
import org.springframework.webflow.conversation.ConversationId;
import org.springframework.webflow.conversation.ConversationLockException;

public class LiyqConversation implements Conversation, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String PRE_KEY = "LiyqConversation:";
	
	private static int conversationTimeout;

	private ConversationId id;
	
	private RedisManagement redisManagement;

	public LiyqConversation(ConversationId id, RedisManagement redisManagement) {
		this.id = id;
		this.redisManagement = redisManagement;
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
		try {
			redisManagement.operate((jc) -> {
				return jc.del(PRE_KEY + this.id.toString());
			});
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
		String value = null;
		final String fKey = key;
		try {
			value = redisManagement.operate((jc) -> {
				String val = jc.hget(hashKey, fKey);
				Long.valueOf(jc.expire(hashKey, timeout.intValue()));
				return val;
			});
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
		
		final String hashKey = PRE_KEY + id.toString();
		final String fKey = key;
		final String fval = val;
		try {
			redisManagement.operate((jc) -> {
				Long timeout = jc.hset(hashKey, fKey, fval);
				timeout = jc.expire(hashKey, conversationTimeout);
				return timeout;
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void removeAttribute(Object name) {
		String key = isWrapClass(name.getClass()) || name instanceof String ? name.toString() : null;
		if (null == key && null == (key = obj2String(name))) {
			return ;
		}
		final String fkey = key;
		try {
			redisManagement.operate((jc) -> {
				return jc.hdel(PRE_KEY + this.id.toString(), fkey);
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
