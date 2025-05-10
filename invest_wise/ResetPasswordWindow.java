package invest_wise;

import javax.swing.*;
import java.awt.*;

/**
 * ResetPasswordWindow class handles the password reset functionality.
 * Extends login_signup to inherit common styling and user management methods.
 * Provides a form for users to reset their password by entering their username and new password.
 */
public class ResetPasswordWindow extends login_signup {
    /**
     * Constructor for the password reset window.
     * Creates and initializes the password reset form with validation.
     * 
     * @param loginFrame The parent login window frame for navigation
     */
    ResetPasswordWindow(JFrame loginFrame) {
        // Create main container panel with consistent styling
        JPanel mainPanel = createStyledPanel();

        // === Input Form Panel Setup ===
        // Create panel with grid layout for username and password fields
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 20, 15));
        inputPanel.setBackground(Color.decode("#f5efe7")); // Light beige background

        // Create and style username label and field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        usernameLabel.setForeground(Color.decode("#3e5879")); // Dark blue text

        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        usernameField.setPreferredSize(new Dimension(250, 40));

        // Create and style new password label and field
        JLabel passwordLabel = new JLabel("New Password:");
        passwordLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        passwordLabel.setForeground(Color.decode("#3e5879")); // Dark blue text

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        passwordField.setPreferredSize(new Dimension(250, 40));

        // Add components to input panel
        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);

        // === Action Buttons Panel Setup ===
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBackground(Color.decode("#f5efe7")); // Light beige background

        // Create and configure reset password button
        JButton reset = styledButton("Update Password");
        reset.addActionListener(e -> {
            String user = usernameField.getText();
            String newPass = new String(passwordField.getPassword());
            
            // Validate user exists and update password
            if (userExists(user)) {
                updatePassword(user, newPass);
                JOptionPane.showMessageDialog(this, "Password updated.");
                loginFrame.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "User not found.");
            }
        });

        // Create and configure back button
        JButton back = styledButton("Back");
        back.addActionListener(e -> {
            loginFrame.setVisible(true);
            dispose();
        });

        // Add buttons to panel with spacing
        buttonsPanel.add(reset);
        buttonsPanel.add(Box.createVerticalStrut(10));
        buttonsPanel.add(back);

        // Assemble the main panel
        mainPanel.add(inputPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buttonsPanel);

        // Create center wrapper for proper alignment
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(Color.decode("#f5efe7")); // Light beige background
        centerWrapper.add(mainPanel);

        // Add the centered panel to the window
        add(centerWrapper, BorderLayout.CENTER);
        setVisible(true);
    }
}