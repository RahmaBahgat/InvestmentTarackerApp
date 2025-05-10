package invest_wise;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Home class represents the main dashboard of the InvestWise application.
 * It extends the styles class to inherit common styling methods and provides
 * a central interface for accessing various financial management features.
 */
public class Home extends styles {
    // List to store all user assets, accessible throughout the application
    private ArrayList<Asset> assets = new ArrayList<>();

    /**
     * Constructor for the Home class.
     * Initializes the main window and sets up the user interface components.
     */
    public Home() {
        window(); // Initialize the main window from parent class

        // === Main Panel Setup ===
        // Create the main container panel with GridBagLayout for flexible component arrangement
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.decode("#f5efe7")); // Set light beige background

        // Configure GridBagConstraints for component positioning
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // === Navigation Buttons Creation ===
        gbc.gridy = 2;
        // Create buttons for different financial management features
        JButton financialButton = new JButton("Financial goals");
        JButton zakatButton = new JButton("Zakat Calculator");
        JButton stockButton = new JButton("Connect Stock Market");
        JButton reportButton = new JButton("Report & Insights");
        JButton addAssetsButton = new JButton("Add Assets");
        JButton editRemoveButton = new JButton("Edit/Remove Assets");
        JButton riskAssessmentButton = new JButton("Risk Assessment");

        // Apply consistent styling to all buttons using inherited method
        buttons(financialButton);
        buttons(zakatButton);
        buttons(stockButton);
        buttons(reportButton);
        buttons(addAssetsButton);
        buttons(editRemoveButton);
        buttons(riskAssessmentButton);

        // === Button Action Handlers ===
        // Zakat Calculator button handler
        zakatButton.addActionListener(e -> {
            new ZakatCalculator(Home.this);
            setVisible(false);
        });

        // Stock Market Connection button handler
        stockButton.addActionListener(e -> {
            new StockAccountConnection(Home.this);
            setVisible(false);
        });

        // Reports and Insights button handler
        reportButton.addActionListener(e -> new ReportAndInsights(Home.this));

        // Add Assets button handler
        addAssetsButton.addActionListener(e -> {
            this.setVisible(false);
            new AddAssets(this, assets).setVisible(true);  // Pass both Home and assets
        });

        // Edit/Remove Assets button handler
        editRemoveButton.addActionListener(e -> {
            this.setVisible(false);
            new EditRemoveAssets(this, assets).setVisible(true);
        });

        // Risk Assessment button handler with validation
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

        // Financial Goals button handler
        financialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FinancialGoals();
                setVisible(false);
            }
        });

        // === Button Panel Setup ===
        // Create a panel to hold all navigation buttons
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setPreferredSize(new Dimension(600, 400));
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Center alignment with spacing
        buttonsPanel.setBackground(Color.decode("#f5efe7")); // Match main panel background

        // Add all buttons to the panel
        buttonsPanel.add(financialButton);
        buttonsPanel.add(zakatButton);
        buttonsPanel.add(stockButton);
        buttonsPanel.add(reportButton);
        buttonsPanel.add(addAssetsButton);
        buttonsPanel.add(editRemoveButton);
        buttonsPanel.add(riskAssessmentButton);

        // Add the buttons panel to the main panel
        gbc.gridy = 2;
        mainPanel.add(buttonsPanel, gbc);

        // === Final Window Setup ===
        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Updates the assets list with new data.
     * This method is called when assets are modified in other screens.
     * @param updatedAssets The new list of assets to replace the current list
     */
    public void updateAssets(ArrayList<Asset> updatedAssets) {
        this.assets = new ArrayList<>(updatedAssets);
    }
}
