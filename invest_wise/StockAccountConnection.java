package invest_wise;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * Provides functionality for connecting stock trading accounts to the InvestWise application.
 * Handles user authentication and account linking for various trading platforms.
 */
public class StockAccountConnection extends styles {
    /** Input field for email/username */
    private JTextField emailField, otpField;
    /** Input field for password */
    private JPasswordField passwordField;
    /** Dropdown for selecting trading platform */
    private JComboBox<String> platformBox;
    /** Currently logged in user's username */
    private String currentUser;

    /**
     * Constructs the stock account connection window.
     * Initializes the UI components and handles user authentication.
     *
     * @param previousFrame The frame to return to when closing this window
     */
    public StockAccountConnection(JFrame previousFrame) {
        currentUser = login_signup.getCurrentUser();
        if (currentUser == null || currentUser.isEmpty()) {
            JOptionPane.showMessageDialog(this, "You must be logged in to connect a stock account.");
            previousFrame.setVisible(true);
            dispose();
            return;
        }

        window();
        setTitle("Connect Stock Account");

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.decode("#f5efe7"));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // === Form Panel ===
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBackground(Color.decode("#f5efe7"));

        String[] platforms = {"E*TRADE", "TDAmeritrade", "Interactive Brokers", "Custom"};
        platformBox = new JComboBox<>(platforms);
        emailField = new JTextField();
        passwordField = new JPasswordField();
        otpField = new JTextField();

        formPanel.add(new JLabel("Select Platform:"));
        formPanel.add(platformBox);

        formPanel.add(new JLabel("Email / Username:"));
        formPanel.add(emailField);

        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);

        formPanel.add(new JLabel("OTP Code:"));
        formPanel.add(otpField);

        // Buttons
        JButton connectButton = styledButton("Connect Account");
        JButton backButton = styledButton("Back");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.decode("#f5efe7"));
        buttonPanel.add(connectButton);
        buttonPanel.add(backButton);

        // Actions
        connectButton.addActionListener(this::handleConnection);
        backButton.addActionListener(e -> {
            previousFrame.setVisible(true);
            dispose();
        });

        // Layout
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    /**
     * Handles the account connection process.
     * Validates input fields and saves account information.
     *
     * @param e The action event that triggered the connection
     */
    private void handleConnection(ActionEvent e) {
        String platform = (String) platformBox.getSelectedItem();
        String email = emailField.getText().trim();
        String password = new String(this.passwordField.getPassword()); // ✅ Use the correct password field
        String otp = otpField.getText().trim();

        if (email.isEmpty() || password.isEmpty() || otp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Simulate OTP check
        if (!otp.equals("123456")) {
            JOptionPane.showMessageDialog(this, "Invalid OTP code.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Save account info
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("invest_wise/stock_accounts.txt", true));
            writer.write(currentUser + "," + platform + "," + email + "," + password);
            writer.newLine();
            writer.close();

            JOptionPane.showMessageDialog(this, "✅ Successfully connected to " + platform + "!");

            // Open mock dashboard
            new PortfolioDashboard(this, platform);
            setVisible(false);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to save account information.");
        }
    }
}
