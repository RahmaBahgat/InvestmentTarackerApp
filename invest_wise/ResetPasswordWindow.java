package invest_wise;

import javax.swing.*;
import java.awt.*;

public class ResetPasswordWindow extends login_signup {
    ResetPasswordWindow(JFrame loginFrame) {
        styles styleHelper = new styles();
        styleHelper.window();
        setTitle("Reset Password");

        // Main styled panel
        JPanel mainPanel = createStyledPanel();

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 20, 15));
        inputPanel.setBackground(Color.decode("#f5efe7"));

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        usernameLabel.setForeground(Color.decode("#3e5879"));

        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        usernameField.setPreferredSize(new Dimension(250, 40));

        JLabel passwordLabel = new JLabel("New Password:");
        passwordLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        passwordLabel.setForeground(Color.decode("#3e5879"));

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        passwordField.setPreferredSize(new Dimension(250, 40));

        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);

        // Buttons Panel (Reset + Back)
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBackground(Color.decode("#f5efe7"));

        JButton reset = styledButton("Update Password");
        reset.addActionListener(e -> {
            String user = usernameField.getText();
            String newPass = new String(passwordField.getPassword());
            if (userExists(user)) {
                updatePassword(user, newPass);
                JOptionPane.showMessageDialog(this, "Password updated.");
                loginFrame.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "User not found.");
            }
        });

        JButton back = styledButton("Back");
        back.addActionListener(e -> {
            loginFrame.setVisible(true);
            dispose();
        });

        buttonsPanel.add(reset);
        buttonsPanel.add(Box.createVerticalStrut(10));
        buttonsPanel.add(back);

        mainPanel.add(inputPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buttonsPanel);

        // Centering the panel like in login_signup
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(Color.decode("#f5efe7"));
        centerWrapper.add(mainPanel);

        add(centerWrapper, BorderLayout.CENTER);
        setVisible(true);
    }
}