package invest_wise;

import javax.swing.*;
import java.awt.*;
import java.util.regex.*;

public class SignUpWindow extends login_signup {

    SignUpWindow(JFrame loginFrame) {
        styles styleHelper = new styles();
        styleHelper.window();

        // === Styled Outer Panel ===
        JPanel mainPanel = createStyledPanel();
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBackground(Color.decode("#f5efe7"));

        Font labelFont = new Font("Segoe UI Emoji", Font.PLAIN, 18);
        Color labelColor = Color.decode("#3e5879");
        Dimension fieldSize = new Dimension(200, 40);
        Font fieldFont = new Font("Segoe UI Emoji", Font.PLAIN, 16);

        // === Input Fields ===
        JTextField usernameField = styledTextField(fieldFont, fieldSize);
        JPasswordField passwordField = styledPasswordField(fieldFont, fieldSize);
        JTextField fullNameField = styledTextField(fieldFont, fieldSize);
        JTextField emailField = styledTextField(fieldFont, fieldSize);
        JTextField phoneField = styledTextField(fieldFont, fieldSize);
        JTextField balanceField = styledTextField(fieldFont, fieldSize);

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

        // === Buttons Panel (Submit + Back) ===
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBackground(Color.decode("#f5efe7"));

        JButton submit = styledButton("Create Account");
        submit.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());
            String fullName = fullNameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String balance = balanceField.getText();

            // Validation for empty fields
            if (user.isEmpty() || pass.isEmpty() || fullName.isEmpty() || email.isEmpty() ||
                    phone.isEmpty() || balance.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Email format validation
            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "Please enter a valid email format.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Phone number validation (must start with "01" and be 14 digits)
            if (!isValidPhone(phone)) {
                JOptionPane.showMessageDialog(this, "Phone number must start with '01' and be 14 digits.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Password validation (must contain letters, numbers, and special characters)
            if (!isValidPassword(pass)) {
                JOptionPane.showMessageDialog(this, "Password must contain letters, numbers, and special characters.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Initial Balance validation (must be numeric)
            if (!isValidBalance(balance)) {
                JOptionPane.showMessageDialog(this, "Initial Balance must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Full Name validation (only letters and spaces)
            if (!isValidFullName(fullName)) {
                JOptionPane.showMessageDialog(this, "Full Name must contain only letters and spaces.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if user exists
            if (userExists(user)) {
                JOptionPane.showMessageDialog(this, "User already exists.");
            } else {
                addUser(user, pass, fullName, email, phone, balance);
                JOptionPane.showMessageDialog(this, "User registered successfully.");
                new Home();
                dispose();
            }
        });

        JButton back = styledButton("Back");
        back.addActionListener(e -> {
            loginFrame.setVisible(true);
            dispose();
        });

        buttonsPanel.add(submit);
        buttonsPanel.add(Box.createVerticalStrut(10));
        buttonsPanel.add(back);

        mainPanel.add(inputPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buttonsPanel);

        // === Center Wrapper (like login) ===
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(Color.decode("#f5efe7"));
        centerWrapper.add(mainPanel);

        add(centerWrapper, BorderLayout.CENTER);
        setVisible(true);
    }

    // Validation Methods

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPhone(String phone) {
        return phone.startsWith("01") && phone.length() == 14 && phone.matches("\\d+");
    }

    private boolean isValidPassword(String password) {
        return password.matches(".*[a-zA-Z].*") && password.matches(".*\\d.*") && password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
    }

    private boolean isValidBalance(String balance) {
        return balance.matches("\\d+");
    }

    private boolean isValidFullName(String fullName) {
        return fullName.matches("[a-zA-Z ]+");
    }

    private JLabel styledLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    private JTextField styledTextField(Font font, Dimension size) {
        JTextField field = new JTextField();
        field.setFont(font);
        field.setPreferredSize(size);
        return field;
    }

    private JPasswordField styledPasswordField(Font font, Dimension size) {
        JPasswordField field = new JPasswordField();
        field.setFont(font);
        field.setPreferredSize(size);
        return field;
    }
}
