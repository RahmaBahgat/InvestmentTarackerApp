package invest_wise;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Home extends styles{
    public Home(){
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
        gbc.gridy = 2;
        JButton FinancialButton = new JButton("Financial goals");
        buttons(FinancialButton);

        // Add action to login button
        FinancialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FinancialGoalsApp();
                setVisible(false);
            }
        });

        // Add the login button to the panel
        mainPanel.add(FinancialButton, gbc);

        // === Final Frame Setup ===
        add(mainPanel, BorderLayout.CENTER);
    }
}
