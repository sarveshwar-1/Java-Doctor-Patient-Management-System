
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import database.Mongodb;

public class DPM {
    public static void main(String[] args) {
        System.out.println("hello");

        Hospital hospital1 = new Hospital("Apollo", "Chennai", "1234567890", "apollo@gmail.com");
        Hospital hospital2 = new Hospital("Max Healthcare", "Delhi", "9876543210", "maxhealthcare@example.com");
        Hospital hospital3 = new Hospital("Fortis", "Mumbai", "5555555555", "fortis@example.com");

        // Create doctors
        Doctor doctor1 = new Doctor("Dr. Raj", "raj@gmail.com", "1234567890", "1234567890", "Chennai", "12/12/1990", hospital1, "Cardiologist");
        Doctor doctor2 = new Doctor("Dr. Priya", "priya@example.com", "9876543210", "9876543210", "Delhi", "05/03/1985", hospital2, "Pediatrician");
        Doctor doctor3 = new Doctor("Dr. Amit", "amit@example.com", "5555555555", "5555555555", "Mumbai", "20/08/1992", hospital3, "Orthopedic Surgeon");

        // Create patients
        Patient patient1 = new Patient("Rahul", "rahul@gmail.com", "1234567890", "1234567890", "Chennai", "12/12/1990", "A+", 180, 70);
        Patient patient2 = new Patient("Neha", "neha@example.com", "9876543210", "9876543210", "Delhi", "25/06/1995", "O-", 165, 55);
        Patient patient3 = new Patient("Sameer", "sameer@example.com", "5555555555", "5555555555", "Mumbai", "10/11/1988", "B+", 175, 80);      
        // doctor2.AddDoctor();
        // patient2.AddPatient();
        patient2.viewPatient();
        // ArrayList<Doctor> doctors = new ArrayList<>();
        Session session = new Session(doctor1,patient1);

        Prescription prescription = new Prescription();

        session.addPrescription(prescription);

    }
}
class User{
    protected String name;
    protected String email;
    protected String password;
    protected String phone;
    protected String address;
    protected String DOB;
    User(String name,String email, String password,String phone, String address,String DOB){
        this.name=name;
        this.email=email;
        this.password=password;
        this.phone=phone;
        this.address=address;
        this.DOB=DOB;
        }
    public String getName() {
        return name;
    }
    public String getEmail(){
        return email;
    }
    public String hashString(String input, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] hashBytes = md.digest(input.getBytes());

            // Convert byte array to hexadecimal string representation
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}
class Hospital{
    public String name;
    public String address;
    public String phone;
    public String email;

    Hospital(String name, String address,String phone, String email){
        this.name=name;
        this.address=address;
        this.phone=phone;
        this.email = email;
    }
}
class Patient extends User{
    protected String bloodGroup;
    protected int height;
    protected int weight;

    Patient (String name,String email, String password,String phone, String address,String DOB, String bloodGroup,int height, int weight){
        super(name,email,password,phone,address,DOB);
        this.bloodGroup=bloodGroup;
        this.height=height;
        this.weight = weight;
    }
    void viewPatient(){
        MongoCursor mongocursor = new Mongodb().getCursor("patients",this.name);
        if(mongocursor.hasNext()){
            Document doc = (Document) mongocursor.next();
            System.out.println("Name: " + doc.getString("name"));
            System.out.println("Email: " + doc.getString("email"));
            System.out.println("Phone: " + doc.getString("phone"));
            System.out.println("Address: " + doc.getString("address"));
            System.out.println("DOB: " + doc.getString("DOB"));
            System.out.println("Blood Group: " + doc.getString("bloodgroup"));
            System.out.println("Height: " + doc.getInteger("height"));
            System.out.println("Weight: " + doc.getInteger("weight"));
        } else {
            System.out.println("Patient not found.");
        }




    }


    // public void viewPatient(String username,String password){
        
    //     String hashPass = hashString(password, "sha256");
    //     collection.findOne(username);

    //     // view patient details
    // }

}

class Medicine{
    public String name;
    public float dosage;
    public float frequency;
    Medicine(String name,float dosage,float frequency){
        this.name=name;
        this.dosage=dosage;
        this.frequency=frequency;
    }

}
class Prescription{
    protected ArrayList<Medicine> medicines;
    Prescription(){
        this.medicines=new ArrayList<Medicine>();
    }
    void addMedicine(Medicine medicine){
        this.medicines.add(medicine);
    }
}


class Session {
    public Doctor doctor;
    public Patient patient;
    public TimeStamp timestamp;
    public Prescription prescription;

    Session(Doctor doctor, Patient patient){
        this.doctor=doctor;
        this.patient=patient;
        this.timestamp=new TimeStamp();
    }

    void addPrescription(Prescription prescription){
        this.prescription=prescription;
    }

}
class Doctor extends User{
    public Hospital hospital;
    public String specialization;
    protected ArrayList <Patient> patients;

