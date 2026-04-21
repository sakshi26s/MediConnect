package MiniProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DiagnosisAppFrame extends JFrame {

    private JTextField txtName, txtAge, txtAddress;
    private JButton btnStartDiagnosis, btnNextQuestion, btnServeNext;
    private JLabel lblQuestion;
    private JRadioButton rbYes, rbNo;
    private ButtonGroup bgYesNo;

    private DefaultListModel<String> queueListModel;
    private JList<String> queueList;

    private JTextArea txtReport;

    private Patient_Priority queue = new Patient_Priority();
    private Patient currentPatient;
    private Patient.Node ptrNode;

    private hospital[] hospitals;
    private Doctor[] doctors;
    private HashMap<String, String> doctorHospitalMap;
    private Graph graph;

    public DiagnosisAppFrame(hospital[] hospitals, Doctor[] doctors,
                             HashMap<String, String> doctorHospitalMap, Graph graph) {

        this.hospitals = hospitals;
        this.doctors = doctors;
        this.doctorHospitalMap = doctorHospitalMap;
        this.graph = graph;

        setTitle("MediConnect");
        setSize(850, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        setVisible(true);
    }

    private void initUI() {

        setLayout(new BorderLayout());

        // -------- LEFT PANEL (INPUT + QUESTIONS) --------
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        txtName = new JTextField(15);
        txtAge = new JTextField(5);
        txtAddress = new JTextField(15);

        btnStartDiagnosis = new JButton("Start Diagnosis");

        left.add(new JLabel("Name"));
        left.add(txtName);
        left.add(new JLabel("Age"));
        left.add(txtAge);
        left.add(new JLabel("Address"));
        left.add(txtAddress);
        left.add(btnStartDiagnosis);

        lblQuestion = new JLabel(" ");
        rbYes = new JRadioButton("Yes");
        rbNo = new JRadioButton("No");

        bgYesNo = new ButtonGroup();
        bgYesNo.add(rbYes);
        bgYesNo.add(rbNo);

        btnNextQuestion = new JButton("Next");
        btnNextQuestion.setEnabled(false);

        left.add(lblQuestion);
        left.add(rbYes);
        left.add(rbNo);
        left.add(btnNextQuestion);

        add(left, BorderLayout.WEST);

        // -------- RIGHT PANEL (QUEUE + REPORT) --------
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));

        queueListModel = new DefaultListModel<>();
        queueList = new JList<>(queueListModel);

        btnServeNext = new JButton("Serve Next Patient");

        txtReport = new JTextArea(10, 20);
        txtReport.setEditable(false);

        right.add(new JLabel("Queue"));
        right.add(new JScrollPane(queueList));
        right.add(btnServeNext);
        right.add(new JScrollPane(txtReport));

        add(right, BorderLayout.CENTER);

        // -------- BUTTON ACTIONS --------

        btnStartDiagnosis.addActionListener(e -> {

            String name = txtName.getText();
            int age = Integer.parseInt(txtAge.getText());
            String addr = txtAddress.getText();

            currentPatient = new Patient(name, age, addr);
            currentPatient.diseaseMapping();

            ptrNode = currentPatient.getQuestionRoot();

            lblQuestion.setText(ptrNode.data);
            btnNextQuestion.setEnabled(true);
        });

        btnNextQuestion.addActionListener(e -> {

            if (!rbYes.isSelected() && !rbNo.isSelected()) {
                JOptionPane.showMessageDialog(this, "Select Yes/No");
                return;
            }

            ptrNode = rbYes.isSelected() ? ptrNode.right : ptrNode.left;
            bgYesNo.clearSelection();

            if (ptrNode.left != null) {
                lblQuestion.setText(ptrNode.data);
            } else {
                currentPatient.disease = ptrNode.data;
                currentPatient.urgency_rating =
                        currentPatient.getUrgency(ptrNode.data);
                currentPatient.Specialist_required =
                        currentPatient.getSpecialist(ptrNode.data);

                queue.Enqueue_Patient(currentPatient);
                refreshQueue();

                JOptionPane.showMessageDialog(this,
                        "Disease: " + currentPatient.disease +
                        "\nUrgency: " + currentPatient.urgency_rating);

                btnNextQuestion.setEnabled(false);
            }
        });

        btnServeNext.addActionListener(e -> {

            Patient p = queue.Deque_Patient();
            if (p == null) return;

            refreshQueue();

            String needed = p.Specialist_required;

            Graph.HospitalSelection result =
                    graph.findNearestDoctor(hospitals[0].name,
                            needed, doctors, doctorHospitalMap, hospitals);

            if (result != null) {
                txtReport.setText(
                        "Patient: " + p.name +
                        "\nDisease: " + p.disease +
                        "\nDoctor: " + result.doctorName +
                        "\nHospital: " + result.hospitalObj.name +
                        "\nDistance: " + result.distance + " km"
                );
            }
        });
    }

    private void refreshQueue() {

        queueListModel.clear();

        Patient ptr = queue.front;
        while (ptr != null) {
            queueListModel.addElement(ptr.name + " | " +
                    ptr.disease + " | " + ptr.urgency_rating);
            ptr = ptr.link;
        }
    }
}
