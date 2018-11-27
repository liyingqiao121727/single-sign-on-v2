package com.liyingqiao.webflow;

import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

import org.springframework.webflow.execution.repository.impl.FlowExecutionSnapshotGroup;
import org.springframework.webflow.execution.repository.snapshot.FlowExecutionSnapshot;
import org.springframework.webflow.execution.repository.snapshot.SnapshotNotFoundException;

import redis.clients.jedis.JedisCommands;

public class LiyqFlowExecutionSnapshotGroup implements FlowExecutionSnapshotGroup, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String PRE_KEY = "FlowExecutionSnapshot:";
	private static int snapshotTimeout;
	
	public static void setSnapshotTimeout(int snapshotTimeout) {
		LiyqFlowExecutionSnapshotGroup.snapshotTimeout = snapshotTimeout;
	}

	@Override
	public FlowExecutionSnapshot getSnapshot(Serializable snapshotId) throws SnapshotNotFoundException {
		if (null == snapshotId) {
			return null;
		}
		String key = PRE_KEY + snapshotId.toString();
		Long timeout = (long) snapshotTimeout;
		JedisCommands jc = RedisManagement.getJedisCommands();
		String value = jc.get(key);
		timeout = jc.expire(key, timeout.intValue());
		try {
			RedisManagement.close(jc);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (null == value) {
			return null;
		}
		FlowExecutionSnapshot fes = null;
		try {
			fes = (FlowExecutionSnapshot) RedisManagement.strDeserialObj(value);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return fes;
	}

	@Override
	public void addSnapshot(Serializable snapshotId, FlowExecutionSnapshot snapshot) {
		if (null == snapshotId) {
			return;
		}
		String value = null;
		try {
			value = RedisManagement.objSerialStr(snapshot);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (null == value) {
			return;
		}
		String key = PRE_KEY + snapshotId.toString();
		String ret = key;
		Long timeout = (long) snapshotTimeout;
		JedisCommands jc = RedisManagement.getJedisCommands();
		ret = jc.set(ret, value);
		timeout = jc.expire(key, timeout.intValue());
		try {
			RedisManagement.close(jc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateSnapshot(Serializable snapshotId, FlowExecutionSnapshot snapshot) {
		this.addSnapshot(snapshotId, snapshot);
	}

	@Override
	public void removeSnapshot(Serializable snapshotId) {
		JedisCommands jc = RedisManagement.getJedisCommands();
		jc.del(PRE_KEY + snapshotId.toString());
		try {
			RedisManagement.close(jc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void removeAllSnapshots() {
	}

	@Override
	public int getSnapshotCount() {
		return 0;
	}

	@Override
	public Serializable nextSnapshotId() {
		return UUID.randomUUID();
	}

}
