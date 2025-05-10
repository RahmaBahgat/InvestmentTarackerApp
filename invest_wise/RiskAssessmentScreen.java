package invest_wise;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import org.jfree.chart.*;
import org.jfree.data.general.DefaultPieDataset;

public class RiskAssessmentScreen extends styles {
    private JPanel riskPanel;
    private JLabel riskScoreLabel;
    private JButton backButton;
    private Home home;
    private ArrayList<Asset> assets;
    private static final String ASSETS_FILE = "invest_wise/assets.txt";

    public RiskAssessmentScreen(Home home) {
        this.home = home;

        // First check if assets file exists and has content
        if (!checkAssetsExist()) {
            JOptionPane.showMessageDialog(home,  // Use home as parent
                    "Please add assets first to assess risk",
                    "No Assets",
                    JOptionPane.WARNING_MESSAGE);
            return;  // Exit constructor without showing window
        }

        // Only proceed if assets exist
        this.assets = loadAssetsFromFile();
        initializeUI();
    }

    private boolean checkAssetsExist() {
        File file = new File(ASSETS_FILE);
        if (!file.exists() || file.length() == 0) {
            return false;
        }

        // Quick check if file has valid content
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            return br.readLine() != null;
        } catch (IOException e) {
            return false;
        }
    }

    private void initializeUI() {
        window();
        setTitle("InvestWise - Risk Assessment");
        createMainInterface();
    }

    private void createMainInterface() {
        riskPanel = new JPanel(new BorderLayout(10, 10));
        riskPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        riskPanel.setBackground(Color.decode("#f5efe7"));

        // Risk score display
        int riskScore = calculateRiskScore();
        riskScoreLabel = new JLabel("Your Risk Score: " + riskScore + "/100", SwingConstants.CENTER);
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
        backButton = new JButton("Back");
        buttons(backButton);
        backButton.addActionListener(e -> goBack());

        // Layout
        riskPanel.add(riskScoreLabel, BorderLayout.NORTH);
        riskPanel.add(chartPanel, BorderLayout.CENTER);
        riskPanel.add(new JScrollPane(tipsArea), BorderLayout.SOUTH);
        riskPanel.add(backButton, BorderLayout.PAGE_END);

        add(riskPanel);
    }

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
            JOptionPane.showMessageDialog(this,
                    "Error reading assets file",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return loadedAssets;
    }

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

    private JPanel createRiskChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Asset asset : assets) {
            dataset.setValue(asset.type, asset.value);
        }
        JFreeChart chart = ChartFactory.createPieChart(
                "Asset Distribution", dataset, true, true, false);
        return new ChartPanel(chart);
    }

    private String getMitigationTips(int score) {
        if (score > 70) return "High Risk! Consider adding bonds/gold (20-30%).";
        else if (score > 40) return "Moderate Risk. Rebalance with stable assets.";
        else return "Low Risk. Maintain current allocation.";
    }

    private double getTotalPortfolioValue() {
        return assets.stream().mapToDouble(a -> a.value).sum();
    }

    private Color getRiskColor(int score) {
        if (score > 70) return Color.RED;
        else if (score > 40) return Color.ORANGE;
        else return Color.GREEN;
    }

    private void goBack() {
        this.setVisible(false);
        home.setVisible(true);
    }
}
