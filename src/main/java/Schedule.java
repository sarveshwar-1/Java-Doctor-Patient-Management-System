import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


public class Schedule{
    public String scheduleId;
    public String doctorId;
    public ArrayList <Appointment> appointments;
    public ArrayList <Appointment> appointmentRequests;
    
    public Schedule(String doctorId){
        this.scheduleId = UUID.randomUUID().toString();
        this.doctorId = doctorId;
        this.appointments = new ArrayList<>();
        this.appointmentRequests = new ArrayList<>();
    }

    public void addAppointment(Appointment appointment){
        this.appointments.add(appointment);
    }
    public void addAppointmentRequest(String id){
        try(MongoClient mongoClient = new MongoClient("localhost", 27017)){
            MongoDatabase database = mongoClient.getDatabase("mongodbjava");
            MongoCollection<Document> schedulesCollection = database.getCollection("schedules");
            Document myDoc = schedulesCollection.find(new Document("doctorid", doctorId)).first();
            if (myDoc != null) {
                ArrayList<String> appointmentRequests_id = (ArrayList<String>) myDoc.get("appointmentRequests");
                if (appointmentRequests_id == null) {
                    appointmentRequests_id = new ArrayList<>();
                }
                appointmentRequests_id.add(id);
                Document scheduleDoc = new Document("scheduleId", this.scheduleId)
                    .append("doctorId", this.doctorId)
                    .append("appointments", myDoc.get("appointments"))
                    .append("appointmentRequests", appointmentRequests_id);
                System.out.println(schedulesCollection.updateOne(new Document("scheduleId", this.scheduleId), new Document("$set", scheduleDoc)));
            } else {
                System.out.println("Schedule for the given doctor is not found");
            }}
            catch(Exception e){
                System.out.println("Error adding appointment request: " + e.getMessage());
            }}
    public void saveAppointment(){
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase("mongodbjava");
            MongoCollection<Document> schedulesCollection = database.getCollection("schedules");
            ArrayList<String> appointments_id = new ArrayList<>();
            ArrayList<String> appointmentRequests_id = new ArrayList<>();
            Document scheduleDoc = new Document("scheduleId", this.scheduleId)
                .append("doctorId", this.doctorId)
                .append("appointments", appointments_id)
                .append("appointmentRequests", appointmentRequests_id);
            schedulesCollection.insertOne(scheduleDoc);
        } catch (Exception e) {
            System.out.println("Error creating schedule: " + e.getMessage());
        }
    }
    public void updateAppointment(){
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase("mongodbjava");
            MongoCollection<Document> schedulesCollection = database.getCollection("schedules");
            ArrayList<String> appointments_id = new ArrayList<>();
            ArrayList<String> appointmentRequests_id = new ArrayList<>();
            for (Appointment appointment : this.appointments) {
                appointments_id.add(appointment.id);
            }
            for (Appointment appointment : this.appointmentRequests) {
                appointmentRequests_id.add(appointment.id);
            }
            Document scheduleDoc = new Document("scheduleId", this.scheduleId)
                .append("doctorId", this.doctorId)
                .append("appointments", appointments_id)
                .append("appointmentRequests", appointmentRequests_id);
            System.out.println(schedulesCollection.updateOne(new Document("scheduleId", this.scheduleId), new Document("$set", scheduleDoc)));
        } catch (Exception e) {
            System.out.println("Error updating schedule: " + e.getMessage());
        }
    }
    public void deleteAppointment(){
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase("mongodbjava");
            MongoCollection<Document> schedulesCollection = database.getCollection("schedules");
            schedulesCollection.deleteOne(new Document("scheduleId", this.scheduleId));
        } catch (Exception e) {
            System.out.println("Error deleting schedule: " + e.getMessage());
        }
    }
    public void printSchedule(){
        System.out.println("Schedule ID: " + this.scheduleId);
        System.out.println("Doctor ID: " + this.doctorId);
        System.out.println("Appointments: ");
        for (Appointment appointment : this.appointments) {
            appointment.printAppointment();
        }
        for (Appointment appointment : this.appointmentRequests) {
            appointment.printAppointment();
        }
    }
    public void confirmAppointmentRequest(String appointment_id){
        for (Appointment appointment : this.appointmentRequests) {
            if (appointment.id.equals(appointment_id)) {
                appointment.isConfirmed = true;
                this.appointments.add(appointment);
                this.appointmentRequests.remove(appointment);
                updateAppointment();
                break;
            }
        }
       
    }
    public void cancelAppointmentRequest(String id){
        for (Appointment appointment : this.appointmentRequests) {
            if (appointment.id.equals(id)) {
                appointment.isConfirmed = false;
                this.appointmentRequests.remove(appointment);
                updateAppointment();
                break;
            }
        }
    }
    public void printAppointmentRequests(){
        String doctorId = this.doctorId;
        System.out.println("Appointment Requests: ");
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase("mongodbjava");
            MongoCollection<Document> schedulesCollection = database.getCollection("schedules");
            MongoCollection<Document> appoinmentsCollection = database.getCollection("appointments");
            for (Document doc : schedulesCollection.find(new Document("doctorId", doctorId))) {
                ArrayList<String> appointmentRequests_id = (ArrayList<String>) doc.get("appointmentRequests");
                for (String id : appointmentRequests_id) {
                    Document myDoc = appoinmentsCollection.find(new Document("id", id)).first();
                    if (myDoc != null) {
                        Appointment appointment = new Appointment(myDoc.getString("patientId"), myDoc.getString("doctorId"), myDoc.getString("date"), myDoc.getString("time"));
                        appointment.id = myDoc.getString("id");
                        appointment.isConfirmed = myDoc.getBoolean("isConfirmed");
                        appointment.printAppointment();
                    } else {
                        System.out.println("Appointment not found");
                    }
                }
            }
        }
        catch(Exception e){
            System.out.println("Error printing appointment requests: " + e.getMessage());
        }


    }



}