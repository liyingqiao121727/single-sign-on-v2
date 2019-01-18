package com.liyingqiao.test;

public class Test {

    /*public static void main(String[] args) { 
    	new MongoManagement().setConnectionString("mongodb://172.20.53.122:28016");
    	MongoCollection<Document> collection = MongoManagement.getMongoClient().getDatabase("sso_user").getCollection("permission");
    	Document docPermission = Document.parse("{name: 'liyingqiao'}");
    	collection.insertOne(docPermission);
    	ObjectId permissionOid = docPermission.getObjectId("_id");
    	collection = MongoManagement.getMongoClient().getDatabase("sso_user").getCollection("role");
    	Document docRole = Document.parse("{rolename: 'liyingqiao'}");
    	collection.insertOne(docRole);
    	ObjectId roleOid = docRole.getObjectId("_id");
    	collection = MongoManagement.getMongoClient().getDatabase("sso_user").getCollection("role_permission");
    	Document doc = new Document("_id", roleOid);
    	doc.put("permission_id", permissionOid);
    	collection.insertOne(doc);
    	StringBuilder sb = new StringBuilder("{'username':").append('\'').append("liyingqiao666").append('\'').append('}');
    	Document doc = MongoManagement.findOne("sso_user", "user", sb.toString());
        final String cachedPassword = doc.getString("password");
        System.err.println(cachedPassword);
    	Document userDoc = MongoManagement.findOne("sso_user", "role", "{'name':'liyingqiao'}");
    	
    	MongoCollection<Document> collection = MongoManagement.getMongoClient().getDatabase("sso_user").getCollection("user_role");
    	Document userRoleDoc = new Document();
    	userRoleDoc.put("user_id", userDoc.getObjectId("_id"));
    	collection.insertOne(userRoleDoc);
    	MongoCollection<Document> collection = MongoManagement.getMongoClient().getDatabase("sso_user").getCollection("user_role");
    	Document userRoleDoc = new Document();
    	userRoleDoc.put("user_id", userDoc.getObjectId("_id"));
    	FindIterable<Document> finds = collection.find(userRoleDoc);
    	List<ObjectId> list = new LinkedList<ObjectId>();
    	Consumer<Document> c = (doc) -> {
    		list.add(doc.getObjectId("_id"));
    	};
		finds.forEach(c);
		
		Document permissionDoc = MongoManagement.findOne("sso_user", "permission", "{'name':'liyingqiao'}");
		
		collection = MongoManagement.getMongoClient().getDatabase("sso_user").getCollection("role_permission");
    	Document doc = new Document("role_id", list.get(0));
    	doc.put("permission_id", permissionDoc.getObjectId("_id"));
    	ObjectId o = collection.find(doc).first().getObjectId("role_id");
    	collection = MongoManagement.getMongoClient().getDatabase("sso_user").getCollection("role");
    	Document docRole = new Document();
    	docRole.put("name", "liyingqiao");
    	docRole.put("_id", o);
    	collection.insertOne(docRole);
        
        LinkedList<Bson> pipeline = new LinkedList<Bson>();
        pipeline.add(BsonDocument.parse("{$lookup:{from:'permission',localField: 'permission_id'," + 
        		"foreignField: '_id', as: 'permission'}}"));

        pipeline.add(BsonDocument.parse("{$lookup:{from:'role',localField: 'role_id'," + 
        		"foreignField: '_id', as: 'role'}}"));
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("$match", new BasicDBObject("name", "liyingqiao"));
        pipeline.add(basicDBObject);
        
		List<Document> target = new LinkedList<Document>();
		MongoManagement.getMongoClient().getDatabase("sso_user")
		.getCollection("role").aggregate(pipeline).into(target);
		System.out.println(target);
	}*/
    
    /*public static class Liyingqiao implements Serializable {
		
		private static final long serialVersionUID = 1L;
		
		private String liyingqiao;
		private Long l;

		public String getLiyingqiao() {
			return liyingqiao;
		}
		public void setLiyingqiao(String liyingqiao) {
			this.liyingqiao = liyingqiao;
		}
		public Long getL() {
			return l;
		}
		public void setL(Long l) {
			this.l = l;
		}
	}*/

    /*public static void main(String[] args) throws IOException {
    	
    	File f = new File("liyingqiao.txt");
    	if (!f.exists()) {
    		f.createNewFile();
    	}
    	
    	FileOutputStream fos = new FileOutputStream(f);
    	
    	
    	Liyingqiao ll = new Liyingqiao();
		ll.setLiyingqiao("123");
		ll.setL(55L);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(ll);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//this.cache.put(ticket.getId(), baos.toString());
		FileInputStream fis = new FileInputStream(f);
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toString().getBytes());
		Liyingqiao ticket1 = null;
		try (ObjectInputStream ois = new ObjectInputStream(fis)) {
			ticket1 = (Liyingqiao) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		System.err.println(ticket1);
	}*/
    
    /*public static void main(String[] args) throws InterruptedException {
		Jedis jedis = new Jedis("127.0.0.1", 6379);
		//jedis.set("liyingqiao", "good", "", "", 5);
		String result = jedis.get("mykey");
		System.out.println(result);
		Thread.sleep(17000);
		result = jedis.get("mykey");
		System.out.println(result);
	}*/
	
	public static void main(String[] args) {
		Byte[] b = {};
		Object[] o = b;
		System.out.println(o);
		//DefaultExports.initialize();
		/*io.prometheus.client.filter.MetricsFilter mf = null;
		io.prometheus.client.exporter.MetricsServlet ms = null;
		io.prometheus.
		DefaultExports.initialize();*/
		
	}
}
