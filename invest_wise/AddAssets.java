package invest_wise;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.text.DecimalFormat;

/**
 * Provides functionality for adding and managing financial assets.
 * Allows users to input asset details and maintains a list of assets.
 */
public class AddAssets extends styles {
    /** Combo box for selecting asset type */
    private JComboBox<String> assetTypeCombo;
    /** Text field for asset name */
    private JTextField assetNameField;
    /** Text field for asset value */
    private JTextField assetValueField;
    /** Button for adding new assets */
    private JButton addButton;
    /** Button for clearing all assets */
    private JButton clearButton;
    /** Button for returning to previous screen */
    private JButton backButton;
    /** Text area for displaying asset list */
    private JTextArea assetListArea;
    /** Label for displaying messages */
    private JLabel messageLabel;
    /** Label for displaying total value */
    private JLabel totalValueLabel;
    /** List of user's assets */
    private ArrayList<Asset> assets;
    /** Formatter for currency values */
    private DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");
    /** Reference to the main Home window */
    private Home home;
    /** File path for storing asset data */
    private static final String ASSETS_FILE = "invest_wise/assets.txt";

    /**
     * Constructs the Add Assets window with input fields and asset management functionality.
     *
     * @param home Reference to the main Home window
     * @param assets List of existing assets
     */
    public AddAssets(Home home, ArrayList<Asset> assets) {
        this.window();
        this.home = home;
        this.assets = assets;
        loadAssetsFromFile(); // Load assets from file on startup

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.decode("#f5efe7"));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // === FORM PANEL ===
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 15, 15));  // 3 rows, 2 columns
        formPanel.setBackground(Color.decode("#f5efe7"));

        Font labelFont = new Font("Segoe UI Emoji", Font.PLAIN, 16);
        Color labelColor = Color.decode("#3e5879");

        // Create styled labels
        JLabel typeLabel = new JLabel("Select Asset Type:");
        typeLabel.setFont(labelFont);
        typeLabel.setForeground(labelColor);

        JLabel nameLabel = new JLabel("Enter Asset Name:");
        nameLabel.setFont(labelFont);
        nameLabel.setForeground(labelColor);

        JLabel valueLabel = new JLabel("Enter Asset Value:");
        valueLabel.setFont(labelFont);
        valueLabel.setForeground(labelColor);

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

        // Add label-field pairs
        formPanel.add(typeLabel);
        formPanel.add(assetTypeCombo);
        formPanel.add(nameLabel);
        formPanel.add(assetNameField);
        formPanel.add(valueLabel);
        formPanel.add(assetValueField);

        // === FORM WRAPPER ===
        JPanel formWrapper = new JPanel(new GridBagLayout());
        formWrapper.setBackground(Color.decode("#f5efe7"));
        formWrapper.add(formPanel);

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
        centerPanel.add(messagePanel, BorderLayout.SOUTH);

        // === BUTTON PANEL ===
        addButton = styledButton("Add Asset");
        clearButton = styledButton("Clear All");
        backButton = styledButton("Back");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.decode("#f5efe7"));
        buttonPanel.add(backButton);
        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);

        // === MAIN PANEL LAYOUT ===
        mainPanel.add(centerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH); // Buttons at the bottom

        add(mainPanel, BorderLayout.CENTER);

        // === EVENT LISTENERS ===
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

    /**
     * Loads assets from the assets file.
     * Clears existing assets before loading new ones.
     */
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

    /**
     * Saves the current list of assets to the assets file.
     * Writes each asset in CSV format.
     */
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

    /**
     * Creates a styled label with specified text, font, and color.
     *
     * @param panel The panel to add the label to
     * @param text The text to display
     * @param font The font to use
     * @param color The text color
     */
    private void addStyledLabel(JPanel panel, String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        panel.add(label);
    }

    /**
     * Adds a new asset to the list based on user input.
     * Validates input fields and updates the asset list display.
     */
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

    /**
     * Updates the asset list display with current assets.
     * Calculates and displays the total value of all assets.
     */
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

    /**
     * Clears all assets from the list.
     * Updates the display and saves changes to file.
     */
    private void clearAssets() {
        assets.clear();
        updateAssetList();
        showMessage("All assets cleared.", Color.BLUE);
    }

    /**
     * Displays a message with specified text and color.
     *
     * @param text The message to display
     * @param color The color to use for the message
     */
    private void showMessage(String text, Color color) {
        messageLabel.setText(text);
        messageLabel.setForeground(color);
    }

    /**
     * Returns to the previous screen.
     * Updates the home window with current assets.
     */
    private void goBack() {
        this.setVisible(false);
        home.setVisible(true);
    }
}
