package invest_wise;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Home extends styles {
    private ArrayList<Asset> assets = new ArrayList<>();
    private CardLayout cardLayout;
    private JPanel mainCardPanel;
    private RiskAssessmentScreen riskAssessmentScreen;

    public Home() {
        window();
        setTitle("InvestWise - Home");

        // Initialize card layout
        cardLayout = new CardLayout();
        mainCardPanel = new JPanel(cardLayout);
        mainCardPanel.setBackground(Color.decode("#f5efe7"));

        // Create home view
        JPanel homeView = createHomeView();
        mainCardPanel.add(homeView, "HOME");

        riskAssessmentScreen = new RiskAssessmentScreen(this, this);
        mainCardPanel.add(riskAssessmentScreen.getRiskPanel(), "RISK_ASSESSMENT");

        add(mainCardPanel, BorderLayout.CENTER);
    }

    private JPanel createHomeView() {
        JPanel homePanel = new JPanel(new GridBagLayout());
        homePanel.setBackground(Color.decode("#f5efe7"));

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
            new AddAssets(this, assets).setVisible(true);
        });

        editRemoveButton.addActionListener(e -> {
            this.setVisible(false);
            new EditRemoveAssets(this, assets).setVisible(true);
        });

        riskAssessmentButton.addActionListener(e -> {
            if (!RiskAssessmentScreen.checkAssetsExist()) {
                JOptionPane.showMessageDialog(Home.this,
                        "Please add assets first to assess risk",
                        "No Assets",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                riskAssessmentScreen.refreshData();
                cardLayout.show(mainCardPanel, "RISK_ASSESSMENT");
                setTitle("InvestWise - Risk Assessment");
            }
        });

        financialButton.addActionListener(e -> {
            new FinancialGoals();
            setVisible(false);
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

        // Add buttonsPanel to homePanel
        gbc.gridy = 2;
        homePanel.add(buttonsPanel, gbc);

        return homePanel;
    }

    public void showHomeView() {
        cardLayout.show(mainCardPanel, "HOME");
        setTitle("InvestWise - Home");
    }

    public void updateAssets(ArrayList<Asset> updatedAssets) {
        this.assets = new ArrayList<>(updatedAssets);
    }
}
