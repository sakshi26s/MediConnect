package MiniProject;

import java.util.*;

// -------------------- Patient Class --------------------
class Patient {

    String name;
    int age;
    String disease;
    int urgency_rating;
    String Specialist_required;
    Patient link;
    String address;
    double lat, lon;

    HashMap<String, Integer> disease_urgencyRating = new HashMap<>();
    HashMap<String, String> disease_SpecialistType = new HashMap<>();

    Patient() {
        link = null;
    }

    Patient(String name, int age, String address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    // ---------------- Decision Tree Node ----------------
    class Node {
        String data;
        Node left, right;

        Node(String data) {
            this.data = data;
        }
    }

    // Assign diseases to leaf nodes
    private void assignLeaves(Node node, String[] leaves, int[] idx) {
        if (node == null) return;

        if (node.left == null && node.right == null) {
            node.data = leaves[idx[0]++];
            return;
        }

        assignLeaves(node.left, leaves, idx);
        assignLeaves(node.right, leaves, idx);
    }

    // ---------------- Disease Mapping ----------------
    void diseaseMapping() {

        disease_urgencyRating.put("Healthy", 1);
        disease_SpecialistType.put("Healthy", "General Physician");

        disease_urgencyRating.put("Migraine mild", 2);
        disease_SpecialistType.put("Migraine mild", "Neurologist");

        disease_urgencyRating.put("Tension headache", 2);
        disease_SpecialistType.put("Tension headache", "Neurologist");

        disease_urgencyRating.put("Migraine severe", 4);
        disease_SpecialistType.put("Migraine severe", "Neurologist");

        disease_urgencyRating.put("Food poisoning mild", 3);
        disease_SpecialistType.put("Food poisoning mild", "General Physician");

        disease_urgencyRating.put("Gastroenteritis", 4);
        disease_SpecialistType.put("Gastroenteritis", "Gastroenterologist");

        disease_urgencyRating.put("Flu", 3);
        disease_SpecialistType.put("Flu", "General Physician");

        disease_urgencyRating.put("Pneumonia/COVID", 7);
        disease_SpecialistType.put("Pneumonia/COVID", "Pulmonologist");

        disease_urgencyRating.put("Mild chest strain", 2);
        disease_SpecialistType.put("Mild chest strain", "Orthopedist");

        disease_urgencyRating.put("Pleurisy", 5);
        disease_SpecialistType.put("Pleurisy", "Pulmonologist");

        disease_urgencyRating.put("Angina", 8);
        disease_SpecialistType.put("Angina", "Cardiologist");

        disease_urgencyRating.put("Heart Attack", 10);
        disease_SpecialistType.put("Heart Attack", "Cardiologist");

        disease_urgencyRating.put("Appendicitis", 9);
        disease_SpecialistType.put("Appendicitis", "Surgeon");

        disease_urgencyRating.put("Allergy/Rash", 3);
        disease_SpecialistType.put("Allergy/Rash", "Dermatologist");

        disease_urgencyRating.put("Sepsis/Multi-organ failure", 12);
        disease_SpecialistType.put("Sepsis/Multi-organ failure", "Intensivist");

        disease_urgencyRating.put("Severe trauma/Emergency", 15);
        disease_SpecialistType.put("Severe trauma/Emergency", "Trauma Surgeon");
    }

    int getUrgency(String disease) {
        return disease_urgencyRating.getOrDefault(disease, 0);
    }

    String getSpecialist(String disease) {
        return disease_SpecialistType.getOrDefault(disease, "General Physician");
    }

    // ---------------- Diagnosis ----------------
    String Patient_Disease_Diagnosis() {

        Node root = getQuestionRoot();
        Node ptr = root;

        Scanner sc = new Scanner(System.in);
        int ans = 0;

        while (ptr.left != null && ptr.right != null) {

            System.out.println(ptr.data + " (1 = yes, 0 = no)");

            try {
                ans = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Enter 0 or 1");
                sc.next();
                continue;
            }

            ptr = (ans == 1) ? ptr.right : ptr.left;
        }

        this.disease = ptr.data;
        this.urgency_rating = getUrgency(disease);
        this.Specialist_required = getSpecialist(disease);

        return disease;
    }

    // ---------------- Tree Builder ----------------
    public Node getQuestionRoot() {

        Node root = new Node("Do you have chest pain?");
        root.left = new Node("Do you have fever?");
        root.right = new Node("Do you have shortness of breath?");

        root.left.left = new Node("Do you have headache?");
        root.left.right = new Node("Do you have nausea?");

        root.right.left = new Node("Do you feel dizzy?");
        root.right.right = new Node("Do you have abdominal pain?");

        root.left.left.left = new Node("");
        root.left.left.right = new Node("");
        root.left.right.left = new Node("");
        root.left.right.right = new Node("");
        root.right.left.left = new Node("");
        root.right.left.right = new Node("");
        root.right.right.left = new Node("");
        root.right.right.right = new Node("");

        String[] leaves = {
            "Healthy", "Migraine mild", "Tension headache", "Migraine severe",
            "Food poisoning mild", "Gastroenteritis", "Flu", "Pneumonia/COVID"
        };

        int[] idx = {0};
        assignLeaves(root, leaves, idx);

        return root;
    }
}
