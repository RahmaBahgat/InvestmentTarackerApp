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
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.decode("#f5efe7"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // === Login Button ===
        gbc.gridy = 2;  // Move the button below the welcome message
        JButton loginButton = new JButton("Login");

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

        // === Final Frame Setup ===
        add(mainPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InvestWise::new);
    }
}
