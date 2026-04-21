package MiniProject;

import java.util.*;
import java.io.*;
import java.net.*;
import org.json.*;

public class Main {

    public static void main(String[] args) {

        System.out.println("Welcome to MediConnect!!");
        Scanner sc = new Scanner(System.in);

        // ---- Create hospital network (graph) ----
        Graph g = new Graph();

        hospital h1 = new hospital("CityCare", "Bavdhan", "9876543210");
        hospital h2 = new hospital("LifeLine", "Kothrud", "9123456780");
        hospital h3 = new hospital("MediPoint", "Warje", "9988776655");
        hospital h4 = new hospital("GreenLeaf", "Kharadi", "9090909090");
        hospital h5 = new hospital("HopeHospital", "Baner", "9876501234");
        hospital h6 = new hospital("MediTrust", "Hadapsar", "9564789123");

        hospital[] hospitals = {h1, h2, h3, h4, h5, h6};

        try {

            for (hospital h : hospitals) {
                g.addHospital(h.name);
            }

            // Add connections
            g.addConnection("CityCare", "LifeLine", 5);
            g.addConnection("LifeLine", "MediPoint", 7);
            g.addConnection("CityCare", "GreenLeaf", 12);
            g.addConnection("GreenLeaf", "MediPoint", 6);
            g.addConnection("LifeLine", "HopeHospital", 10);
            g.addConnection("HopeHospital", "MediTrust", 4);
            g.addConnection("MediPoint", "MediTrust", 8);

            // ---- Create doctors ----
            Doctor d1 = new Doctor("Dr. Mehta", "Cardiologist");
            Doctor d2 = new Doctor("Dr. Patel", "General Physician");

            Doctor[] doctors = {d1, d2};

            HashMap<String, String> doctorHospitalMap = new HashMap<>();
            doctorHospitalMap.put(d1.name, "CityCare");
            doctorHospitalMap.put(d2.name, "LifeLine");

            Patient_Priority Q = new Patient_Priority();

            System.out.print("Enter patient name: ");
            String name = sc.nextLine();

            System.out.print("Enter patient age: ");
            int age = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter patient Address: ");
            String add = sc.nextLine();

            Patient patient = new Patient(name, age, add);
            patient.diseaseMapping();

            String disease = patient.Patient_Disease_Diagnosis();

            System.out.println("Disease: " + disease);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
