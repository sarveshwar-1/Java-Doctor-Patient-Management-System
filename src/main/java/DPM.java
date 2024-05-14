
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import database.Mongodb;

public class DPM {
    public static void main(String[] args) {
        System.out.println("hello");
        Hospital hospital = new Hospital("Apollo","Chennai","1234567890","apollo@gmail.com");
        Doctor doctor = new Doctor("Dr. Raj","raj@gmail.com","1234567890","1234567890","Chennai","12/12/1990",hospital,"Cardiologist");
        doctor.AddDoctor();
        Patient patient  = new Patient("Rahul","rahul@gmail.com","1234567890","1234567890","Chennai","12/12/1990","A+",180,70);
        patient.AddPatient();
        // ArrayList<Doctor> doctors = new ArrayList<>();
        Session session = new Session(doctor,patient);

        Prescription prescription = new Prescription();
        prescription.addMedicine("Crocin");

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

    void AddPatient(){
        MongoClient mc = new Mongodb().getMongoClient();
		MongoDatabase db = mc.getDatabase("mongodbjava");
		MongoCollection<org.bson.Document> collection = db.getCollection("patients");
		org.bson.Document doc = new org.bson.Document();
        String hashPass = hashString(this.password, "sha256");
        doc.append("name", this.name);
        doc.append("email", this.email);
        doc.append("password", hashPass);
        doc.append("phone", this.phone);
        doc.append("address", this.address);
        doc.append("DOB", this.DOB);
        doc.append("bloodgroup",this.bloodGroup);
        doc.append("height",this.height);
        doc.append("weight",this.weight);
        collection.insertOne(doc);
        // add patient to the database
    }

    // public void viewPatient(String username,String password){
        
    //     String hashPass = hashString(password, "sha256");
    //     collection.findOne(username);

    //     // view patient details
    // }

}


class Prescription{
    protected ArrayList<String> medicines;
    Prescription(){
        this.medicines=new ArrayList<String>();
    }
    void addMedicine(String medicine){
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