package org.jasig.cas.mongo;

import org.bson.BsonDocument;
import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.connection.SocketSettings;

public class MongoManagement {

	private static ConnectionString connStr;

	private static MongoClient mongoClient;
	
	//spring instance it only
	private MongoManagement() {
		
	}
	
	public void setConnectionString(String connectionString) {
		if (null == connStr) {
			connStr =  new ConnectionString(connectionString);
		}
		if (null == mongoClient) {
			Block<ClusterSettings.Builder> blockCluster = (builder) -> {
				//ConnectionString connectionString = new ConnectionString("");
				builder.applyConnectionString(connStr);
				//builder.serverSelector(serverSelector);
			}; 
			Block<ConnectionPoolSettings.Builder> blockPool = (builder) -> {
				builder.applyConnectionString(connStr);
				//builder.maintenanceFrequency(maintenanceFrequency, timeUnit);
				//builder.maintenanceInitialDelay(maintenanceInitialDelay, timeUnit);
			};
			Block<SocketSettings.Builder> blockSocket = (builder) -> {
				builder.applyConnectionString(connStr);
				builder.receiveBufferSize(0).sendBufferSize(0);
			};
			/*new Block<ClusterSettings.Builder>() {

		         @Override
		         public void apply(Builder t) {
		          }
	             };*/
			MongoClientSettings settings = MongoClientSettings.builder()
					.applyToClusterSettings(blockCluster)
					.applyToConnectionPoolSettings(blockPool)
					.applyToSocketSettings(blockSocket).build();
			//通过连接认证获取MongoDB连接  
			mongoClient = MongoClients.create(settings); 
		}
	}
	
	public static Document findOne(String dbName, String collectionName, String json) {
		return mongoClient.getDatabase(dbName)
				.getCollection(collectionName).find(BsonDocument.parse(json)).first();
	}

	public static MongoClient getMongoClient() {
		return mongoClient;
	}

}
