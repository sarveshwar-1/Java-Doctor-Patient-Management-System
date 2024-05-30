
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import java.util.ArrayList;
import org.bson.Document;


public class Patient extends User {
    private String bloodGroup;
    private int height;
    private int weight;
    private ArrayList<DiagnosisSession> sessions;
    ArrayList<String> doctors;
    ArrayList<Report> reports;


    public Patient(String name, String email, String password, String phone, String address, String dob, String bloodGroup, int height, int weight) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.dob = dob;
        this.bloodGroup = bloodGroup;
        this.height = height;
        this.weight = weight;
        this.sessions = new ArrayList<>();
        this.doctors = new ArrayList<>();
        this.reports = new ArrayList<>();

    }

    public void AddPatient() {
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase("mongodbjava");
            MongoCollection<Document> patientsCollection = database.getCollection("patients");
            Document patientDoc = new Document("name", this.name)
                .append("email", this.email)
                .append("password", this.password)
                .append("phone", this.phone)
                .append("address", this.address)
                .append("dob", this.dob)
                .append("bloodGroup", this.bloodGroup)
                .append("height", this.height)
                .append("weight", this.weight)
                .append("sessions", this.sessions)
                .append("doctors", this.doctors)
                .append("reports", this.reports);
            patientsCollection.insertOne(patientDoc);
        }
    }

    

    public void viewPatient() {
        // Display patient details
        System.out.println("Patient Details:");
        System.out.println("Name: " + this.name);
        System.out.println("Email: " + this.email);
        System.out.println("Phone: " + this.phone);
        System.out.println("Address: " + this.address);
        System.out.println("DOB: " + this.dob);
        System.out.println("Blood Group: " + this.bloodGroup);
        System.out.println("Height: " + this.height);
        System.out.println("Weight: " + this.weight);
    }

    public static Patient getPatientByName(String name) {
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase("mongodbjava");
            MongoCollection<Document> patientsCollection = database.getCollection("patients");

            Document doc = patientsCollection.find(Filters.eq("name", name)).first();
            if (doc != null) {
                String email = doc.getString("email");
                String password = doc.getString("password");
                String phone = doc.getString("phone");
                String address = doc.getString("address");
                String dob = doc.getString("dob");
                String bloodGroup = doc.getString("bloodGroup");
                int height = doc.getInteger("height");
                int weight = doc.getInteger("weight");
                Patient patient = new Patient(name, email, password, phone, address, dob, bloodGroup, height, weight);
                return patient;
            }
        }
        return null;
    }

    // Additional getters and setters for patient-specific fields
    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public ArrayList<DiagnosisSession> getSessions() {
        return sessions;
    }

    public void setSessions(ArrayList<DiagnosisSession> sessions) {
        this.sessions = sessions;
    }
}
