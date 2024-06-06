
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.bson.Document;
public class Hospital {
    private String name;
    private String address;

    public Hospital(String name, String address) {
        this.name = name;
        this.address = address;
    }

    // Getters and setters for hospital fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static Hospital getHospitalByName(String name) {
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase("mongodbjava");
            MongoCollection<Document> hospitalsCollection = database.getCollection("hospitals");
            
            Document doc = hospitalsCollection.find(Filters.eq("name", name)).first();
            if (doc != null) {
                String address = doc.getString("address");
                Hospital hospital = new Hospital(name, address);
                return hospital;
            }else {
                System.out.println("There is no hospital found with the name: " + name);
            }
        } catch(MongoException e){
            System.out.println("Error getting hospital from database: " + e.getMessage());
        }
        return null;
    }

    public void AddHospital() {
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase("mongodbjava");
            MongoCollection<Document> hospitalsCollection = database.getCollection("hospitals");
            Document hospitalDoc = new Document("name", this.name)
                    .append("address", this.address);
            hospitalsCollection.insertOne(hospitalDoc);
        }
    }

}