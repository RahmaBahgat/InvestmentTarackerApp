package invest_wise;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * The main dashboard of the InvestWise application.
 * Provides access to various financial management features and displays user's assets.
 */
public class Home extends styles {
    /** List of user's financial assets */
    private ArrayList<Asset> assets = new ArrayList<>();
    /** Layout manager for switching between different views */
    private CardLayout cardLayout;
    /** Main panel containing all card views */
    private JPanel mainCardPanel;
    /** Risk assessment screen component */
    private RiskAssessmentScreen riskAssessmentScreen;

    /**
     * Constructs the home dashboard with navigation buttons and initializes the card layout.
     * Sets up the main interface and risk assessment screen.
     */
    public Home() {
        window();
        setTitle("Investa - Dashboard");
        getContentPane().setBackground(Color.decode("#F5EFE7"));

        // Initialize card layout
        cardLayout = new CardLayout();
        mainCardPanel = new JPanel(cardLayout);
        mainCardPanel.setBackground(Color.decode("#F5EFE7"));
        mainCardPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Create home view
        JPanel homeView = createHomeView();
        mainCardPanel.add(homeView, "HOME");

        riskAssessmentScreen = new RiskAssessmentScreen(this, this);
        mainCardPanel.add(riskAssessmentScreen.getRiskPanel(), "RISK_ASSESSMENT");

        add(mainCardPanel, BorderLayout.CENTER);
        setMinimumSize(new Dimension(900, 650));
        setLocationRelativeTo(null);
    }

    /**
     * Creates the main home view panel with navigation buttons.
     * Each button provides access to different financial management features.
     *
     * @return A JPanel containing the main navigation interface
     */
    private JPanel createHomeView() {
        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.setBackground(Color.decode("#F5EFE7"));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.decode("#F5EFE7"));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JLabel titleLabel = new JLabel("Investa Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.decode("#213555"));
        headerPanel.add(titleLabel);

        homePanel.add(headerPanel, BorderLayout.NORTH);

        // Main Buttons Panel
        JPanel buttonsPanel = new JPanel(new GridLayout(0, 2, 25, 25));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));
        buttonsPanel.setBackground(Color.decode("#F5EFE7"));

        // Create buttons
        String[] buttonLabels = {
                "Financial Goals", "Zakat Calculator",
                "Stock Market", "Reports & Insights",
                "Add Assets", "Manage Assets",
                "Risk Assessment"
        };

        for (String label : buttonLabels) {
            JButton button = createDashboardButton(label);
            buttonsPanel.add(button);

            // Add button actions
            switch (label) {
                case "Zakat Calculator":
                    button.addActionListener(e -> {
                        new ZakatCalculator(Home.this);
                        setVisible(false);
                    });
                    break;
                case "Stock Market":
                    button.addActionListener(e -> {
                        new StockAccountConnection(Home.this);
                        setVisible(false);
                    });
                    break;
                case "Reports & Insights":
                    button.addActionListener(e -> new ReportAndInsights(Home.this));
                    break;
                case "Add Assets":
                    button.addActionListener(e -> {
                        this.setVisible(false);
                        new AddAssets(this, assets).setVisible(true);
                    });
                    break;
                case "Manage Assets":
                    button.addActionListener(e -> {
                        this.setVisible(false);
                        new EditRemoveAssets(this, assets).setVisible(true);
                    });
                    break;
                case "Risk Assessment":
                    button.addActionListener(e -> {
                        if (!RiskAssessmentScreen.checkAssetsExist()) {
                            JOptionPane.showMessageDialog(Home.this,
                                    "Please add assets first to assess risk",
                                    "No Assets",
                                    JOptionPane.WARNING_MESSAGE);
                        } else {
                            riskAssessmentScreen.refreshData();
                            cardLayout.show(mainCardPanel, "RISK_ASSESSMENT");
                            setTitle("Investa - Risk Assessment");
                        }
                    });
                    break;
                case "Financial Goals":
                    button.addActionListener(e -> {
                        new FinancialGoals();
                        setVisible(false);
                    });
                    break;
            }
        }

        homePanel.add(buttonsPanel, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Color.decode("#F5EFE7"));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        JLabel footerLabel = new JLabel("© 2025 Investa. All rights reserved.");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerLabel.setForeground(Color.decode("#3E5879"));
        footerPanel.add(footerLabel);

        homePanel.add(footerPanel, BorderLayout.SOUTH);

        return homePanel;
    }

    /**
     * Creates a consistently styled dashboard button
     */
    private JButton createDashboardButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(Color.decode("#3E5879"));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#213555"), 1),
                BorderFactory.createEmptyBorder(12, 25, 12, 25)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.decode("#213555"));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.decode("#3E5879"));
            }
        });

        return button;
    }

    /**
     * Switches the view back to the home screen.
     * Updates the window title to reflect the current view.
     */
    public void showHomeView() {
        cardLayout.show(mainCardPanel, "HOME");
        setTitle("Investa - Dashboard");
    }

    /**
     * Updates the list of user's financial assets.
     *
     * @param updatedAssets The new list of assets to replace the current ones
     */
    public void updateAssets(ArrayList<Asset> updatedAssets) {
        this.assets = new ArrayList<>(updatedAssets);
    }
}
