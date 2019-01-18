package org.jasig.cas.jedis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;

public class RedisManagement {

	private JedisCluster jc = null;

	private JedisPool jedisPool = null;

	//spring instance it only
	//redis cluster
	private RedisManagement(final Set<HostAndPort> jedisClusterNode, final int connectionTimeout, 
			final int soTimeout, final int maxAttempts, final String password, 
			final GenericObjectPoolConfig poolConfig) throws IOException {
		jc = new JedisCluster(jedisClusterNode, connectionTimeout, 
				soTimeout, maxAttempts, password, poolConfig);
		jc.get("liyingqiao");
	}

	private RedisManagement(final GenericObjectPoolConfig poolConfig, final String host, int port,
			final int connectionTimeout, final int soTimeout, final String password, final int database,
			final String clientName, final boolean ssl, final SSLSocketFactory sslSocketFactory,
			final SSLParameters sslParameters, final HostnameVerifier hostnameVerifier) {
		jedisPool = new JedisPool(poolConfig, host, port, connectionTimeout, soTimeout, 
				password, database, clientName, ssl, sslSocketFactory, 
				sslParameters, hostnameVerifier);
		Jedis jedis = jedisPool.getResource();
		jedis.get("liyingqiao");
		jedis.close();
	}

	/*public static JedisCluster getJedisCluster() {
		return jc;
	}

	public static Jedis getJedis() {
		return jedisPool.getResource();
	}*/

	/*private JedisCommands getJedisCommands() {
		return jc == null ? jedisPool.getResource() : jc;
	}*/

	public <T> T operate(RedisOperate<T> redisOpr) throws IOException {
		T t = null;
		if (jc != null) {
			t = redisOpr.operate(jc);
		} else {
			Jedis jedis = jedisPool.getResource();
			t = redisOpr.operate(jedis);
			jedis.close();
		}

		return t;
	}

	/*private void close(JedisCommands jedisCmd) throws IOException {
		if (jedisCmd instanceof Jedis) {
			((Jedis) jedisCmd).close();
		}
	}*/

	public String objSerialStr(Object obj) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(obj);
		return Base64.getEncoder().encodeToString(baos.toByteArray());
	}

	public Object strDeserialObj(String value) throws IOException, ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(value));
		ObjectInputStream oin = new ObjectInputStream(in);
		return oin.readObject();
	}

	public static interface RedisOperate<T> {
		T operate(JedisCommands jedisCmd);
	}

}
