package invest_wise;

import javax.swing.*;
import java.awt.*;
import java.io.*;

class login_signup extends styles {
    private static final String USERS_FILE = "src/invest_wise/users.txt";

    public login_signup() {
        window();
        setTitle("Login");

        JPanel mainPanel = createStyledPanel();

        JPanel inputPanel = createInputPanel();
        JTextField usernameField = (JTextField) inputPanel.getComponent(1);
        JPasswordField passwordField = (JPasswordField) inputPanel.getComponent(3);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBackground(Color.decode("#f5efe7"));

        JButton loginSubmitButton = styledButton("Login");
        JButton signUpButton = styledButton("Sign Up");
        JButton resetButton = styledButton("Reset Password");

        buttonsPanel.add(loginSubmitButton);
        buttonsPanel.add(Box.createVerticalStrut(10));
        buttonsPanel.add(signUpButton);
        buttonsPanel.add(Box.createVerticalStrut(10));
        buttonsPanel.add(resetButton);

        loginSubmitButton.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());
            if (validateLogin(user, pass)) {
                JOptionPane.showMessageDialog(this, "Login successful!");

                new Home();
                dispose();  // Close the login window
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        signUpButton.addActionListener(e -> openSignUpWindow(this));
        resetButton.addActionListener(e -> openResetPasswordWindow(this));

        mainPanel.add(inputPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buttonsPanel);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(Color.decode("#f5efe7"));
        centerWrapper.add(mainPanel);

        add(centerWrapper, BorderLayout.CENTER);
    }

    // === Shared Components ===
    public JPanel createStyledPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.decode("#f5efe7"));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 70, 30, 10));
        return panel;
    }

    public JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBackground(Color.decode("#f5efe7"));

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        usernameLabel.setForeground(Color.decode("#3e5879"));

        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        usernameField.setPreferredSize(new Dimension(200, 40));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        passwordLabel.setForeground(Color.decode("#3e5879"));

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        passwordField.setPreferredSize(new Dimension(200, 40));

        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);

        return inputPanel;
    }

    public JButton styledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        button.setBackground(Color.decode("#3e5879"));
        button.setForeground(Color.decode("#f5efe7"));
        button.setPreferredSize(new Dimension(200, 50));
        button.setMaximumSize(new Dimension(200, 50));
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    // === File Operations ===

    private boolean validateLogin(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] creds = line.split(",");
                if (creds.length >= 2 && creds[0].equals(username) && creds[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading user file.");
        }
        return false;
    }

    boolean userExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] creds = line.split(",");
                if (creds.length >= 1 && creds[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException ignored) {}
        return false;
    }

    void addUser(String username, String password, String fullName, String email, String phone, String balance) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            writer.write(String.join(",", username, password, fullName, email, phone, balance));
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing to file.");
        }
    }

    void updatePassword(String username, String newPassword) {
        File inputFile = new File(USERS_FILE);
        File tempFile = new File("src/invest_wise/temp_users.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] creds = line.split(",");
                if (creds[0].equals(username)) {
                    creds[1] = newPassword; // update only the password
                    writer.write(String.join(",", creds));
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error updating password.");
            return;
        }

        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            JOptionPane.showMessageDialog(this, "Failed to save updated password.");
        }
    }

    // === Function to Open Sign-Up Window ===
    private void openSignUpWindow(JFrame loginFrame) {
        loginFrame.setVisible(false);
        new SignUpWindow(loginFrame);
    }

    // === Function to Open Reset Password Window ===
    private void openResetPasswordWindow(JFrame loginFrame) {
        loginFrame.setVisible(false);
        new ResetPasswordWindow(loginFrame);
    }

}