    Doctor(String name,String email, String password,String phone, String address,String DOB,Hospital hospital,String specialization){
        super(name,email,password,phone,address,DOB);
        this.hospital=hospital;
        this.specialization=specialization;
        this.patients=new ArrayList<Patient>();
    }
    public void AddDoctor(){
        MongoClient mc = new Mongodb().getMongoClient();
		MongoDatabase db = mc.getDatabase("mongodbjava");
		MongoCollection<org.bson.Document> collection = db.getCollection("doctors");
		org.bson.Document doc = new org.bson.Document();
        String hashPass = hashString(this.password, "sha256");
        doc.append("name", this.name);
        doc.append("email", this.email);
        doc.append("password", hashPass);
        doc.append("phone", this.phone);
        doc.append("address", this.address);
        doc.append("DOB", this.DOB);
        doc.append("hospital", this.hospital.name);
        doc.append("specialization",this.specialization);
        doc.append("patients", this.patients);
        collection.insertOne(doc);
	}
    protected void AddPatient(Patient patient){
        MongoClient mc = new Mongodb().getMongoClient();
		MongoDatabase db = mc.getDatabase("mongodbjava");
		MongoCollection<org.bson.Document> collection = db.getCollection("patients");
		org.bson.Document doc = new org.bson.Document();
        String hashPass = hashString(patient.password, "sha256");
        doc.append("name", patient.name);
        doc.append("email", patient.email);
        doc.append("password", hashPass);
        doc.append("phone", patient.phone);
        doc.append("address", patient.address);
        doc.append("DOB", patient.DOB);
        doc.append("bloodgroup",patient.bloodGroup);
        doc.append("height",patient.height);
        doc.append("weight",patient.weight);
        collection.insertOne(doc);
        // add patient to the database
    }
    public void 

}

class TimeStamp{
    public int day;
    public int month;
    public int year;
    public int hour;
    public int minute;

    TimeStamp(){
        this.day=LocalDate.now().getDayOfMonth();
        this.month=LocalDate.now().getMonthValue();
        this.year=LocalDate.now().getYear();
        this.hour=LocalTime.now().getHour();
        this.minute=LocalTime.now().getMinute();
    }
    String getTime(){
        return hour+":"+minute;
    }
    String getDate(){
        return day+"/"+month+"/"+year;
    }
}
class Report {
    protected String type;
    protected TimeStamp time;
    protected Hospital hospital;
    protected Patient patient;
    protected Doctor doctor;

    Report(String type, TimeStamp time, Hospital hospital, Patient patient, Doctor doctor) {
        this.type = type;
        this.time = time;
        this.hospital = hospital;
        this.patient = patient;
        this.doctor = doctor;
    }
}

class BloodTest extends Report {
    String bloodGroup;
    float hb; // Hemoglobin
    float rbc; // Red Blood Cell count
    float wbc; // White Blood Cell count
    float plateletCount;
    float hemoglobin;
    float glucose;
    float totalCholesterol;

    BloodTest(String type, TimeStamp time, Hospital hospital, Patient patient, Doctor doctor,
              String bloodGroup, float hb, float rbc, float wbc, float plateletCount,
              float hemoglobin, float glucose, float totalCholesterol) {
        super(type, time, hospital, patient, doctor);
        this.bloodGroup = bloodGroup;
        this.hb = hb;
        this.rbc = rbc;
        this.wbc = wbc;
        this.plateletCount = plateletCount;
        this.hemoglobin = hemoglobin;
        this.glucose = glucose;
        this.totalCholesterol = totalCholesterol;
    }

    void display() {
        // Implement display logic for BloodTest report
    }
}

class UrineTest extends Report {
    String color;
    String ph;
    String bacteria;
    String protein;

    UrineTest(String type, TimeStamp time, Hospital hospital, Patient patient, Doctor doctor,
              String color, String ph, String bacteria, String protein) {
        super(type, time, hospital, patient, doctor);
        this.color = color;
        this.ph = ph;
        this.bacteria = bacteria;
        this.protein = protein;
    }

    void display() {
        // Implement display logic for UrineTest report
    }
}

class GeneralTest extends Report {
    float pulse;
    float bloodPressure;
    float temperature;
    float respiration;
    String other;

    GeneralTest(String type, TimeStamp time, Hospital hospital, Patient patient, Doctor doctor,
                float pulse, float bloodPressure, float temperature, float respiration, String other) {
        super(type, time, hospital, patient, doctor);
        this.pulse = pulse;
        this.bloodPressure = bloodPressure;
        this.temperature = temperature;
        this.respiration = respiration;
        this.other = other;
    }

    void display() {
        // Implement display logic for GeneralTest report
    }
}

class DentalReport extends Report {
    String missingTeeth;
    String decayedTeeth;
    String rootIssues;

    DentalReport(String type, TimeStamp time, Hospital hospital, Patient patient, Doctor doctor,
                 String missingTeeth, String decayedTeeth, String rootIssues) {
        super(type, time, hospital, patient, doctor);
        this.missingTeeth = missingTeeth;
        this.decayedTeeth = decayedTeeth;
        this.rootIssues = rootIssues;
    }

    void display() {
        // Implement display logic for DentalReport
    }
}

class OpticalReport extends Report {
    String colorVision;
    String lightEyePower;
    String lightEyeCondition;

    OpticalReport(String type, TimeStamp time, Hospital hospital, Patient patient, Doctor doctor,
                  String colorVision, String lightEyePower, String lightEyeCondition) {
        super(type, time, hospital, patient, doctor);
        this.colorVision = colorVision;
        this.lightEyePower = lightEyePower;
        this.lightEyeCondition = lightEyeCondition;
    }

    void display() {
        // Implement display logic for OpticalReport
    }
}


// Assuming Hospital, Patient, and Doctor classes are already defined