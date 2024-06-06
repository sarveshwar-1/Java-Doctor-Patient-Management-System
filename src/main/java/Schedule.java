import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


public class Schedule{
    public String scheduleId;
    public String doctorName;
    public ArrayList <Appointment> appointments;
    public ArrayList <Appointment> appointmentRequests;
    
    public Schedule(String Name){
        this.scheduleId = UUID.randomUUID().toString();
        this.doctorName = Name;
        this.appointments = new ArrayList<>();
        this.appointmentRequests = new ArrayList<>();
    }
    public static Schedule getScheduleByDoctorName(String doctorName){
        try(MongoClient mongoClient = new MongoClient("localhost", 27017)){
            MongoDatabase database = mongoClient.getDatabase("mongodbjava");
            MongoCollection<Document> schedulesCollection = database.getCollection("schedules");
            Document myDoc = schedulesCollection.find(new Document("doctorName", doctorName)).first();
            if (myDoc != null) {
                Schedule schedule = new Schedule(myDoc.getString("doctorId"));
                schedule.doctorName = myDoc.getString("doctorName");
                schedule.scheduleId = myDoc.getString("scheduleId");
                ArrayList<String> appointments_id = (ArrayList<String>) myDoc.get("appointments");
                ArrayList<String> appointmentRequests_id = (ArrayList<String>) myDoc.get("appointmentRequests");
                for (String id : appointments_id) {
                    Appointment appointment = Appointment.getAppointmentbyid(id);
                    schedule.appointments.add(appointment);
                }
                for (String id : appointmentRequests_id) {
                    Appointment appointment = Appointment.getAppointmentbyid(id);
                    schedule.appointmentRequests.add(appointment);
                }
                return schedule;
            } else {
                System.out.println("Schedule not found");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error reading schedule: " + e.getMessage());
            return null;
        }
    }

    public Schedule getSchedule(String scheduleId){
        try(MongoClient mongoClient = new MongoClient(null, 27017)){
            MongoDatabase database = mongoClient.getDatabase("mongodbjava");
            MongoCollection<Document> schedulesCollection = database.getCollection("schedules");
            Document myDoc = schedulesCollection.find(new Document("scheduleId", scheduleId)).first();
            if (myDoc != null) {
                Schedule schedule = new Schedule(myDoc.getString("doctorId"));
                schedule.scheduleId = myDoc.getString("scheduleId");
                ArrayList<String> appointments_id = (ArrayList<String>) myDoc.get("appointments");
                ArrayList<String> appointmentRequests_id = (ArrayList<String>) myDoc.get("appointmentRequests");
                for (String id : appointments_id) {
                    Appointment appointment = new Appointment(appointments_id.get(0), null, null, null);
                    appointment.id = id;
                    schedule.appointments.add(appointment);
                }
                for (String id : appointmentRequests_id) {
                    Appointment appointment = new Appointment(null, null, null, null);
                    appointment.id = id;
                    schedule.appointmentRequests.add(appointment);
                }
                return schedule;
            } else {
                System.out.println("Schedule not found");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error reading schedule: " + e.getMessage());
            return null;
        }

    }
    public void addAppointment(Appointment appointment){
        this.appointments.add(appointment);
    }
    public void addAppointmentRequest(String id){
        try(MongoClient mongoClient = new MongoClient("localhost", 27017)){
            MongoDatabase database = mongoClient.getDatabase("mongodbjava");
            MongoCollection<Document> schedulesCollection = database.getCollection("schedules");
            System.out.println(doctorName);
            Document myDoc = schedulesCollection.find(new Document("doctorName", doctorName)).first();
            if (myDoc != null) {
                ArrayList<String> appointmentRequests_id = (ArrayList<String>) myDoc.get("appointmentRequests");
                if (appointmentRequests_id == null) {
                    appointmentRequests_id = new ArrayList<>();
                }
                appointmentRequests_id.add(id);
                Document scheduleDoc = new Document("scheduleId", this.scheduleId)
                    .append("doctorName", this.doctorName)
                    .append("appointments", myDoc.get("appointments"))
                    .append("appointmentRequests", appointmentRequests_id);
                schedulesCollection.updateOne(new Document("scheduleId", this.scheduleId), new Document("$set", scheduleDoc));
            } 
            else {
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
                .append("doctorName", this.doctorName)
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
                .append("doctorName", this.doctorName)
                .append("appointments", appointments_id)
                .append("appointmentRequests", appointmentRequests_id);
            schedulesCollection.updateOne(new Document("scheduleId", this.scheduleId), new Document("$set", scheduleDoc));
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
        System.out.println("Doctor Name: " + this.doctorName);
        System.out.println("Appointments: ");
        for (Appointment appointment : this.appointments) {
            appointment.printAppointment();
        }
        for (Appointment appointment : this.appointmentRequests) {
            appointment.printAppointment();
        }
    }
    public void confirmAppointmentRequest(String appointment_id){
        System.out.println(appointment_id);
        for (Appointment appointment : this.appointmentRequests) {
            System.out.println(appointment.id);
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
        String doctorName = this.doctorName;
        System.out.println("Appointment Requests: ");
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase("mongodbjava");
            MongoCollection<Document> schedulesCollection = database.getCollection("schedules");
            MongoCollection<Document> appoinmentsCollection = database.getCollection("appointments");
            for (Document doc : schedulesCollection.find(new Document("doctorName", doctorName))) {
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
       public String getId(){
            return this.scheduleId;
        }
    



}