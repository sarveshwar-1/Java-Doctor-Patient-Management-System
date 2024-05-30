import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
public class Admin {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String dob;

    public Admin(String name, String email, String password, String phone, String address, String dob) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.dob = dob;
    }

    public void AddAdmin() {
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase("mongodbjava");
            MongoCollection<Document> collection = database.getCollection("admins");

            Document document = new Document("name", name)
                    .append("email", email)
                    .append("password", password)
                    .append("phone", phone)
                    .append("address", address)
                    .append("dob", dob);
            collection.insertOne(document);
        }
    }
}

