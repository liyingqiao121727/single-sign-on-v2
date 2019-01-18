package com.liyingqiao.webflow;

import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

import org.jasig.cas.jedis.RedisManagement;
import org.springframework.webflow.execution.repository.impl.FlowExecutionSnapshotGroup;
import org.springframework.webflow.execution.repository.snapshot.FlowExecutionSnapshot;
import org.springframework.webflow.execution.repository.snapshot.SnapshotNotFoundException;

public class LiyqFlowExecutionSnapshotGroup implements FlowExecutionSnapshotGroup, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String PRE_KEY = "FlowExecutionSnapshot:";
	private static int snapshotTimeout;
	private RedisManagement redisManagement;
	
	public LiyqFlowExecutionSnapshotGroup(RedisManagement redisManagement) {
		this.redisManagement = redisManagement;
	}
	
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
		String value = null;
		try {
			value = redisManagement.operate((jc) -> {
				String val = jc.get(key);
				Long.valueOf(jc.expire(key, timeout.intValue()));
				return val;
			});
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (null == value) {
			return null;
		}
		FlowExecutionSnapshot fes = null;
		try {
			fes = (FlowExecutionSnapshot) redisManagement.strDeserialObj(value);
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
			value = redisManagement.objSerialStr(snapshot);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (null == value) {
			return;
		}
		String key = PRE_KEY + snapshotId.toString();
		String fvalue = value;
		try {
			redisManagement.operate((jc) -> {
				String ret = jc.set(key, fvalue);
				Long.valueOf(jc.expire(key, snapshotTimeout));
				return ret;
			});
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
		try {
			redisManagement.operate((jc) -> {
				return jc.del(PRE_KEY + snapshotId.toString());
			});
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
