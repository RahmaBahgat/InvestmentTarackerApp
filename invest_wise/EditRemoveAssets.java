package invest_wise;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Provides functionality for editing and removing financial assets.
 * Allows users to modify asset details and remove assets from their portfolio.
 */
public class EditRemoveAssets extends styles {
    /** List component for displaying assets */
    private JList<Asset> assetList;
    /** Model for managing the asset list data */
    private DefaultListModel<Asset> listModel;
    /** Button for editing selected asset */
    private JButton editButton;
    /** Button for removing selected asset */
    private JButton removeButton;
    /** Button for returning to previous screen */
    private JButton backButton;
    /** Label for displaying messages */
    private JLabel messageLabel;
    /** List of user's assets */
    private ArrayList<Asset> assets;
    /** Reference to the main Home window */
    private Home home;
    /** File path for storing asset data */
    private static final String ASSETS_FILE = "invest_wise/assets.txt";

    /**
     * Constructs the edit/remove assets window.
     * Initializes the UI components and loads existing assets.
     *
     * @param home Reference to the main Home window
     * @param assets List of existing assets
     */
    public EditRemoveAssets(Home home, ArrayList<Asset> assets) {
        this.home = home;
        this.assets = new ArrayList<>(assets);

        // Initialize listModel
        listModel = new DefaultListModel<>();

        window();
        setTitle("InvestWise - Edit/Remove Assets");

        // load assets from file
        loadAssetsFromFile();

        // Initialize components
        assetList = new JList<>(listModel);
        assetList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        assetList.setCellRenderer(new AssetListRenderer());
        assetList.setFont(new Font("Monospaced", Font.PLAIN, 12));

        editButton = new JButton("Edit Selected");
        removeButton = new JButton("Remove Selected");
        backButton = new JButton("Back");
        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setBorder(new EmptyBorder(10, 0, 10, 0));

        // Style components
        buttons(editButton);
        buttons(removeButton);
        buttons(backButton);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Add action listeners
        editButton.addActionListener(e -> {
            editAsset();
            saveAssetsToFile();
        });

        removeButton.addActionListener(e -> {
            removeAsset();
            saveAssetsToFile();
        });

        backButton.addActionListener(e -> goBack());

        // Layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.decode("#f5efe7"));

        JScrollPane scrollPane = new JScrollPane(assetList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Your Assets"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.decode("#f5efe7"));
        buttonPanel.add(backButton);
        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(messageLabel, BorderLayout.NORTH);

        add(mainPanel);
    }

    /**
     * Loads assets from the assets file.
     * Updates the list model with loaded assets.
     */
    private void loadAssetsFromFile() {
        File file = new File(ASSETS_FILE);
        if (!file.exists()) {
            // If file doesn't exist, just use the assets passed in constructor
            updateListModel();
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            assets.clear(); // Clear existing assets before loading
            while ((line = br.readLine()) != null) {
                Asset asset = Asset.fromCSV(line);
                if (asset != null) {
                    assets.add(asset);
                }
            }
            updateListModel();
        } catch (IOException e) {
            showMessage("Error loading assets from file.", Color.RED);
        }
    }

    /**
     * Updates the list model with current assets.
     * Refreshes the display of assets in the list.
     */
    private void updateListModel() {
        listModel.clear();
        for (Asset asset : assets) {
            listModel.addElement(asset);
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
     * Opens a dialog to edit the selected asset.
     * Allows modification of asset type, name, and value.
     */
    private void editAsset() {
        Asset selected = assetList.getSelectedValue();
        if (selected == null) {
            showMessage("Please select an asset to edit.", Color.RED);
            return;
        }

        // Create edit dialog
        JDialog editDialog = new JDialog(this, "Edit Asset", true);
        editDialog.setLayout(new GridLayout(4, 2, 10, 10));
        editDialog.setSize(400, 200);

        JComboBox<String> typeCombo = new JComboBox<>(
                new String[]{"Stocks", "Real Estate", "Crypto", "Gold", "Bonds", "Mutual Funds"});
        typeCombo.setSelectedItem(selected.type);

        JTextField nameField = new JTextField(selected.name);
        JTextField valueField = new JTextField(String.valueOf(selected.value));

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        // Add components to dialog
        editDialog.add(new JLabel("Type:"));
        editDialog.add(typeCombo);
        editDialog.add(new JLabel("Name:"));
        editDialog.add(nameField);
        editDialog.add(new JLabel("Value:"));
        editDialog.add(valueField);
        editDialog.add(saveButton);
        editDialog.add(cancelButton);

        // Add action listeners
        saveButton.addActionListener(e -> {
            try {
                String newName = nameField.getText().trim();
                if (newName.isEmpty()) {
                    showMessage("Name cannot be empty", Color.RED);
                    return;
                }

                double newValue = Double.parseDouble(valueField.getText().trim());
                if (newValue <= 0) {
                    showMessage("Value must be positive", Color.RED);
                    return;
                }

                selected.type = (String) typeCombo.getSelectedItem();
                selected.name = newName;
                selected.value = newValue;

                updateListModel();
                showMessage("Asset updated successfully!", new Color(0, 128, 0));
                editDialog.dispose();
            } catch (NumberFormatException ex) {
                showMessage("Please enter a valid number for value", Color.RED);
            }
        });

        cancelButton.addActionListener(e -> editDialog.dispose());

        editDialog.setLocationRelativeTo(this);
        editDialog.setVisible(true);
    }

    /**
     * Removes the selected asset from the list.
     * Prompts for confirmation before removal.
     */
    private void removeAsset() {
        Asset selected = assetList.getSelectedValue();
        if (selected == null) {
            showMessage("Please select an asset to remove.", Color.RED);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove this asset?\n" + selected,
                "Confirm Removal", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            assets.remove(selected);
            updateListModel();
            showMessage("Asset removed successfully!", new Color(0, 128, 0));
        }
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
        home.updateAssets(assets); // Pass the updated list back to Home
        home.setVisible(true);
    }

    /**
     * Custom renderer for displaying assets in the list.
     * Formats each asset entry with consistent styling.
     */
    private class AssetListRenderer extends DefaultListCellRenderer {
        /**
         * Renders an asset in the list with custom formatting.
         *
         * @param list The list being rendered
         * @param value The asset to render
         * @param index The index of the asset
         * @param isSelected Whether the asset is selected
         * @param cellHasFocus Whether the cell has focus
         * @return A component configured to display the asset
         */
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Asset) {
                Asset asset = (Asset) value;
                setText(asset.toString());
            }
            return this;
        }
    }
}
