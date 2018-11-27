package com.liyingqiao.webflow;

import java.io.Serializable;

import org.springframework.util.StringUtils;
import org.springframework.webflow.conversation.Conversation;
import org.springframework.webflow.conversation.ConversationManager;
import org.springframework.webflow.conversation.impl.SimpleConversationId;
import org.springframework.webflow.execution.FlowExecutionKey;
import org.springframework.webflow.execution.repository.BadlyFormattedFlowExecutionKeyException;
import org.springframework.webflow.execution.repository.FlowExecutionRepositoryException;
import org.springframework.webflow.execution.repository.impl.DefaultFlowExecutionRepository;
import org.springframework.webflow.execution.repository.impl.FlowExecutionSnapshotGroup;
import org.springframework.webflow.execution.repository.snapshot.FlowExecutionSnapshotFactory;
import org.springframework.webflow.execution.repository.support.CompositeFlowExecutionKey;

public class LiyqFlowExecutionRepository extends DefaultFlowExecutionRepository implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String SNAPSHOT_GROUP_ATTRIBUTE = "flowExecutionSnapshotGroup";
	
	public LiyqFlowExecutionRepository(ConversationManager conversationManager,
			FlowExecutionSnapshotFactory snapshotFactory) {
		super(conversationManager, snapshotFactory);
	}
	
	@Override
	protected FlowExecutionSnapshotGroup createFlowExecutionSnapshotGroup() {
		return new LiyqFlowExecutionSnapshotGroup();
	}

	/**
	 * Returns the snapshot group associated with the governing conversation.
	 * @param conversation the conversation where the snapshot group is stored
	 * @return the snapshot group
	 */
	@Override
	protected FlowExecutionSnapshotGroup getSnapshotGroup(Conversation conversation) {
		FlowExecutionSnapshotGroup group = (FlowExecutionSnapshotGroup) conversation
				.getAttribute(SNAPSHOT_GROUP_ATTRIBUTE);
		if (group == null) {
			group = new LiyqFlowExecutionSnapshotGroup();
			conversation.putAttribute(SNAPSHOT_GROUP_ATTRIBUTE, group);
		}
		return group;
	}
	
	@Override
	public FlowExecutionKey parseFlowExecutionKey(String encodedKey) throws FlowExecutionRepositoryException {
		if (!StringUtils.hasText(encodedKey)) {
			throw new BadlyFormattedFlowExecutionKeyException(encodedKey,
					"The string-encoded flow execution key is required");
		}
		String[] keyParts = CompositeFlowExecutionKey.keyParts(encodedKey);
		
		Serializable executionId = new SimpleConversationId(keyParts[0]);
		Serializable snapshotId = keyParts[1];
		return new CompositeFlowExecutionKey(executionId, snapshotId);
	}

}
