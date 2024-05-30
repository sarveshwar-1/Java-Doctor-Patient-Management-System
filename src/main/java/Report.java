
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
public class Report {
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
    void save(String patientName){
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase("mongodbjava");
            
            MongoCollection<Document> bloodTestCollection = database.getCollection("blood_tests");
            Document bloodTestDoc = new Document("type", this.type)
                    .append("time", this.time.getDateTime())
                    .append("hospital", this.hospital.getName())
                    .append("patient", this.patient.getName())
                    .append("doctor", this.doctor.getName())
                    .append("bloodGroup", this.bloodGroup)
                    .append("hb", this.hb)
                    .append("rbc", this.rbc)
                    .append("wbc", this.wbc)
                    .append("plateletCount", this.plateletCount)
                    .append("hemoglobin", this.hemoglobin)
                    .append("glucose", this.glucose)
                    .append("totalCholesterol", this.totalCholesterol);
            bloodTestCollection.insertOne(bloodTestDoc);
        }
        catch(Exception e){
            System.out.println("Error in saving BloodTest report: "+e.getMessage());
    }
    }
    void display() {
            // Implement display logic for BloodTest report
            System.out.println("Blood Test Report");
            System.out.println("Type: " + type);
            System.out.println("Time: " + time.getDateTime());
            System.out.println("Hospital: " + hospital.getName());
            System.out.println("Patient: " + patient.getName());
            System.out.println("Doctor: " + doctor.getName());
            System.out.println("Blood Group: " + bloodGroup);
            System.out.println("Hemoglobin: " + hb);
            System.out.println("Red Blood Cell count: " + rbc);
            System.out.println("White Blood Cell count: " + wbc);
            System.out.println("Platelet Count: " + plateletCount);
            System.out.println("Glucose: " + glucose);
            System.out.println("Total Cholesterol: " + totalCholesterol);
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
            System.out.println("Urine Test Report");
            System.out.println("Type: " + type);
            System.out.println("Time: " + time.getDateTime());
            System.out.println("Hospital: " + hospital.getName());
            System.out.println("Patient: " + patient.getName());
            System.out.println("Doctor: " + doctor.getName());
            System.out.println("Color: " + color);
            System.out.println("pH: " + ph);
            System.out.println("Bacteria: " + bacteria);
            System.out.println("Protein: " + protein);
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
            System.out.println("General Test Report");
            System.out.println("Type: " + type);
            System.out.println("Time: " + time.getDateTime());
            System.out.println("Hospital: " + hospital.getName());
            System.out.println("Patient: " + patient.getName());
            System.out.println("Doctor: " + doctor.getName());
            System.out.println("Pulse: " + pulse);
            System.out.println("Blood Pressure: " + bloodPressure);
            System.out.println("Temperature: " + temperature);
            System.out.println("Respiration: " + respiration);
            System.out.println("Other: " + other);
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
            System.out.println("Dental Report");
            System.out.println("Type: " + type);
            System.out.println("Time: " + time.getDateTime());
            System.out.println("Hospital: " + hospital.getName());
            System.out.println("Patient: " + patient.getName());
            System.out.println("Doctor: " + doctor.getName());
            System.out.println("Missing Teeth: " + missingTeeth);
            System.out.println("Decayed Teeth: " + decayedTeeth);
            System.out.println("Root Issues: " + rootIssues);
            // Implement display logic for DentalReport
        }
        // Implement display logic for DentalReport
        
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
        System.out.println("Optical Report");
        System.out.println("Type: " + type);
        System.out.println("Time: " + time.getDateTime());
        System.out.println("Hospital: " + hospital.getName());
        System.out.println("Patient: " + patient.getName());
        System.out.println("Doctor: " + doctor.getName());
        System.out.println("Color Vision: " + colorVision);
        System.out.println("Light Eye Power: " + lightEyePower);
        System.out.println("Light Eye Condition: " + lightEyeCondition);
        // Implement display logic for OpticalReport
    }
}


