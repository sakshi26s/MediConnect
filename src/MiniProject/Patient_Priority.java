package MiniProject;

// -------------------- Patient Priority Queue --------------------
class Patient_Priority {

    Patient front, rear;

    Patient_Priority() {
        front = rear = null;
    }

    // Insert patient based on urgency (higher first)
    void Enqueue_Patient(Patient new_Patient) {

        if (front == null) {
            front = rear = new_Patient;
            return;
        }

        // Highest priority → insert at front
        if (new_Patient.urgency_rating > front.urgency_rating) {
            new_Patient.link = front;
            front = new_Patient;
            return;
        }

        // Lowest priority → insert at end
        if (new_Patient.urgency_rating <= rear.urgency_rating) {
            rear.link = new_Patient;
            rear = new_Patient;
            return;
        }

        // Insert in middle
        Patient ptr = front, prev = null;

        while (ptr != null && ptr.urgency_rating >= new_Patient.urgency_rating) {
            prev = ptr;
            ptr = ptr.link;
        }

        prev.link = new_Patient;
        new_Patient.link = ptr;
    }

    // Remove highest priority patient
    Patient Deque_Patient() {

        if (front == null) {
            System.out.println("No patients");
            return null;
        }

        Patient temp = front;
        front = front.link;

        return temp;
    }

    // Display queue
    void display_Priority_Queue() {

        if (front == null) {
            System.out.println("No patients");
            return;
        }

        System.out.println("Priority Queue (highest → lowest urgency):");

        Patient ptr = front;

        while (ptr != null) {
            System.out.println(
                "Name: " + ptr.name +
                " | Disease: " + ptr.disease +
                " | Urgency: " + ptr.urgency_rating
            );
            ptr = ptr.link;
        }
    }
}
