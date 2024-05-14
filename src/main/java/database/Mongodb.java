package database;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class Mongodb {
	public MongoClient mongoClient;
	public Mongodb(){
		this.mongoClient = new MongoClient("localhost", 27017);
		System.out.println("DB connected");
	}
	public MongoClient getMongoClient() {
		return this.mongoClient;
	}

	public MongoCursor getCursor(String Collection){
		MongoDatabase db = this.mongoClient.getDatabase("mongodbjava");
		MongoCollection<Document> collection = db.getCollection(Collection);
		MongoCursor<Document> cursor = collection.find().iterator();
		return cursor;
	}
	public MongoCursor getCursor(String Collection,String Name){
		MongoDatabase db = this.mongoClient.getDatabase("mongodbjava");
		MongoCollection<Document> collection = db.getCollection(Collection);
		Document query =new Document("name",Name);
		MongoCursor<Document> cursor = collection.find(query).iterator();
		return cursor;

	}
	public MongoCollection getCollection(String Collection){
		MongoDatabase db = this.mongoClient.getDatabase("mongodbjava");
		MongoCollection<Document> collection = db.getCollection(Collection);
		return collection;
	}
	// }
	// public void AddDoctor( Doctor doctor){
	// 	MongoDatabase db = this.mongoClient.getDatabase("doctors");
	// 	MongoCollection<org.bson.Document> collection = db.getCollection("doctors");
	// 	org.bson.Document doc = new org.bson.Document();

	// }
	// public static void main(String[] args) {


	// 	MongoDatabase db = mongoClient.getDatabase("mongodbjava");
	// 	System.out.println("Get database is successful");
	// 	System.out.println("Below are list of databases present in MongoDB");

	// 	MongoCollection<org.bson.Document>  
	// 	collection= db.getCollection("Doctors");
	// 	org.bson.Document doc =(org.bson.Document) new org.bson.Document("Sam","onthropologist");
	// 	collection.insertOne(doc);
	// 	System.out.println("########### Insertion is completed  ###############");

	// 	MongoCursor<String> dbsCursord = mongoClient.listDatabaseNames().iterator();
	// 	while(dbsCursord.hasNext()) {
	// 			System.out.println(dbsCursord.next());
		
	// 		}


	// }
}
