package invest_wise;

import javax.swing.*;
import java.awt.*;

/**
 * Base class that provides common styling and window functionality for all frames in the application.
 * Extends JFrame and implements a custom title bar with minimize and close buttons.
 */
public class styles extends JFrame{
    /**
     * Initializes and configures the main window with a custom title bar.
     * Sets up the window properties, custom title bar with controls, and basic styling.
     */
    public void window () {

        setUndecorated(true);
        setLayout(new BorderLayout());
        setTitle("InvestWise");

        // === Custom Title Bar ===
        JPanel titleBar = new JPanel();
        titleBar.setBackground(Color.decode("#3e5879"));
        titleBar.setLayout(new BorderLayout());

        // Title + Icon on the left
        JLabel titleLabel = new JLabel("  Simple Invest Wise App");
        titleLabel.setForeground(Color.decode("#f5efe7"));
        titleLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        ImageIcon rawIcon = new ImageIcon(getClass().getResource("/invest_wise/investing.png"));
        Image img = rawIcon.getImage();
        Image scaled = img.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        titleLabel.setIcon(new ImageIcon(scaled));
        titleLabel.setIconTextGap(10);

        JPanel titleTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleTextPanel.setOpaque(false);
        titleTextPanel.add(titleLabel);

        // Buttons: Minimize, Maximize, Close
        JButton closeButton = new JButton("❌");
        JButton minimizeButton = new JButton("➖");

        // Style buttons
        JButton[] titleButtons = {minimizeButton, closeButton};
        for (JButton b : titleButtons) {
            b.setFocusPainted(false);
            b.setForeground(Color.WHITE);
            b.setBackground(Color.decode("#213555"));
            b.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
            b.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 10));
            b.setOpaque(true);
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            b.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    b.setBackground(Color.decode("#213555"));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    b.setBackground(Color.decode("#213555"));
                }
            });
        }

        // Button actions
        closeButton.addActionListener(e -> System.exit(0));
        minimizeButton.addActionListener(e -> setState(Frame.ICONIFIED));

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 2));
        controlPanel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        controlPanel.setOpaque(false);
        controlPanel.add(minimizeButton);
        controlPanel.add(closeButton);

        // Add to titleBar
        titleBar.add(titleTextPanel, BorderLayout.WEST);
        titleBar.add(controlPanel, BorderLayout.EAST);
        // === Final Frame Setup ===
        add(titleBar, BorderLayout.NORTH);
        setBackground(Color.decode("#f5efe7"));
        setSize(700, 600);
        setLocationRelativeTo(null);  // Center on screen
        setVisible(true);
    }

    /**
     * Applies consistent styling to a button.
     * Sets font, colors, size, and hover effects for a uniform look across the application.
     *
     * @param b The button to style
     */
    public void buttons(JButton b){
        b.setFont(new Font("Segue UI Emoji", Font.PLAIN, 16));
        b.setBackground(Color.decode("#3e5879"));
        b.setForeground(Color.decode("#f5efe7"));
        b.setPreferredSize(new Dimension(200, 50));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /**
     * Creates a styled button with consistent formatting.
     *
     * @param text The text to display on the button
     * @return A configured JButton with standard styling
     */
    public JButton styledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        button.setBackground(Color.decode("#3e5879"));
        button.setForeground(Color.decode("#f5efe7"));
        button.setPreferredSize(new Dimension(200, 50));
        button.setMaximumSize(new Dimension(200, 50));
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

}
