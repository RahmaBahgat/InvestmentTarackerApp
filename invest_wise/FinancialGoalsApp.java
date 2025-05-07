package invest_wise;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class FinancialGoalsApp extends styles {
    private JComboBox<String> goalTypeBox;
    private JTextField amountField, deadlineField, progressField;
    private JTextArea goalListArea;
    private ArrayList<Goal> goals = new ArrayList<>();
    private final String FILE_NAME = "invest_wise/goals.txt";

    public FinancialGoalsApp() {
        window();
        setTitle("Invest Wise - Financial Goals");

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        goalTypeBox = new JComboBox<>(new String[]{"Retirement", "Wealth Accumulation"});
        amountField = new JTextField();
        deadlineField = new JTextField();
        progressField = new JTextField();
        JButton saveBtn = new JButton("Save Goal");
        JButton listBtn = new JButton("List Goals");

        formPanel.add(new JLabel("Goal Type:"));
        formPanel.add(goalTypeBox);
        formPanel.add(new JLabel("Target Amount:"));
        formPanel.add(amountField);
        formPanel.add(new JLabel("Deadline (YYYY-MM-DD):"));
        formPanel.add(deadlineField);
        formPanel.add(new JLabel("Current Progress:"));
        formPanel.add(progressField);
        formPanel.add(saveBtn);
        formPanel.add(listBtn);

        // Text Area for listing
        goalListArea = new JTextArea();
        goalListArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(goalListArea);

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Load goals from file at startup
        loadGoalsFromFile();

        // Save Goal Action
        saveBtn.addActionListener(e -> saveGoal());

        // List Goals Action
        listBtn.addActionListener(e -> listGoals());
    }

    private void saveGoal() {
        String type = (String) goalTypeBox.getSelectedItem();
        String amountText = amountField.getText().trim();
        String deadline = deadlineField.getText().trim();
        String progressText = progressField.getText().trim();

        // Validation
        if (amountText.isEmpty() || deadline.isEmpty() || progressText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            double progress = Double.parseDouble(progressText);
            if (amount <= 0 || progress < 0) {
                JOptionPane.showMessageDialog(this, "Target amount must be > 0 and progress ≥ 0.");
                return;
            }

            Goal goal = new Goal(type, amount, deadline, progress);
            goals.add(goal);
            saveGoalToFile(goal);
            JOptionPane.showMessageDialog(this, "Goal saved successfully!");
            amountField.setText("");
            deadlineField.setText("");
            progressField.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values.");
        }
    }

    private void listGoals() {
        if (goals.isEmpty()) {
            goalListArea.setText("No goals found.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Goal g : goals) {
            sb.append(g).append("\n");
        }
        goalListArea.setText(sb.toString());
    }

    private void saveGoalToFile(Goal goal) {
        try (FileWriter fw = new FileWriter(FILE_NAME, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(goal.toCSV());
            bw.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving goal to file.");
        }
    }

    private void loadGoalsFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                Goal goal = Goal.fromCSV(line);
                if (goal != null) {
                    goals.add(goal);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading goals from file.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FinancialGoalsApp().setVisible(true));
    }

    // Inner Goal class
    static class Goal {
        private String goalType;
        private double targetAmount;
        private String deadline;
        private double currentProgress;

        public Goal(String goalType, double targetAmount, String deadline, double currentProgress) {
            this.goalType = goalType;
            this.targetAmount = targetAmount;
            this.deadline = deadline;
            this.currentProgress = currentProgress;
        }

        @Override
        public String toString() {
            return "Type: " + goalType +
                    ", Target: $" + targetAmount +
                    ", Deadline: " + deadline +
                    ", Progress: $" + currentProgress;
        }

        public String toCSV() {
            return goalType + "," + targetAmount + "," + deadline + "," + currentProgress;
        }

        public static Goal fromCSV(String csv) {
            try {
                String[] parts = csv.split(",");
                if (parts.length != 4) return null;
                String type = parts[0];
                double target = Double.parseDouble(parts[1]);
                String deadline = parts[2];
                double progress = Double.parseDouble(parts[3]);
                return new Goal(type, target, deadline, progress);
            } catch (Exception e) {
                return null;
            }
        }
    }
}
