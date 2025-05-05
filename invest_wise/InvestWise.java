package invest_wise;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InvestWise extends styles {

    public InvestWise() {
        window();
        // === Main Panel ===
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());  // Use GridBagLayout for centering
        mainPanel.setBackground(Color.decode("#f5efe7"));

        // Create a GridBagConstraints object to align components
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;  // Center horizontally
        gbc.gridy = 0;  // Center vertically
        gbc.anchor = GridBagConstraints.CENTER; // Center the components

        // === Welcome Message ===
        JLabel welcomeMessage = new JLabel("Welcome to InvestWise!");
        welcomeMessage.setFont(new Font("Segoe UI Emoji", Font.BOLD, 20));
        welcomeMessage.setForeground(Color.decode("#3e5879"));
        welcomeMessage.setPreferredSize(new Dimension(300, 40));  // Adjust the size of the label if needed
        mainPanel.add(welcomeMessage, gbc);

        // === Login Button ===
        gbc.gridy = 1;  // Move the button below the welcome message
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        loginButton.setBackground(Color.decode("#3e5879"));
        loginButton.setForeground(Color.decode("#f5efe7"));
        loginButton.setPreferredSize(new Dimension(200, 50));  // Set the size to 200px width and 50px height
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Add action to login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the login window when clicked
                new login_signup();  // Assuming LoginWindow is another class
                setVisible(false);  // Hide the welcome window
            }
        });

        // Add the login button to the panel
        mainPanel.add(loginButton, gbc);

        // === Final Frame Setup ===
        add(mainPanel, BorderLayout.CENTER);  // Only add the mainPanel once
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(InvestWise::new);
    }
}
