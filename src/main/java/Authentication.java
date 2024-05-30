

import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
public class Authentication {
    private MongoClient mongoClient;
    private MongoDatabase database;

    public Authentication() {
        this.mongoClient = new MongoClient("localhost", 27017);
        this.database = mongoClient.getDatabase("mongodbjava");
    }

    public Choice login(String email, String password) {
        // Check if email and password match an entry in the database
        MongoCollection<Document> adminsCollection = database.getCollection("admins");
        Document adminDoc = adminsCollection.find(Filters.eq("email", email)).first();
        if (adminDoc != null && adminDoc.getString("password").equals(DPM.hashPassword(password))) {
            Choice ch = new Choice(3, email);
            return ch; // Admin
        }

        MongoCollection<Document> doctorsCollection = database.getCollection("doctors");
        Document doctorDoc = doctorsCollection.find(Filters.eq("email", email)).first();
        if (doctorDoc != null && doctorDoc.getString("password").equals(DPM.hashPassword(password))) {
            Choice ch = new Choice(1, email);
            return ch; // Doctor
        }

        MongoCollection<Document> patientsCollection = database.getCollection("patients");
        Document patientDoc = patientsCollection.find(Filters.eq("email", email)).first();
        if (patientDoc != null && patientDoc.getString("password").equals(DPM.hashPassword(password))) {
            Choice ch = new Choice(2, email);
            return ch;
        }
        Choice ch = new Choice(0, "none");
        return ch; // Login failed
    }

}
