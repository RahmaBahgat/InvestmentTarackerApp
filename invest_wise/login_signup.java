package invest_wise;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class login_signup extends styles {
    public login_signup() {
        window();
        setTitle("Login");

        // === Container Panel with vertical layout ===
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.decode("#f5efe7"));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50)); // padding around

        // === Username & Password Panel ===
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBackground(Color.decode("#f5efe7"));

        // Username label and field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));  // Increase font size
        usernameLabel.setForeground(Color.decode("#3e5879"));  // Change text color
        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));  // Increase font size for textfield
        usernameField.setPreferredSize(new Dimension(200, 40));  // Increase width

        // Password label and field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));  // Increase font size
        passwordLabel.setForeground(Color.decode("#3e5879"));  // Change text color
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));  // Increase font size for password field
        passwordField.setPreferredSize(new Dimension(200, 40));  // Increase width

        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);

        // === Buttons Panel ===
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

        // === Button Actions ===
        loginSubmitButton.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());

            if (validateLogin(user, pass)) {
                JOptionPane.showMessageDialog(this, "Login successful!");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        resetButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Reset Password window would open"));
        signUpButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Sign Up window would open"));

        // === Add input & buttons to main panel ===
        mainPanel.add(inputPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buttonsPanel);

        // === Centering in frame ===
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(Color.decode("#f5efe7"));
        centerWrapper.add(mainPanel);

        // Final window setup
        add(centerWrapper, BorderLayout.CENTER);
    }

    // === Shared Button Style ===
    private JButton styledButton(String text) {
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

    // === Login Validation ===
    private boolean validateLogin(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/invest_wise/users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] creds = line.split(",");
                if (creds.length == 2 && creds[0].equals(username) && creds[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading user data file.", "File Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
}
