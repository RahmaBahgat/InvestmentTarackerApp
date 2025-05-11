package invest_wise;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The main entry point for the InvestWise application.
 * This class extends the styles class and creates the initial login window.
 */
public class InvestWise extends styles {

    /**
     * Constructs the main InvestWise window with a login button.
     * Initializes the UI components and sets up the main panel.
     */
    public InvestWise() {
        window();
        // === Main Panel ===
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.decode("#f5efe7"));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);

        // === App Title ===
        JLabel titleLabel = new JLabel("Investa");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.decode("#2c3e50"));
        gbc.gridy = 0;
        mainPanel.add(titleLabel, gbc);

        // === Subtitle ===
        JLabel subtitleLabel = new JLabel("Your Smart Investment Companion");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(Color.decode("#7f8c8d"));
        gbc.gridy = 1;
        mainPanel.add(subtitleLabel, gbc);

        // === Login Button ===
        gbc.gridy = 2;  // Move the button below the welcome message
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(200, 50));
        buttons(loginButton);

        // Add action to login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new login_signup();
                setVisible(false);
            }
        });

        // Add the login button to the panel
        mainPanel.add(loginButton, gbc);

        // === Footer ===
        JLabel footerLabel = new JLabel("© 2025 Investa. All rights reserved.");
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerLabel.setForeground(Color.decode("#95a5a6"));
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.weighty = 1.0;
        mainPanel.add(footerLabel, gbc);

        // === Final Frame Setup ===
        add(mainPanel, BorderLayout.CENTER);
        setMinimumSize(new Dimension(500, 400));
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * The main method that launches the InvestWise application.
     * Uses SwingUtilities.invokeLater to ensure thread safety for GUI creation.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InvestWise app = new InvestWise();
            app.setVisible(true);
        });
    }
}
