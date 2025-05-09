package invest_wise;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.text.DecimalFormat;

public class AddAssets extends styles {
    private JComboBox<String> assetTypeCombo;
    private JTextField assetNameField;
    private JTextField assetValueField;
    private JButton addButton;
    private JButton clearButton;
    private JButton backButton;
    private JTextArea assetListArea;
    private JLabel messageLabel;
    private JLabel totalValueLabel;
    private JPanel buttonPanel;

    private ArrayList<Asset> assets;
    private DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");
    private Home home;

    public AddAssets(Home home, ArrayList<Asset> assets) {
        this.home = home;
        this.assets = assets;
        window();
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(6, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        formPanel.setBackground(Color.decode("#f5efe7"));

        // Initialize components
        assetTypeCombo = new JComboBox<>(new String[]{"Stocks", "Real Estate", "Crypto", "Gold", "Bonds", "Mutual Funds"});
        assetNameField = new JTextField();
        assetValueField = new JTextField();

        // Set input verification for asset value field
        assetValueField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String text = ((JTextField) input).getText();
                if (text.isEmpty()) return true;
                try {
                    Double.parseDouble(text);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        });

        // Add tooltips
        assetTypeCombo.setToolTipText("Select the type of asset you want to add");
        assetNameField.setToolTipText("Enter the name or identifier of the asset");
        assetValueField.setToolTipText("Enter the current value of the asset");

        formPanel.add(new JLabel("Select Asset Type:"));
        formPanel.add(assetTypeCombo);
        formPanel.add(new JLabel("Enter Asset Name:"));
        formPanel.add(assetNameField);
        formPanel.add(new JLabel("Enter Asset Value:"));
        formPanel.add(assetValueField);

        // Initialize buttons
        addButton = new JButton("Add Asset");
        clearButton = new JButton("Clear All");
        backButton = new JButton("Back");

        // Style buttons
        buttons(addButton);
        buttons(clearButton);
        buttons(backButton);

        // Button panel
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.decode("#f5efe7"));
        buttonPanel.add(backButton);
        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);

        messageLabel = new JLabel("");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));

        totalValueLabel = new JLabel("Total Value: $0.00");
        totalValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalValueLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        totalValueLabel.setForeground(Color.decode("#3e5879"));

        assetListArea = new JTextArea(8, 30);
        assetListArea.setEditable(false);
        assetListArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(assetListArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Your Assets"));

        // Add action listeners
        addButton.addActionListener(e -> addAsset());
        clearButton.addActionListener(e -> clearAssets());
        backButton.addActionListener(e -> goBack());

        // Add key listener for Enter key
        assetValueField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    addAsset();
                }
            }
        });

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(Color.decode("#f5efe7"));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        centerPanel.add(buttonPanel, BorderLayout.NORTH);
        centerPanel.add(messageLabel, BorderLayout.CENTER);
        centerPanel.add(totalValueLabel, BorderLayout.SOUTH);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(Color.decode("#f5efe7"));
        southPanel.add(centerPanel, BorderLayout.NORTH);
        southPanel.add(scrollPane, BorderLayout.CENTER);

        add(formPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void goBack() {
        this.setVisible(false);
    }

    private void addAsset() {
        String type = (String) assetTypeCombo.getSelectedItem();
        String name = assetNameField.getText().trim();
        String valueStr = assetValueField.getText().trim();

        if (name.isEmpty() || valueStr.isEmpty()) {
            showMessage("Please fill in all required fields.", Color.RED);
            return;
        }

        try {
            double value = Double.parseDouble(valueStr);
            if (value <= 0) {
                showMessage("Asset value must be positive.", Color.RED);
                return;
            }

            Asset newAsset = new Asset(type, name, value);
            assets.add(newAsset);
            updateAssetList();
            showMessage("Asset added successfully!", new Color(0, 128, 0));

            assetNameField.setText("");
            assetValueField.setText("");
            assetNameField.requestFocus();
        } catch (NumberFormatException ex) {
            showMessage("Asset value must be a valid number.", Color.RED);
        }
    }

    private void showMessage(String text, Color color) {
        messageLabel.setText(text);
        messageLabel.setForeground(color);
    }

    private void updateAssetList() {
        StringBuilder sb = new StringBuilder();
        double totalValue = 0;

        for (Asset asset : assets) {
            sb.append(asset.toString()).append("\n");
            totalValue += asset.value;
        }

        assetListArea.setText(sb.toString());
        totalValueLabel.setText("Total Value: " + currencyFormat.format(totalValue));
    }

    private void clearAssets() {
        assets.clear();
        updateAssetList();
        showMessage("All assets cleared.", Color.BLUE);
    }

//    private void goBack() {
//        // Implement navigation back to previous screen
//        showMessage("Back button clicked - implement navigation", Color.BLUE);
//    }

//    static class Asset {
//        String type;
//        String name;
//        double value;
//
//        public Asset(String type, String name, double value) {
//            this.type = type;
//            this.name = name;
//            this.value = value;
//        }
//
//        @Override
//        public String toString() {
//            return String.format("%-12s %-20s %10s",
//                    type + ":",
//                    name,
//                    "$" + String.format("%,.2f", value));
//        }
//    }
}
