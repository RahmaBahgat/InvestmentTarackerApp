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
        JButton zakatButton = new JButton("Zakat Calculator");
        buttons(FinancialButton);
        buttons(zakatButton);


        zakatButton.addActionListener(e -> {
            new ZakatCalculator(Home.this);
            setVisible(false);
        });



        // Add action to login button
        FinancialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FinancialGoalsApp();
                setVisible(false);
            }
        });

        // === Buttons Panel ===
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0)); // 20 px horizontal gap
        buttonsPanel.setBackground(Color.decode("#f5efe7")); // Match background

        buttonsPanel.add(FinancialButton);
        buttonsPanel.add(zakatButton);

        // Add buttonsPanel to mainPanel
        gbc.gridy = 2;
        mainPanel.add(buttonsPanel, gbc);


        // === Final Frame Setup ===
        add(mainPanel, BorderLayout.CENTER);
    }
}
