package MiniProject;

import java.util.*;

// --------------------- Edge Class ---------------------
class Edge {
    String dest;
    int distance;

    Edge(String dest, int distance) {
        this.dest = dest;
        this.distance = distance;
    }
}

// --------------------- Graph Class ---------------------
class Graph {

    HashMap<String, List<Edge>> adj = new HashMap<>();

    // Add hospital node
    void addHospital(String name) {
        adj.putIfAbsent(name, new ArrayList<>());
    }

    // Add connection (bidirectional)
    void addConnection(String src, String dest, int distance) {
        adj.putIfAbsent(src, new ArrayList<>());
        adj.putIfAbsent(dest, new ArrayList<>());

        adj.get(src).add(new Edge(dest, distance));
        adj.get(dest).add(new Edge(src, distance));
    }

    // Result holder
    static class HospitalSelection {
        hospital hospitalObj;
        String doctorName;
        int distance;

        HospitalSelection(hospital h, String doc, int dist) {
            hospitalObj = h;
            doctorName = doc;
            distance = dist;
        }
    }

    // --------------------- Dijkstra ---------------------
    HospitalSelection findNearestDoctor(
        String src,
        String neededSpecialist,
        Doctor[] doctors,
        HashMap<String, String> doctorHospitalMap,
        hospital[] hospitals
    ) {

        // Step 1: Find hospitals having required specialist
        HashMap<String, String> specialistAt = new HashMap<>();

        for (Doctor d : doctors) {
            if (d.specialization.equalsIgnoreCase(neededSpecialist)) {
                String hosp = doctorHospitalMap.get(d.name);
                if (hosp != null) {
                    specialistAt.put(hosp, d.name);
                }
            }
        }

        if (specialistAt.isEmpty()) {
            System.out.println("No doctor found for: " + neededSpecialist);
            return null;
        }

        // Step 2: Initialize distances
        HashMap<String, Integer> dist = new HashMap<>();
        for (String h : adj.keySet()) {
            dist.put(h, Integer.MAX_VALUE);
        }

        dist.put(src, 0);

        List<String> visited = new ArrayList<>();

        // Step 3: Dijkstra loop
        while (visited.size() < dist.size()) {

            String current = null;
            int minDist = Integer.MAX_VALUE;

            for (String h : dist.keySet()) {
                if (!visited.contains(h) && dist.get(h) < minDist) {
                    minDist = dist.get(h);
                    current = h;
                }
            }

            if (current == null) break;

            // If specialist found here → return
            if (specialistAt.containsKey(current)) {

                String doctor = specialistAt.get(current);
                hospital selectedHosp = null;

                for (hospital h : hospitals) {
                    if (h.name.equals(current)) {
                        selectedHosp = h;
                        break;
                    }
                }

                if (selectedHosp != null) {
                    return new HospitalSelection(selectedHosp, doctor, minDist);
                }
            }

            visited.add(current);

            // Relax edges
            List<Edge> neighbors = adj.get(current);

            if (neighbors != null) {
                for (Edge e : neighbors) {
                    if (!visited.contains(e.dest)) {
                        int newDist = dist.get(current) + e.distance;

                        if (newDist < dist.get(e.dest)) {
                            dist.put(e.dest, newDist);
                        }
                    }
                }
            }
        }

        System.out.println("No reachable specialist found.");
        return null;
    }
}
