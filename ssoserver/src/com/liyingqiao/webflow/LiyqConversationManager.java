package com.liyingqiao.webflow;

import java.io.Serializable;
import java.util.UUID;

import org.jasig.cas.jedis.RedisManagement;
import org.springframework.webflow.conversation.Conversation;
import org.springframework.webflow.conversation.ConversationException;
import org.springframework.webflow.conversation.ConversationId;
import org.springframework.webflow.conversation.ConversationManager;
import org.springframework.webflow.conversation.ConversationParameters;
import org.springframework.webflow.conversation.impl.SimpleConversationId;

public class LiyqConversationManager implements ConversationManager, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private RedisManagement redisManagement;
	
	public LiyqConversationManager(RedisManagement redisManagement) {
		this.redisManagement = redisManagement;
	}

	@Override
	public Conversation beginConversation(ConversationParameters conversationParameters) throws ConversationException {
		Conversation c = new LiyqConversation(new SimpleConversationId(UUID.randomUUID()), redisManagement);
		c.putAttribute("name", conversationParameters.getName());
		c.putAttribute("caption", conversationParameters.getCaption());
		c.putAttribute("description", conversationParameters.getDescription());
		return c;
	}

	@Override
	public Conversation getConversation(ConversationId id) throws ConversationException {
		return new LiyqConversation(id, redisManagement);
	}

	@Override
	public ConversationId parseConversationId(String encodedId) throws ConversationException {
		return new SimpleConversationId(encodedId);
	}

}
