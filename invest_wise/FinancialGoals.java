package invest_wise;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class FinancialGoals extends login_signup {
    private JComboBox<String> goalTypeBox;
    private JTextField amountField, deadlineField, progressField;
    private JTextArea goalListArea;
    private static ArrayList<Goal> goals = new ArrayList<>();
    private static final String FILE_NAME = "invest_wise/goals.txt";

    public FinancialGoals() {
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

        JButton saveBtn = styledButton("Save Goal");
        JButton listBtn = styledButton("List Goals");
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
        JPanel mainPanel = createStyledPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(formWrapper, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        // Load + Button Events
        loadGoalsFromFile();
        saveBtn.addActionListener(e -> saveGoal());
        listBtn.addActionListener(e -> listGoals());
    }

    public static ArrayList<Goal> loadGoals(String username) {
        ArrayList<Goal> loadedGoals = new ArrayList<>();
        File file = new File("invest_wise/goals.txt");
        if (!file.exists()) return loadedGoals;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                Goal goal = Goal.fromCSV(line);
                if (goal != null) {
                    loadedGoals.add(goal);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading goals for report.");
        }

        return loadedGoals;
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

            // === DATE VALIDATION ===
            if (!deadline.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(this, "Date format must be YYYY-MM-DD.");
                return;
            }

            String[] parts = deadline.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);

            if (year < 2025) {
                JOptionPane.showMessageDialog(this, "Year must be 2025 or later.");
                return;
            }
            if (month < 1 || month > 12) {
                JOptionPane.showMessageDialog(this, "Month must be between 1 and 12.");
                return;
            }
            if (day < 1 || day > 31) {
                JOptionPane.showMessageDialog(this, "Day must be between 1 and 31.");
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
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unexpected error: " + ex.getMessage());
        }
    }


    private void listGoals() {
        if (goals.isEmpty()) {
            goalListArea.setText("No goals found.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Goal g : goals) {
            sb.append(g);
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

        @Override
        public String toString() {
            return  "⇛" +
                    "Type: " + goalType +
                    ", Target: $" + targetAmount +
                    ", Deadline: " + deadline +
                    ", Progress: $" + currentProgress;
        }

        public String toCSV() {
            return goalType + ", " + targetAmount + ", " + deadline + ", " + currentProgress;
        }

        public static Goal fromCSV(String csv) {
            try {
                String[] parts = csv.split(", ");
                if (parts.length == 4) {
                    String type = parts[0];
                    double target = Double.parseDouble(parts[1]);
                    String deadline = parts[2];
                    double progress = Double.parseDouble(parts[3]);
                    return new Goal(type, target, deadline, progress);
                }
            } catch (Exception ignored) {}

            return null;
        }
    }
}
