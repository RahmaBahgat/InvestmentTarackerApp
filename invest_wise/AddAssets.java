package invest_wise;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
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
    private ArrayList<Asset> assets;
    private DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");
    private Home home;
    private static final String ASSETS_FILE = "invest_wise/assets.txt";

    public AddAssets(Home home, ArrayList<Asset> assets) {
        this.home = home;
        this.assets = assets;
        this.window();
        loadAssetsFromFile(); // Load assets from file on startup

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.decode("#f5efe7"));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // === FORM PANEL ===
        JPanel formPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        formPanel.setBackground(Color.decode("#f5efe7"));

        Font labelFont = new Font("Segoe UI Emoji", Font.PLAIN, 16);
        Color labelColor = Color.decode("#3e5879");

        // Form components
        assetTypeCombo = new JComboBox<>(new String[]{"Stocks", "Real Estate", "Crypto", "Gold", "Bonds", "Mutual Funds"});
        assetNameField = new JTextField();
        assetValueField = new JTextField();

        // Input verification
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

        // Add form components with styled labels
        addStyledLabel(formPanel, "Select Asset Type:", labelFont, labelColor);
        formPanel.add(assetTypeCombo);
        addStyledLabel(formPanel, "Enter Asset Name:", labelFont, labelColor);
        formPanel.add(assetNameField);
        addStyledLabel(formPanel, "Enter Asset Value:", labelFont, labelColor);
        formPanel.add(assetValueField);

        // === FORM WRAPPER ===
        JPanel formWrapper = new JPanel(new GridBagLayout());
        formWrapper.setBackground(Color.decode("#f5efe7"));
        formWrapper.add(formPanel);

        // === BUTTON PANEL ===
        addButton = createStyledButton("Add Asset");
        clearButton = createStyledButton("Clear All");
        backButton = createStyledButton("Back");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.decode("#f5efe7"));
        buttonPanel.add(backButton);
        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);

        // === ASSET LIST AREA ===
        assetListArea = new JTextArea();
        assetListArea.setEditable(false);
        assetListArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        assetListArea.setLineWrap(true);
        assetListArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(assetListArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Your Assets"));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // === MESSAGE PANEL ===
        messageLabel = new JLabel(" ");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));

        totalValueLabel = new JLabel("Total Value: $0.00");
        totalValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalValueLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        totalValueLabel.setForeground(Color.decode("#3e5879"));

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBackground(Color.decode("#f5efe7"));
        messagePanel.add(messageLabel, BorderLayout.NORTH);
        messagePanel.add(totalValueLabel, BorderLayout.SOUTH);

        // === CENTER CONTENT PANEL ===
        JPanel centerPanel = new JPanel(new BorderLayout(10, 20));
        centerPanel.setBackground(Color.decode("#f5efe7"));
        centerPanel.add(formWrapper, BorderLayout.NORTH);
        centerPanel.add(buttonPanel, BorderLayout.CENTER);
        centerPanel.add(messagePanel, BorderLayout.SOUTH);

        // === MAIN LAYOUT ===
        mainPanel.add(centerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        // Event listeners
        addButton.addActionListener(e -> {
            addAsset();
            saveAssetsToFile();
        });

        clearButton.addActionListener(e -> {
            clearAssets();
            saveAssetsToFile();
        });

        backButton.addActionListener(e -> goBack());

        assetValueField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    addAsset();
                    saveAssetsToFile();
                }
            }
        });

        updateAssetList(); // Update UI with loaded assets
    }

    private void loadAssetsFromFile() {
        File file = new File(ASSETS_FILE);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            assets.clear(); // Clear existing assets before loading
            while ((line = br.readLine()) != null) {
                Asset asset = Asset.fromCSV(line);
                if (asset != null) {
                    assets.add(asset);
                }
            }
        } catch (IOException e) {
            showMessage("Error loading assets from file.", Color.RED);
        }
    }

    private void saveAssetsToFile() {
        try (FileWriter fw = new FileWriter(ASSETS_FILE);
             BufferedWriter bw = new BufferedWriter(fw)) {
            for (Asset asset : assets) {
                bw.write(asset.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            showMessage("Error saving assets to file.", Color.RED);
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        button.setBackground(Color.decode("#3e5879"));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    private void addStyledLabel(JPanel panel, String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        panel.add(label);
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

    private void updateAssetList() {
        StringBuilder sb = new StringBuilder();
        double totalValue = 0;

        if (assets.isEmpty()) {
            sb.append("No assets found.");
        } else {
            for (Asset asset : assets) {
                sb.append("⇛ Type: ").append(asset.type)
                        .append(", Name: ").append(asset.name)
                        .append(", Value: ").append(currencyFormat.format(asset.value))
                        .append("\n");
                totalValue += asset.value;
            }
        }

        assetListArea.setText(sb.toString());
        totalValueLabel.setText("Total Value: " + currencyFormat.format(totalValue));
    }

    private void clearAssets() {
        assets.clear();
        updateAssetList();
        showMessage("All assets cleared.", Color.BLUE);
    }

    private void showMessage(String text, Color color) {
        messageLabel.setText(text);
        messageLabel.setForeground(color);
    }

    private void goBack() {
        this.setVisible(false);
        home.setVisible(true);
    }
}
