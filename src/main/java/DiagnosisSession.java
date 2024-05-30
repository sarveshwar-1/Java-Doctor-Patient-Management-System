
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

class DiagnosisSession {
    private String sessionId;
    private String doctor;
    private String patient;
    private String symptoms;
    private String diagnosis;
    private String prescriptions;
    private String notes;
    private LocalDate date;
    private LocalTime time;

    public DiagnosisSession(Doctor doctor, Patient patient, String symptoms, String diagnosis, String prescriptions, String notes) {
        this.sessionId = UUID.randomUUID().toString();
        this.doctor = doctor.getName();
        this.patient = patient.getName();
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
        this.prescriptions = prescriptions;
        this.notes = notes;
        this.date = LocalDate.now();
        this.time = LocalTime.now();
    }

    public void AddSession() {
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase("mongodbjava");
            MongoCollection<Document> sessionsCollection = database.getCollection("sessions");

            Document sessionDoc = new Document("sessionId", sessionId)
                    .append("doctor", doctor)
                    .append("patient", patient)
                    .append("symptoms", symptoms)
                    .append("diagnosis", diagnosis)
                    .append("prescriptions", prescriptions)
                    .append("notes", notes)
                    .append("date", date.toString())
                    .append("time", time.toString());

            sessionsCollection.insertOne(sessionDoc);
        }
    }



    // Getters and setters for session fields
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    // public Doctor getDoctor() {
    //     return doctor;
    // }

    // public void setDoctor(Doctor doctor) {
    //     this.doctor = doctor;
    // }

    // public Patient getPatient() {
    //     return patient;
    // }

    // public void setPatient(Patient patient) {
    //     this.patient = patient;
    // }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(String prescriptions) {
        this.prescriptions = prescriptions;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
