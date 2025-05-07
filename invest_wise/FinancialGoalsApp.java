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
        login_signup StyledPanel = new login_signup();

        // === FORM PANEL ===
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBackground(Color.decode("#f5efe7"));

        Font labelFont = new Font("Segoe UI Emoji", Font.PLAIN, 16);
        Color labelColor = Color.decode("#3e5879");

        JLabel goalLabel = new JLabel("Goal Type:");
        goalLabel.setFont(labelFont);
        goalLabel.setForeground(labelColor);
        formPanel.add(goalLabel);

        goalTypeBox = new JComboBox<>(new String[]{"Retirement", "Wealth Accumulation"});
        goalTypeBox.setPreferredSize(new Dimension(200, 10));
        formPanel.add(goalTypeBox);

        JLabel amountLabel = new JLabel("Target Amount:");
        amountLabel.setFont(labelFont);
        amountLabel.setForeground(labelColor);
        formPanel.add(amountLabel);

        amountField = new JTextField();
        amountField.setPreferredSize(new Dimension(200, 10));
        formPanel.add(amountField);

        JLabel deadlineLabel = new JLabel("Deadline (YYYY-MM-DD):");
        deadlineLabel.setFont(labelFont);
        deadlineLabel.setForeground(labelColor);
        formPanel.add(deadlineLabel);

        deadlineField = new JTextField();
        deadlineField.setPreferredSize(new Dimension(200, 10));
        formPanel.add(deadlineField);

        JLabel progressLabel = new JLabel("Current Progress:");
        progressLabel.setFont(labelFont);
        progressLabel.setForeground(labelColor);
        formPanel.add(progressLabel);

        progressField = new JTextField();
        progressField.setPreferredSize(new Dimension(200, 10));
        formPanel.add(progressField);

        JButton saveBtn = StyledPanel.styledButton("Save Goal");
        JButton listBtn = StyledPanel.styledButton("List Goals");
        formPanel.add(saveBtn);
        formPanel.add(listBtn);

        // === FORM WRAPPER ===
        JPanel formWrapper = new JPanel(new GridBagLayout());
        formWrapper.setBackground(Color.decode("#f5efe7"));
        formWrapper.add(formPanel);

        // === GOAL LIST PANEL ===
        goalListArea = new JTextArea();
        goalListArea.setEditable(false);
        goalListArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        goalListArea.setLineWrap(true);
        goalListArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(goalListArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Saved Goals"));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // === MAIN PANEL ===
        JPanel mainPanel = StyledPanel.createStyledPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(formWrapper, BorderLayout.NORTH);     // Form stays at top
        mainPanel.add(scrollPane, BorderLayout.CENTER);     // Goal list fills the rest

        add(mainPanel, BorderLayout.CENTER); // Add main panel to window

        // Load + Button Events
        loadGoalsFromFile();
        saveBtn.addActionListener(e -> saveGoal());
        listBtn.addActionListener(e -> listGoals());
    }

    private void saveGoal() {
        String type = (String) goalTypeBox.getSelectedItem();
        String amountText = amountField.getText().trim();
        String deadline = deadlineField.getText().trim();
        String progressText = progressField.getText().trim();

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

    // === Inner Goal Class ===
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

        int number = 0;
        @Override
        public String toString() {
            number +=1;
            return number + "- " +
                    "Type: " + goalType +
                    ", Target: $" + targetAmount +
                    ", Deadline: " + deadline +
                    ", Progress: $" + currentProgress;
        }

        public String toCSV() {
            return goalType + ", " + targetAmount + ", " + deadline + ", " + currentProgress + "\n";
        }

        public static Goal fromCSV(String csv) {
            try {
                String[] parts = csv.split(", ");
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
