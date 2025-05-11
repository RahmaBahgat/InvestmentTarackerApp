package invest_wise;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileOutputStream;

/**
 * Provides functionality for calculating Zakat (Islamic almsgiving) based on various assets.
 * Allows users to input different types of assets and generates Zakat calculations.
 */
public class ZakatCalculator extends styles {
    /** Text area for displaying calculation results */
    private JTextArea resultArea;
    /** Input fields for different types of assets */
    private JTextField goldField, cashField, stockField, realEstateField, otherField;

    /**
     * Constructs the Zakat calculator window with input fields and calculation buttons.
     *
     * @param previousFrame The frame to return to when closing this window
     */
    public ZakatCalculator(JFrame previousFrame) {
        window();
        setTitle("Zakat Calculator");

        // === Input Fields ===
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBackground(Color.decode("#f5efe7"));

        Font labelFont = new Font("Segoe UI Emoji", Font.PLAIN, 16);
        Color labelColor = Color.decode("#3e5879");

        inputPanel.add(styledLabel("Gold Value (EGP):", labelFont, labelColor));
        goldField = styledTextField(labelFont, new Dimension(200, 30));
        inputPanel.add(goldField);

        inputPanel.add(styledLabel("Cash & Savings (EGP):", labelFont, labelColor));
        cashField = styledTextField(labelFont, new Dimension(200, 30));
        inputPanel.add(cashField);

        inputPanel.add(styledLabel("Stocks Value (EGP):", labelFont, labelColor));
        stockField = styledTextField(labelFont, new Dimension(200, 30));
        inputPanel.add(stockField);

        inputPanel.add(styledLabel("Real Estate Investment (EGP):", labelFont, labelColor));
        realEstateField = styledTextField(labelFont, new Dimension(200, 30));
        inputPanel.add(realEstateField);

        inputPanel.add(styledLabel("Other Assets (EGP):", labelFont, labelColor));
        otherField = styledTextField(labelFont, new Dimension(200, 30));
        inputPanel.add(otherField);

        // Buttons
        JButton calcButton = styledButton("Calculate Zakat");
        JButton pdfButton = styledButton("Download PDF");
        JButton backButton = styledButton("Back");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.decode("#f5efe7"));
        buttonPanel.add(calcButton);
        buttonPanel.add(pdfButton);
        buttonPanel.add(backButton);

        // Result Area
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Zakat Summary"));

        // Create a wrapper panel with padding at the top
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBorder(BorderFactory.createEmptyBorder(30, 20, 20, 20)); // top, left, bottom, right
        contentWrapper.setBackground(Color.decode("#f5efe7"));

        contentWrapper.add(inputPanel, BorderLayout.NORTH);
        contentWrapper.add(scrollPane, BorderLayout.CENTER);
        contentWrapper.add(buttonPanel, BorderLayout.SOUTH);

        add(contentWrapper, BorderLayout.CENTER); // Use CENTER instead of NORTH


        // Actions
        calcButton.addActionListener(this::calculateZakat);
        pdfButton.addActionListener(this::generatePDFReport);
        backButton.addActionListener(e -> {
            previousFrame.setVisible(true);
            dispose();
        });

        setVisible(true);
    }

    /**
     * Calculates Zakat based on the input asset values.
     * Computes total assets and applies the 2.5% Zakat rate.
     *
     * @param e The action event that triggered the calculation
     */
    private void calculateZakat(ActionEvent e) {
        try {
            double gold = parseInput(goldField.getText());
            double cash = parseInput(cashField.getText());
            double stocks = parseInput(stockField.getText());
            double realEstate = parseInput(realEstateField.getText());
            double others = parseInput(otherField.getText());

            double totalAssets = gold + cash + stocks + realEstate + others;
            double zakatDue = totalAssets * 0.025;

            resultArea.setText(String.format("""
                    🔹 Gold: EGP %.2f
                    🔹 Cash/Savings: EGP %.2f
                    🔹 Stocks: EGP %.2f
                    🔹 Real Estate: EGP %.2f
                    🔹 Other: EGP %.2f
                    ===============================
                    ✅ Total Eligible Assets: EGP %.2f
                    💰 Zakat Due (2.5%%): EGP %.2f
                    """,
                    gold, cash, stocks, realEstate, others, totalAssets, zakatDue));

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers only.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Generates a PDF report of the Zakat calculation.
     * Creates a formatted document with the calculation results.
     *
     * @param e The action event that triggered the PDF generation
     */
    private void generatePDFReport(ActionEvent e) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("Zakat_Report.pdf"));
            document.open();

            com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font bodyFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12);

            document.add(new Paragraph("Zakat Calculation Report", titleFont));
            document.add(new Paragraph(" ")); // Spacer

            String[] lines = resultArea.getText().split("\n");
            for (String line : lines) {
                document.add(new Paragraph(line, bodyFont));
            }

            document.close();
            JOptionPane.showMessageDialog(this, "PDF Report saved as 'Zakat_Report.pdf'");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error generating PDF.");
            ex.printStackTrace();
        }
    }

    /**
     * Parses a string input into a double value.
     * Returns 0 if the input is empty.
     *
     * @param text The text to parse
     * @return The parsed double value, or 0 if empty
     */
    private double parseInput(String text) {
        return text.isEmpty() ? 0 : Double.parseDouble(text);
    }

    /**
     * Creates a styled label with specified text, font, and color.
     *
     * @param text The text to display
     * @param font The font to use
     * @param color The text color
     * @return A configured JLabel with the specified styling
     */
    private JLabel styledLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    /**
     * Creates a styled text field with specified font and size.
     *
     * @param font The font to use
     * @param size The preferred size of the field
     * @return A configured JTextField with the specified styling
     */
    private JTextField styledTextField(Font font, Dimension size) {
        JTextField field = new JTextField();
        field.setFont(font);
        field.setPreferredSize(size);
        return field;
    }
}
