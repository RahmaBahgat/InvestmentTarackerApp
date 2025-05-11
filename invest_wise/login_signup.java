package invest_wise;

import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 * Handles user authentication and registration functionality.
 * Provides a login interface and manages user data persistence.
 */
class login_signup extends styles {
    /** File path for storing user data */
    static final String USERS_FILE = "invest_wise/users.txt";
    /** Currently logged in user's username */
    private static String currentUser = "";

    /**
     * Constructs the login window with input fields and authentication buttons.
     * Initializes the UI components and sets up event handlers.
     */
    public login_signup() {
        window();

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
                currentUser = user; // ← Set current user after successful login
                JOptionPane.showMessageDialog(this, "Login successful!");
                new Home();
                dispose();
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

    /**
     * Gets the username of the currently logged in user.
     *
     * @return The username of the current user
     */
    public static String getCurrentUser() {
        return currentUser;
    }

    // === Shared Components ===
    /**
     * Creates a styled panel with consistent formatting.
     *
     * @return A configured JPanel with standard styling
     */
    public JPanel createStyledPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.decode("#f5efe7"));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 10));
        return panel;
    }

    /**
     * Creates the input panel containing username and password fields.
     *
     * @return A JPanel containing styled input fields
     */
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

    // === File Operations ===

    /**
     * Validates user login credentials against stored user data.
     *
     * @param username The username to validate
     * @param password The password to validate
     * @return true if credentials are valid, false otherwise
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
     *
     * @param username The username to check
     * @return true if the username exists, false otherwise
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
     *
     * @param username The new user's username
     * @param password The new user's password
     * @param fullName The new user's full name
     * @param email The new user's email
     * @param phone The new user's phone number
     * @param balance The new user's initial balance
     */
    void addUser(String username, String password, String fullName, String email, String phone, String balance) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            writer.write(String.join(",", username, password, fullName, email, phone, balance));
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing to file.");
        }
    }

    // === Function to Open Sign-Up Window ===
    /**
     * Opens the sign-up window and hides the login window.
     *
     * @param loginFrame The current login window to hide
     */
    private void openSignUpWindow(JFrame loginFrame) {
        loginFrame.setVisible(false);
        new SignUpWindow(loginFrame);
    }

    // === Function to Open Reset Password Window ===
    /**
     * Opens the password reset window and hides the login window.
     *
     * @param loginFrame The current login window to hide
     */
    private void openResetPasswordWindow(JFrame loginFrame) {
        loginFrame.setVisible(false);
        new ResetPasswordWindow(loginFrame);
    }

}
