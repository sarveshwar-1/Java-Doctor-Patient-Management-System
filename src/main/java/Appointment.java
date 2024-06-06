import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import java.util.UUID;

import org.bson.Document;

public class Appointment {
    String id;
    private String patientId;
    private String doctorId;
    private String date;
    private String time;
    boolean isConfirmed;

    public Appointment(String patientId, String doctorId, String date, String time) {
        this.id = UUID.randomUUID().toString();
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.time = time;
        this.isConfirmed = false;
    }

    public static Appointment getAppointmentbyid(String id){
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)){
            MongoDatabase database = mongoClient.getDatabase("mongodbjava");
            MongoCollection<Document> appointmentsCollection = database.getCollection("appointments");
            Document myDoc = appointmentsCollection.find(Filters.eq("id", id)).first();
            String app_id = myDoc.getString("id");
            Appointment ap = new Appointment(myDoc.getString("patientId"), myDoc.getString("doctorId"), myDoc.getString("date"), myDoc.getString("time"));
            ap.id = app_id;
            return ap;
            

        } catch (Exception e) {
            System.out.println("Error reading appointment: " + e.getMessage());
            return null;
        }
    }
    public void createAppointment() {
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase("mongodbjava");
            MongoCollection<Document> appointmentsCollection = database.getCollection("appointments");
            Document appointmentDoc = new Document("patientId", this.patientId)
                    .append("id", this.id)
                    .append("doctorId", this.doctorId)
                    .append("date", this.date)
                    .append("time", this.time)
                    .append("isConfirmed", this.isConfirmed);
            appointmentsCollection.insertOne(appointmentDoc);
        } catch (Exception e) {
            System.out.println("Error creating appointment: " + e.getMessage());
        }
    }

    public Document readAppointment(String id) {
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase("mogodbjava");
            MongoCollection<Document> appointmentsCollection = database.getCollection("appointments");
            return appointmentsCollection.find(Filters.eq("_id", id)).first();
        } catch (Exception e) {
            System.out.println("Error reading appointment: " + e.getMessage());
            return null;
        }
    }

    public void updateAppointment(String id, String field, String value) {
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase("yourDatabaseName");
            MongoCollection<Document> appointmentsCollection = database.getCollection("appointments");
            appointmentsCollection.updateOne(Filters.eq("_id", id), new Document("$set", new Document(field, value)));
        } catch (Exception e) {
            System.out.println("Error updating appointment: " + e.getMessage());
        }
    }

    public void deleteAppointment(String id) {
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase("yourDatabaseName");
            MongoCollection<Document> appointmentsCollection = database.getCollection("appointments");
            appointmentsCollection.deleteOne(Filters.eq("_id", id));
        } catch (Exception e) {
            System.out.println("Error deleting appointment: " + e.getMessage());
        }
    }

    public void confirmAppointment() {
        this.isConfirmed = true;
        updateAppointment(this.id, "isConfirmed", "true");
    }

    public void cancelAppointment() {
        this.isConfirmed = false;
        updateAppointment(this.id, "isConfirmed", "false");
    }
    public void printAppointment(){
        System.out.println("Appointment ID: " + this.id);
        System.out.println("Patient ID: " + this.patientId);
        System.out.println("Doctor ID: " + this.doctorId);
        System.out.println("Date: " + this.date);
        System.out.println("Time: " + this.time);
        System.out.println("Is Confirmed: " + this.isConfirmed);
    }
    public String getId() {
        return id;
    }

}