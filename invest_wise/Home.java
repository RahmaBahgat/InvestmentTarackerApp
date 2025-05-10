package invest_wise;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Home extends styles {
    // Declare the assets list as a class member
    private ArrayList<Asset> assets = new ArrayList<>();

    public Home() {
        window();
        // === Main Panel ===
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.decode("#f5efe7"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // === Buttons ===
        gbc.gridy = 2;
        JButton financialButton = new JButton("Financial goals");
        JButton zakatButton = new JButton("Zakat Calculator");
        JButton stockButton = new JButton("Connect Stock Market");
        JButton reportButton = new JButton("Report & Insights");
        JButton addAssetsButton = new JButton("Add Assets");
        JButton editRemoveButton = new JButton("Edit/Remove Assets");
        JButton riskAssessmentButton = new JButton("Risk Assessment");

        // Style buttons
        buttons(financialButton);
        buttons(zakatButton);
        buttons(stockButton);
        buttons(reportButton);
        buttons(addAssetsButton);
        buttons(editRemoveButton);
        buttons(riskAssessmentButton);

        // === Button Actions ===
        zakatButton.addActionListener(e -> {
            new ZakatCalculator(Home.this);
            setVisible(false);
        });

        stockButton.addActionListener(e -> {
            new StockAccountConnection(Home.this);
            setVisible(false);
        });

        reportButton.addActionListener(e -> new ReportAndInsights(Home.this));

        addAssetsButton.addActionListener(e -> {
            this.setVisible(false);
            new AddAssets(this, assets).setVisible(true);  // Pass both Home and assets
        });

        editRemoveButton.addActionListener(e -> {
            this.setVisible(false);
            new EditRemoveAssets(this, assets).setVisible(true);
        });
        riskAssessmentButton.addActionListener(e -> {
            if (assets.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please add assets first to assess risk",
                        "No Assets",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                setVisible(false);
                new RiskAssessmentScreen(this, assets).setVisible(true);
            }
        });

        financialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FinancialGoals();
                setVisible(false);
            }
        });

        // === Buttons Panel ===
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setPreferredSize(new Dimension(600, 400));
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonsPanel.setBackground(Color.decode("#f5efe7"));

        buttonsPanel.add(financialButton);
        buttonsPanel.add(zakatButton);
        buttonsPanel.add(stockButton);
        buttonsPanel.add(reportButton);
        buttonsPanel.add(addAssetsButton);
        buttonsPanel.add(editRemoveButton);
        buttonsPanel.add(riskAssessmentButton);


        // Add buttonsPanel to mainPanel
        gbc.gridy = 2;
        mainPanel.add(buttonsPanel, gbc);

        // === Final Frame Setup ===
        add(mainPanel, BorderLayout.CENTER);
    }

    public void updateAssets(ArrayList<Asset> updatedAssets) {
        this.assets = new ArrayList<>(updatedAssets);
    }
}
