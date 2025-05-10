package invest_wise;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import org.jfree.chart.*;
import org.jfree.data.general.DefaultPieDataset;

/**
 * Provides risk assessment functionality for the user's financial portfolio.
 * Analyzes assets and generates risk assessment reports with visualizations.
 */
public class RiskAssessmentScreen {
    /** Main panel containing risk assessment interface */
    private JPanel riskPanel;
    /** Reference to the main Home window */
    private Home home;
    /** Helper class for styling components */
    private styles styleHelper;
    /** List of user's assets for risk analysis */
    private ArrayList<Asset> assets;
    /** File path for storing asset data */
    private static final String ASSETS_FILE = "invest_wise/assets.txt";

    /**
     * Constructs the risk assessment screen with necessary components.
     *
     * @param home Reference to the main Home window
     * @param styleHelper Helper class for styling components
     */
    public RiskAssessmentScreen(Home home, styles styleHelper) {
        this.home = home;
        this.styleHelper = styleHelper;
        this.assets = loadAssetsFromFile();
        initializeUI();
    }

    /**
     * Checks if there are any assets available for risk assessment.
     *
     * @return true if assets exist and are not empty, false otherwise
     */
    public static boolean checkAssetsExist() {
        File file = new File(ASSETS_FILE);
        if (!file.exists() || file.length() == 0) {
            return false;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            return br.readLine() != null;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Gets the main risk assessment panel.
     *
     * @return The JPanel containing the risk assessment interface
     */
    public JPanel getRiskPanel() {
        return riskPanel;
    }

    /**
     * Refreshes the risk assessment data and updates the display.
     * Reloads assets and recreates the interface.
     */
    public void refreshData() {
        this.assets = loadAssetsFromFile();
        riskPanel.removeAll();
        createMainInterface();
        riskPanel.revalidate();
        riskPanel.repaint();
    }

    /**
     * Initializes the user interface components for risk assessment.
     * Sets up the main panel and creates the interface layout.
     */
    private void initializeUI() {
        riskPanel = new JPanel(new BorderLayout(10, 10));
        riskPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        riskPanel.setBackground(Color.decode("#f5efe7"));
        createMainInterface();
    }

    /**
     * Creates the main interface components for risk assessment display.
     * Includes risk score, chart, tips, and navigation controls.
     */
    private void createMainInterface() {
        // Risk score display
        int riskScore = calculateRiskScore();
        JLabel riskScoreLabel = new JLabel("Your Risk Score: " + riskScore + "/100", SwingConstants.CENTER);
        riskScoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        riskScoreLabel.setForeground(getRiskColor(riskScore));

        // Chart panel
        JPanel chartPanel = createRiskChart();

        // Tips area
        JTextArea tipsArea = new JTextArea(getMitigationTips(riskScore));
        tipsArea.setEditable(false);
        tipsArea.setLineWrap(true);
        tipsArea.setWrapStyleWord(true);

        // Back button
        JButton backButton = new JButton("Back");
        styleHelper.buttons(backButton); // Now using the instance method
        backButton.addActionListener(e -> home.showHomeView());

        // Layout
        riskPanel.add(riskScoreLabel, BorderLayout.NORTH);
        riskPanel.add(chartPanel, BorderLayout.CENTER);
        riskPanel.add(new JScrollPane(tipsArea), BorderLayout.SOUTH);
        riskPanel.add(backButton, BorderLayout.PAGE_END);
    }

    /**
     * Loads asset data from the assets file.
     *
     * @return ArrayList of assets loaded from the file
     */
    private ArrayList<Asset> loadAssetsFromFile() {
        ArrayList<Asset> loadedAssets = new ArrayList<>();
        File file = new File(ASSETS_FILE);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                Asset asset = Asset.fromCSV(line);
                if (asset != null) {
                    loadedAssets.add(asset);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error reading assets file",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return loadedAssets;
    }

    /**
     * Calculates the overall risk score for the portfolio.
     * Weights different asset types based on their risk levels.
     *
     * @return Risk score from 0 to 100
     */
    private int calculateRiskScore() {
        if (assets.isEmpty()) return 0;

        double totalRisk = 0;
        for (Asset asset : assets) {
            double assetRisk = switch (asset.type) {
                case "Crypto" -> 0.9;
                case "Stocks" -> 0.7;
                case "Real Estate" -> 0.4;
                case "Gold" -> 0.3;
                case "Bonds" -> 0.2;
                default -> 0.5;
            };
            totalRisk += assetRisk * asset.value;
        }
        return (int) ((totalRisk / getTotalPortfolioValue()) * 100);
    }

    /**
     * Creates a pie chart showing the distribution of assets.
     *
     * @return JPanel containing the asset distribution chart
     */
    private JPanel createRiskChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Asset asset : assets) {
            dataset.setValue(asset.type, asset.value);
        }
        JFreeChart chart = ChartFactory.createPieChart(
                "Asset Distribution", dataset, true, true, false);
        return new ChartPanel(chart);
    }

    /**
     * Generates risk mitigation tips based on the risk score.
     *
     * @param score The calculated risk score
     * @return String containing risk mitigation advice
     */
    private String getMitigationTips(int score) {
        if (score > 70) return "High Risk! Consider adding bonds/gold (20-30%).";
        else if (score > 40) return "Moderate Risk. Rebalance with stable assets.";
        else return "Low Risk. Maintain current allocation.";
    }

    /**
     * Calculates the total value of all assets in the portfolio.
     *
     * @return The sum of all asset values
     */
    private double getTotalPortfolioValue() {
        return assets.stream().mapToDouble(a -> a.value).sum();
    }

    /**
     * Determines the color to display based on the risk score.
     *
     * @param score The calculated risk score
     * @return Color representing the risk level (Red for high, Orange for medium, Green for low)
     */
    private Color getRiskColor(int score) {
        if (score > 70) return Color.RED;
        else if (score > 40) return Color.ORANGE;
        else return Color.GREEN;
    }
}
