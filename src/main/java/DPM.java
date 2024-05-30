import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import java.util.logging.Level;
import java.util.logging.Logger;

class Choice{
    int choice;
    String user;
    Choice(int choice, String user){
        this.choice = choice;
        this.user = user;
    }

}
    public class DPM {
        private static final Logger MONGO_LOGGER = Logger.getLogger("org.mongodb.driver");
        static {
            MONGO_LOGGER.setLevel(Level.SEVERE); // or Level.WARNING, etc.
        }

        public static Scanner scanner = new Scanner(System.in);

        
        
        public static void main(String[] args) {
            ensureAdminExists(); 
            Choice ch = Login();
            
            if (ch.choice != 0) {
                Activities(ch);
            }
        }
    
        public static String hashPassword(String password) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(password.getBytes());
                byte[] digest = md.digest();
                StringBuilder sb = new StringBuilder();
                for (byte b : digest) {
                    sb.append(String.format("%02x", b));
                }
                return sb.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return null;
            }
    }

    static void ensureAdminExists() {
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase("mongodbjava");
            MongoCollection<Document> adminsCollection = database.getCollection("admins");
            
            long adminCount = adminsCollection.countDocuments();
            
            if (adminCount == 0) {
                System.out.println("No admin found. Creating an admin account.");
                createAdmin();
            }
        }
    }

    static void createAdmin() {
        
        System.out.println("Enter admin details:");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        password = DPM.hashPassword(password);
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();
        System.out.print("DOB (dd/MM/yyyy): ");
        String dob = scanner.nextLine();

        Admin admin = new Admin(name, email, password, phone, address, dob);
        admin.AddAdmin();
        System.out.println("Admin created successfully.");
    }

    static void Activities(Choice ch) {
        int User_ch = ch.choice;
        String user = ch.user;
        if (User_ch == 1) { // Doctor
            while (true) {
                Doctor doctor = Doctor.getDoctorByEmail(user);
                System.out.println("1. Add patient");
                System.out.println("2. Create a session for diagnosis");
                System.out.println("3. View patient");
                System.out.println("4. Add report");
                System.out.println("5. Exit");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                switch (choice) {
                    case 1:
                        System.out.println("Enter patient details:");
                        System.out.print("Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Email: ");
                        String email = scanner.nextLine();
                        System.out.print("Password: ");
                        String password = scanner.nextLine();
                        password = hashPassword(password);
                        System.out.print("Phone: ");
                        String phone = scanner.nextLine();
                        System.out.print("Address: ");
                        String address = scanner.nextLine();
                        System.out.print("DOB (dd/MM/yyyy): ");
                        String dob = scanner.nextLine();
                        System.out.print("Blood Group: ");
                        String bloodGroup = scanner.nextLine();
                        System.out.print("Height: ");
                        int height = scanner.nextInt();
                        System.out.print("Weight: ");
                        int weight = scanner.nextInt();
                        Patient patient = new Patient(name, email, password, phone, address, dob, bloodGroup, height, weight);
                        doctor.AddPatient(patient);
                        
                        System.out.println("Patient added successfully.");
                        break;
                    case 2:
                        // Create session functionality
                        System.out.println("Enter patient name for diagnosis session:");
                        String patientName = scanner.nextLine();
                        // Fetch the patient from the database using patientName
                        Patient sessionPatient = Patient.getPatientByName(patientName); 
                        Doctor.CreateSession(doctor,sessionPatient);
                        System.out.println("Session created successfully.");
                        break;
                    case 3:
                        System.out.println("Enter patient name to view details:");
                        String viewPatientName = scanner.nextLine();
                        Patient viewPatient = Patient.getPatientByName(viewPatientName); 
                        viewPatient.viewPatient();
                        break;
                    case 4:
                        System.out.println("Enter patient name for report:");
                        String patientNameReport = scanner.nextLine();
                        Patient reportPatient = Patient.getPatientByName(patientNameReport); 
                        doctor.AddReports(reportPatient);
                    case 5:
                        // Exit
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        } else if (User_ch == 2) { // Patient
            while (true) {
                System.out.println("1. View doctors");
                System.out.println("2. View reports for each session");
                System.out.println("3. Exit");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                switch (choice) {
                    case 1:
                        // View doctors functionality
                        System.out.println("Doctors list:");
                        viewDoctors();
                        
                        break;
                    case 2:
                        // View reports functionality
                        System.out.println("Enter your name to view reports:");
                        String patientName = scanner.nextLine();
                        viewReports(patientName);
                        // Fetch and display reports from the database
                        break;
                    case 3:
                        // Exit
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        } else if (User_ch == 3) { 
            while (true) {
                System.out.println("1. Add doctor");
                System.out.println("2. Add hospital");
                System.out.println("3. View doctors");
                System.out.println("4. Exit");
                int choice = scanner.nextInt();
                scanner.nextLine();
                  
                switch (choice) {
                    case 1:
                        System.out.println("Enter doctor details:");
                        System.out.println("Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Email: ");
                        String email = scanner.nextLine();
                        System.out.print("Password: ");
                        String password = scanner.nextLine();
                        password = DPM.hashPassword(password);
                        System.out.print("Phone: ");
                        String phone = scanner.nextLine();
                        System.out.print("Address: ");
                        String address = scanner.nextLine();
                        System.out.print("DOB (dd/MM/yyyy): ");
                        String dob = scanner.nextLine();
                        System.out.print("Hospital Name: ");
                        String hospitalname = scanner.nextLine();
                        System.out.print("Specialization: ");
                        String specialization = scanner.nextLine();
                        Hospital hospital = Hospital.getHospitalByName(hospitalname); 
                        Doctor doctor = new Doctor(name, email, password, phone, address, dob, hospital, specialization);
                        doctor.AddDoctor();
                        System.out.println("Doctor added successfully.");
                        break;
                    case 2:
                        System.out.println("Enter hospital details:");
                        System.out.print("Name: ");
                        String hospitalName = scanner.nextLine();
                        System.out.print("Address: ");
                        String hospitalAddress = scanner.nextLine();
                        Hospital HOSPITAL = new Hospital(hospitalName, hospitalAddress);
                        HOSPITAL.AddHospital();
                        System.out.println("Hospital added successfully.");
                    case 3:
                        System.out.println("Doctors list:");
                        viewDoctors();
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        }
    }

    static void viewDoctors() {
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase("mongodbjava");
            MongoCollection<Document> doctorsCollection = database.getCollection("doctors");
            FindIterable<Document> doctors = doctorsCollection.find();
            System.out.println("List of Doctors:");
            for (Document doc : doctors) {
                String name = doc.getString("name");
                String email = doc.getString("email");
                String specialization = doc.getString("specialization");
                System.out.printf("Name: %s, Email: %s, Specialization: %s%n", name, email, specialization);
            }
        }
    }
    
    static void viewReports(String patientName) {
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase("mongodbjava");
            MongoCollection<Document> sessionsCollection = database.getCollection("diagnosis_sessions");

            Bson filter = Filters.eq("patient", patientName);
            FindIterable<Document> sessions = sessionsCollection.find(filter);
            System.out.println("Reports for " + patientName + ":");
            for (Document session : sessions) {
                String sessionId = session.getString("sessionId");
                String doctor = session.getString("doctor");
                String symptoms = session.getString("symptoms");
                String diagnosis = session.getString("diagnosis");
                String prescriptions = session.getString("prescriptions");
                String notes = session.getString("notes");
                System.out.printf("Session ID: %s, Doctor: %s, Symptoms: %s, Diagnosis: %s, Prescriptions: %s, Notes: %s%n",
                        sessionId, doctor, symptoms, diagnosis, prescriptions, notes);
            }
        }
    }

    static Choice Login() {
        try {
            System.out.print("Enter your email: ");
            String email = scanner.nextLine();
            System.out.print("Enter your password: ");
            String password = scanner.nextLine();
            Authentication auth = new Authentication();
            Choice user = auth.login(email, password);
            int userNum = user.choice;
            if (userNum != 0) {
                System.out.println("Login successful!");
                // scanner.close();
            } else {
                System.out.println("Invalid credentials.");
                // scanner.close();

            }
            return user;
        } catch (Exception e) {
            System.out.println("An error occurred during login: " + e.getMessage());
            return new Choice(0, "none");
        }
    }

    public static Scanner getScanner() {
        return scanner;
    }

    public static void setScanner(Scanner scanner) {
        DPM.scanner = scanner;
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
String getDateTime(){
    return getDate()+" "+getTime();
}
}