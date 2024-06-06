
import java.util.ArrayList;
import java.util.Scanner;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class Doctor extends User {
    private ArrayList<String> patients;
    private Hospital hospital;
    private String specialization;
    public Schedule schedule;

    public Doctor(String name, String email, String password, String phone, String address, String dob,
            Hospital hospital, String specialization) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.dob = dob;
        this.hospital = hospital;
        this.specialization = specialization;
        this.patients = new ArrayList<>();
        this.schedule = new Schedule(this.email);
        schedule.saveAppointment();
    }

    public void AddDoctor() {
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase("mongodbjava");
            MongoCollection<Document> doctorsCollection = database.getCollection("doctors");
            Document doctorDoc = new Document("name", this.name)
                    .append("email", this.email)
                    .append("password", this.password)
                    .append("phone", this.phone)
                    .append("address", this.address)
                    .append("dob", this.dob)
                    .append("hospital", this.hospital.getName())
                    .append("specialization", this.specialization);
            doctorsCollection.insertOne(doctorDoc);
        }
    }

    public void AddPatient(Patient patient) {
        this.patients.add(patient.name);
        patient.doctors.add(this.name);
        patient.AddPatient();
    }

    public static Doctor getDoctorByName(String name) {
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase("mongodbjava");
            MongoCollection<Document> doctorsCollection = database.getCollection("doctors");

            Document doc = doctorsCollection.find(Filters.eq("name", name)).first();
            if (doc != null) {
                String email = doc.getString("email");
                String password = doc.getString("password");
                String phone = doc.getString("phone");
                String address = doc.getString("address");
                String dob = doc.getString("dob");
                String hospitalName = doc.getString("hospital");
                String specialization = doc.getString("specialization");
                Hospital hospital = Hospital.getHospitalByName(hospitalName); // Assuming a method to get the hospital
                                                                              // details
                Doctor doctor = new Doctor(name, email, password, phone, address, dob, hospital, specialization);
                return doctor;
            }
            return null;
        } catch (Exception e) {
            System.out.println("Error in getting doctor by email: " + e.getMessage());
            return null;
        }
    }

    public static void CreateSession(Doctor doctor, Patient patient) {
        Scanner scanner = DPM.getScanner();
        System.out.println("Enter the symptoms:");
        String symptoms = scanner.nextLine();
        System.out.println("Enter the diagnosis:");
        String diagnosis = scanner.nextLine();
        System.out.println("Enter the prescriptions:");
        String prescriptions = scanner.nextLine();
        System.out.println("Enter the notes:");
        String notes = scanner.nextLine();
        // scanner.close();

        DiagnosisSession session = new DiagnosisSession(doctor, patient, symptoms, diagnosis, prescriptions, notes);
        session.AddSession();

        // Update patient's sessions
        patient.getSessions().add(session);

        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase("mongodbjava");
            MongoCollection<Document> patientsCollection = database.getCollection("patients");
            Document update = new Document("$push", new Document("sessions", session.getSessionId()));
            patientsCollection.updateOne(Filters.eq("email", patient.getEmail()), update);
        }
    }

    public static Doctor getDoctorByEmail(String email) {
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase("mongodbjava");
            MongoCollection<Document> doctorsCollection = database.getCollection("doctors");

            Document doc = doctorsCollection.find(Filters.eq("email", email)).first();
            if (doc != null) {
                String name = doc.getString("name");
                String password = doc.getString("password");
                String phone = doc.getString("phone");
                String address = doc.getString("address");
                String dob = doc.getString("dob");
                String hospitalName = doc.getString("hospital");
                String specialization = doc.getString("specialization");
                Hospital hospital = Hospital.getHospitalByName(hospitalName); // Assuming a method to get the hospital
                                                                              // details
                Doctor doctor = new Doctor(name, email, password, phone, address, dob, hospital, specialization);
                return doctor;
            }
        }
        return null;
    }

    public void AddReports(Patient patient) {
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase("mongodbjava");
            MongoCollection<Document> patientsCollection = database.getCollection("patients");

            Scanner scanner = DPM.getScanner();
            System.out.println(
                    "Enter the type of report (BloodTest, UrineTest, GeneralTest, DentalReport, OpticalReport):");
            String type = scanner.nextLine();
            TimeStamp time = new TimeStamp();
            String timeStr = time.getDateTime();
            Hospital hospital = this.hospital;
            Doctor doctor = this;
            switch (type) {
                case "BloodTest":
                    System.out.println("Enter the blood group:");
                    String bloodGroup = scanner.nextLine();
                    System.out.println("Enter the Hemoglobin:");
                    float hb = scanner.nextFloat();
                    System.out.println("Enter the Red Blood Cell count:");
                    float rbc = scanner.nextFloat();
                    System.out.println("Enter the White Blood Cell count:");
                    float wbc = scanner.nextFloat();
                    System.out.println("Enter the Platelet Count:");
                    float plateletCount = scanner.nextFloat();
                    System.out.println("Enter the Hemoglobin:");
                    float hemoglobin = scanner.nextFloat();
                    System.out.println("Enter the Glucose:");
                    float glucose = scanner.nextFloat();
                    System.out.println("Enter the Total Cholesterol:");
                    float totalCholesterol = scanner.nextFloat();

                    Document bloodTestDoc = new Document("type", type)
                            .append("time", timeStr)
                            .append("hospital", hospital.getName())
                            .append("patient", patient.getName())
                            .append("doctor", doctor.getName())
                            .append("bloodGroup", bloodGroup)
                            .append("hb", hb)
                            .append("rbc", rbc)
                            .append("wbc", wbc)
                            .append("plateletCount", plateletCount)
                            .append("hemoglobin", hemoglobin)
                            .append("glucose", glucose)
                            .append("totalCholesterol", totalCholesterol);
                    BloodTest bloodTest = new BloodTest(type, time, hospital, patient, doctor, bloodGroup, hb, rbc, wbc,
                            plateletCount, hemoglobin, glucose, totalCholesterol);
                    patient.reports.add(bloodTest);
                    Document update = new Document("$push", new Document("reports", bloodTestDoc));
                    patientsCollection.updateOne(Filters.eq("email", patient.getEmail()), update);
                    bloodTest.display();
                    break;
                case "UrineTest":
                    System.out.println("Enter the color:");
                    String color = scanner.nextLine();
                    System.out.println("Enter the pH:");
                    String ph = scanner.nextLine();
                    System.out.println("Enter the bacteria:");
                    String bacteria = scanner.nextLine();
                    System.out.println("Enter the protein:");
                    String protein = scanner.nextLine();
                    UrineTest urineTest = new UrineTest(type, time, hospital, patient, doctor, color, ph, bacteria,
                            protein);
                    patient.reports.add(urineTest);
                    Document urineTestDoc = new Document("type", type)
                            .append("time", timeStr)
                            .append("hospital", hospital.getName())
                            .append("patient", patient.getName())
                            .append("doctor", doctor.getName())
                            .append("color", color)
                            .append("ph", ph)
                            .append("bacteria", bacteria)
                            .append("protein", protein);
                    Document updateUrine = new Document("$push", new Document("reports", urineTestDoc));
                    patientsCollection.updateOne(Filters.eq("email", patient.getEmail()), updateUrine);

                    urineTest.display();

                    break;
                case "GeneralTest":
                    System.out.println("Enter the pulse:");
                    float pulse = scanner.nextFloat();
                    System.out.println("Enter the blood pressure:");
                    float bloodPressure = scanner.nextFloat();
                    System.out.println("Enter the temperature");
                    float temperature = scanner.nextFloat();
                    System.out.println("Enter the respiration:");
                    float respiration = scanner.nextFloat();
                    System.out.println("Enter any other details:");
                    String other = scanner.nextLine();
                    GeneralTest generalTest = new GeneralTest(type, time, hospital, patient, doctor, pulse,
                            bloodPressure, temperature, respiration, other);
                    patient.reports.add(generalTest);
                    Document generalTestDoc = new Document("type", type)
                            .append("time", timeStr)
                            .append("hospital", hospital.getName())
                            .append("patient", patient.getName())
                            .append("doctor", doctor.getName())
                            .append("pulse", pulse)
                            .append("bloodPressure", bloodPressure)
                            .append("temperature", temperature)
                            .append("respiration", respiration)
                            .append("other", other);
                    Document updateGeneral = new Document("$push", new Document("reports", generalTestDoc));
                    patientsCollection.updateOne(Filters.eq("email", patient.getEmail()), updateGeneral);
                    generalTest.display();
                    break;
                case "DentalReport":
                    System.out.println("Enter the missing teeth:");
                    String missingTeeth = scanner.nextLine();
                    System.out.println("Enter the decayed teeth:");
                    String decayedTeeth = scanner.nextLine();
                    System.out.println("Enter the root issues:");
                    String rootIssues = scanner.nextLine();
                    DentalReport dentalReport = new DentalReport(type, time, hospital, patient, doctor, missingTeeth,
                            decayedTeeth, rootIssues);
                    patient.reports.add(dentalReport);
                    Document dentalReportDoc = new Document("type", type)
                            .append("time", timeStr)
                            .append("hospital", hospital.getName())
                            .append("patient", patient.getName())
                            .append("doctor", doctor.getName())
                            .append("missingTeeth", missingTeeth)
                            .append("decayedTeeth", decayedTeeth)
                            .append("rootIssues", rootIssues);
                    Document updateDental = new Document("$push", new Document("reports", dentalReportDoc));
                    patientsCollection.updateOne(Filters.eq("email", patient.getEmail()), updateDental);
                    dentalReport.display();

                    break;
                case "OpticalReport":
                    System.out.println("Enter the color vision:");
                    String colorVision = scanner.nextLine();
                    System.out.println("Enter the light eye power:");
                    String lightEyePower = scanner.nextLine();
                    System.out.println("Enter the light eye condition:");
                    String lightEyeCondition = scanner.nextLine();
                    OpticalReport opticalReport = new OpticalReport(type, time, hospital, patient, doctor, colorVision,
                            lightEyePower, lightEyeCondition);
                    patient.reports.add(opticalReport);
                    Document opticalReportDoc = new Document("type", type)
                            .append("time", timeStr)
                            .append("hospital", hospital.getName())
                            .append("patient", patient.getName())
                            .append("doctor", doctor.getName())
                            .append("colorVision", colorVision)
                            .append("lightEyePower", lightEyePower)
                            .append("lightEyeCondition", lightEyeCondition);
                    Document updateOptical = new Document("$push", new Document("reports", opticalReportDoc));
                    patientsCollection.updateOne(Filters.eq("email", patient.getEmail()), updateOptical);
                    opticalReport.display();
                    break;
                default:
                    System.out.println("Invalid report type.");
            }

        } catch (Exception e) {
            System.out.println("Error in adding reports: " + e.getMessage());
        }
    }

    // Additional getters and setters for doctor-specific fields
    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public String getSpecialization() {
        return this.specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
