package invest_wise;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class EditRemoveAssets extends styles {
    private JList<Asset> assetList;
    private DefaultListModel<Asset> listModel;
    private JButton editButton;
    private JButton removeButton;
    private JButton backButton;
    private JLabel messageLabel;
    private ArrayList<Asset> assets;
    private Home home;
    private static final String ASSETS_FILE = "invest_wise/assets.txt";

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

    private void updateListModel() {
        listModel.clear();
        for (Asset asset : assets) {
            listModel.addElement(asset);
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

    private void showMessage(String text, Color color) {
        messageLabel.setText(text);
        messageLabel.setForeground(color);
    }

    private void goBack() {
        this.setVisible(false);
        home.updateAssets(assets); // Pass the updated list back to Home
        home.setVisible(true);
    }

    private class AssetListRenderer extends DefaultListCellRenderer {
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
