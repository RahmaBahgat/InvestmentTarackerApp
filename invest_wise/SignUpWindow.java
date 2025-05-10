package invest_wise;

import javax.swing.*;
import java.awt.*;
import java.util.regex.*;

/**
 * SignUpWindow class handles the user registration process.
 * Extends login_signup to inherit common styling and functionality.
 */
public class SignUpWindow extends login_signup {

    /**
     * Constructor for the sign-up window.
     * Creates and initializes the registration form with input validation.
     * @param loginFrame The parent login window frame
     */
    SignUpWindow(JFrame loginFrame) {
        // Create main container panel with consistent styling
        JPanel mainPanel = createStyledPanel();
        // Create input panel with grid layout for form fields
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBackground(Color.decode("#f5efe7"));

        // Define common styling for form elements
        Font labelFont = new Font("Segoe UI Emoji", Font.PLAIN, 18);
        Color labelColor = Color.decode("#3e5879");
        Dimension fieldSize = new Dimension(200, 40);
        Font fieldFont = new Font("Segoe UI Emoji", Font.PLAIN, 16);

        // Create and style input fields
        JTextField usernameField = styledTextField(fieldFont, fieldSize);
        JPasswordField passwordField = styledPasswordField(fieldFont, fieldSize);
        JTextField fullNameField = styledTextField(fieldFont, fieldSize);
        JTextField emailField = styledTextField(fieldFont, fieldSize);
        JTextField phoneField = styledTextField(fieldFont, fieldSize);
        JTextField balanceField = styledTextField(fieldFont, fieldSize);

        // Add labels and fields to input panel
        inputPanel.add(styledLabel("Username:", labelFont, labelColor));
        inputPanel.add(usernameField);
        inputPanel.add(styledLabel("Password:", labelFont, labelColor));
        inputPanel.add(passwordField);
        inputPanel.add(styledLabel("Full Name:", labelFont, labelColor));
        inputPanel.add(fullNameField);
        inputPanel.add(styledLabel("Email:", labelFont, labelColor));
        inputPanel.add(emailField);
        inputPanel.add(styledLabel("Phone:", labelFont, labelColor));
        inputPanel.add(phoneField);
        inputPanel.add(styledLabel("Initial Balance:", labelFont, labelColor));
        inputPanel.add(balanceField);

        // Create panel for action buttons
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBackground(Color.decode("#f5efe7"));

        // Create submit button with validation logic
        JButton submit = styledButton("Create Account");
        submit.addActionListener(e -> {
            // Get input values
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());
            String fullName = fullNameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String balance = balanceField.getText();

            // Validate all fields are filled
            if (user.isEmpty() || pass.isEmpty() || fullName.isEmpty() || email.isEmpty() ||
                    phone.isEmpty() || balance.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate email format
            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "Please enter a valid email format.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate phone number format
            if (!isValidPhone(phone)) {
                JOptionPane.showMessageDialog(this, "Phone number must start with '01' and be 14 digits.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate password strength
            if (!isValidPassword(pass)) {
                JOptionPane.showMessageDialog(this, "Password must contain letters, numbers, and special characters.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate balance format
            if (!isValidBalance(balance)) {
                JOptionPane.showMessageDialog(this, "Initial Balance must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate full name format
            if (!isValidFullName(fullName)) {
                JOptionPane.showMessageDialog(this, "Full Name must contain only letters and spaces.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if username already exists
            if (userExists(user)) {
                JOptionPane.showMessageDialog(this, "User already exists.");
            } else {
                // Add new user and proceed to home screen
                addUser(user, pass, fullName, email, phone, balance);
                JOptionPane.showMessageDialog(this, "User registered successfully.");
                new Home();
                dispose();
            }
        });

        // Create back button to return to login screen
        JButton back = styledButton("Back");
        back.addActionListener(e -> {
            loginFrame.setVisible(true);
            dispose();
        });

        // Add buttons to panel with spacing
        buttonsPanel.add(submit);
        buttonsPanel.add(Box.createVerticalStrut(10));
        buttonsPanel.add(back);

        // Assemble the main panel
        mainPanel.add(inputPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buttonsPanel);

        // Create center wrapper for proper alignment
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(Color.decode("#f5efe7"));
        centerWrapper.add(mainPanel);

        add(centerWrapper, BorderLayout.CENTER);
        setVisible(true);
    }

    /**
     * Validates email format using regex pattern.
     * @param email The email address to validate
     * @return boolean indicating if email is valid
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Validates phone number format (must start with '01' and be 14 digits).
     * @param phone The phone number to validate
     * @return boolean indicating if phone number is valid
     */
    private boolean isValidPhone(String phone) {
        return phone.startsWith("01") && phone.length() == 14 && phone.matches("\\d+");
    }

    /**
     * Validates password strength (must contain letters, numbers, and special characters).
     * @param password The password to validate
     * @return boolean indicating if password meets requirements
     */
    private boolean isValidPassword(String password) {
        return password.matches(".*[a-zA-Z].*") && password.matches(".*\\d.*") && password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
    }

    /**
     * Validates balance format (must be numeric).
     * @param balance The balance to validate
     * @return boolean indicating if balance is valid
     */
    private boolean isValidBalance(String balance) {
        return balance.matches("\\d+");
    }

    /**
     * Validates full name format (only letters and spaces allowed).
     * @param fullName The full name to validate
     * @return boolean indicating if full name is valid
     */
    private boolean isValidFullName(String fullName) {
        return fullName.matches("[a-zA-Z ]+");
    }

    /**
     * Creates a styled label with specified font and color.
     * @param text The label text
     * @param font The font to use
     * @param color The text color
     * @return JLabel with specified styling
     */
    private JLabel styledLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    /**
     * Creates a styled text field with specified font and size.
     * @param font The font to use
     * @param size The preferred size
     * @return JTextField with specified styling
     */
    private JTextField styledTextField(Font font, Dimension size) {
        JTextField field = new JTextField();
        field.setFont(font);
        field.setPreferredSize(size);
        return field;
    }

    /**
     * Creates a styled password field with specified font and size.
     * @param font The font to use
     * @param size The preferred size
     * @return JPasswordField with specified styling
     */
    private JPasswordField styledPasswordField(Font font, Dimension size) {
        JPasswordField field = new JPasswordField();
        field.setFont(font);
        field.setPreferredSize(size);
        return field;
    }
}
