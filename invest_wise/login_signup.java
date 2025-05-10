package invest_wise;

import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 * Main login and signup class that handles user authentication and registration.
 * Extends the styles class to inherit common UI styling methods.
 */
class login_signup extends styles {
    // File path for storing user credentials and information
    private static final String USERS_FILE = "invest_wise/users.txt";
    // Stores the currently logged-in user's username
    private static String currentUser = "";

    /**
     * Constructor for the login window.
     * Initializes the main login interface with username and password fields.
     */
    public login_signup() {
        window();

        // Create main container panel with consistent styling
        JPanel mainPanel = createStyledPanel();
        // Create input panel with username and password fields
        JPanel inputPanel = createInputPanel();
        JTextField usernameField = (JTextField) inputPanel.getComponent(1);
        JPasswordField passwordField = (JPasswordField) inputPanel.getComponent(3);

        // Create panel for action buttons
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBackground(Color.decode("#f5efe7"));

        // Create and style the main action buttons
        JButton loginSubmitButton = styledButton("Login");
        JButton signUpButton = styledButton("Sign Up");
        JButton resetButton = styledButton("Reset Password");

        // Add buttons to panel with spacing
        buttonsPanel.add(loginSubmitButton);
        buttonsPanel.add(Box.createVerticalStrut(10));
        buttonsPanel.add(signUpButton);
        buttonsPanel.add(Box.createVerticalStrut(10));
        buttonsPanel.add(resetButton);

        // Login button action handler
        loginSubmitButton.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());
            if (validateLogin(user, pass)) {
                currentUser = user; // Set current user after successful login
                JOptionPane.showMessageDialog(this, "Login successful!");
                new Home();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Sign Up button action handler
        signUpButton.addActionListener(e -> openSignUpWindow(this));
        // Reset Password button action handler
        resetButton.addActionListener(e -> openResetPasswordWindow(this));

        // Assemble the main panel
        mainPanel.add(inputPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buttonsPanel);

        // Create center wrapper for proper alignment
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(Color.decode("#f5efe7"));
        centerWrapper.add(mainPanel);

        add(centerWrapper, BorderLayout.CENTER);
    }

    /**
     * Returns the currently logged-in user's username.
     * @return String containing the current user's username
     */
    public static String getCurrentUser() {
        return currentUser;
    }

    /**
     * Creates a styled panel with consistent padding and background.
     * @return JPanel with predefined styling
     */
    public JPanel createStyledPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.decode("#f5efe7"));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 70, 30, 10));
        return panel;
    }

    /**
     * Creates the input panel containing username and password fields.
     * @return JPanel containing styled input fields
     */
    public JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBackground(Color.decode("#f5efe7"));

        // Create and style username label and field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        usernameLabel.setForeground(Color.decode("#3e5879"));

        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        usernameField.setPreferredSize(new Dimension(200, 40));

        // Create and style password label and field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        passwordLabel.setForeground(Color.decode("#3e5879"));

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        passwordField.setPreferredSize(new Dimension(200, 40));

        // Add components to panel
        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);

        return inputPanel;
    }

    /**
     * Creates a styled button with consistent appearance.
     * @param text The text to display on the button
     * @return JButton with predefined styling
     */
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

    /**
     * Validates user login credentials against stored user data.
     * @param username The username to validate
     * @param password The password to validate
     * @return boolean indicating if login is valid
     */
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

    /**
     * Checks if a username already exists in the user database.
     * @param username The username to check
     * @return boolean indicating if username exists
     */
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

    /**
     * Adds a new user to the user database.
     * @param username User's username
     * @param password User's password
     * @param fullName User's full name
     * @param email User's email address
     * @param phone User's phone number
     * @param balance User's initial balance
     */
    void addUser(String username, String password, String fullName, String email, String phone, String balance) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            writer.write(String.join(",", username, password, fullName, email, phone, balance));
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing to file.");
        }
    }

    /**
     * Updates a user's password in the user database.
     * @param username The username whose password to update
     * @param newPassword The new password to set
     */
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

    /**
     * Opens the sign-up window and hides the login window.
     * @param loginFrame The current login window frame
     */
    private void openSignUpWindow(JFrame loginFrame) {
        loginFrame.setVisible(false);
        new SignUpWindow(loginFrame);
    }

    /**
     * Opens the password reset window and hides the login window.
     * @param loginFrame The current login window frame
     */
    private void openResetPasswordWindow(JFrame loginFrame) {
        loginFrame.setVisible(false);
        new ResetPasswordWindow(loginFrame);
    }
}
