package invest_wise;

import javax.swing.*;
import java.awt.*;

/**
 * Base class for styling and window management in the InvestWise application.
 * Provides common styling methods and a custom window frame with title bar.
 * All other windows in the application extend this class to maintain consistent styling.
 */
public class styles extends JFrame {
    /**
     * Initializes and configures the main application window.
     * Creates a custom title bar with application icon, title, and control buttons.
     * Sets up the window properties and styling.
     */
    public void window() {
        // Remove default window decorations for custom title bar
        setUndecorated(true);
        setLayout(new BorderLayout());
        setTitle("InvestWise");

        // === Custom Title Bar Setup ===
        JPanel titleBar = new JPanel();
        titleBar.setBackground(Color.decode("#3e5879")); // Dark blue background
        titleBar.setLayout(new BorderLayout());

        // Create title label with application icon
        JLabel titleLabel = new JLabel("  Simple Invest Wise App");
        titleLabel.setForeground(Color.decode("#f5efe7")); // Light beige text
        titleLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        
        // Load and scale the application icon
        ImageIcon rawIcon = new ImageIcon(getClass().getResource("/invest_wise/investing.png"));
        Image img = rawIcon.getImage();
        Image scaled = img.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        titleLabel.setIcon(new ImageIcon(scaled));
        titleLabel.setIconTextGap(10);

        // Create panel for title and icon
        JPanel titleTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleTextPanel.setOpaque(false);
        titleTextPanel.add(titleLabel);

        // Create window control buttons
        JButton closeButton = new JButton("❌");
        JButton minimizeButton = new JButton("➖");

        // Style the control buttons
        JButton[] titleButtons = {minimizeButton, closeButton};
        for (JButton b : titleButtons) {
            b.setFocusPainted(false);
            b.setForeground(Color.WHITE);
            b.setBackground(Color.decode("#213555")); // Darker blue for buttons
            b.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
            b.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 10));
            b.setOpaque(true);
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            
            // Add hover effects for buttons
            b.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    b.setBackground(Color.decode("#213555"));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    b.setBackground(Color.decode("#213555"));
                }
            });
        }

        // Add functionality to control buttons
        closeButton.addActionListener(e -> System.exit(0));
        minimizeButton.addActionListener(e -> setState(Frame.ICONIFIED));

        // Create panel for control buttons
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 2));
        controlPanel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        controlPanel.setOpaque(false);
        controlPanel.add(minimizeButton);
        controlPanel.add(closeButton);

        // Assemble the title bar
        titleBar.add(titleTextPanel, BorderLayout.WEST);
        titleBar.add(controlPanel, BorderLayout.EAST);

        // === Final Window Configuration ===
        add(titleBar, BorderLayout.NORTH);
        setBackground(Color.decode("#f5efe7")); // Light beige background
        setSize(700, 600);
        setLocationRelativeTo(null);  // Center window on screen
        setVisible(true);
    }

    /**
     * Applies consistent styling to buttons throughout the application.
     * Sets font, colors, size, and hover effects for a unified look.
     * 
     * @param b The button to style
     */
    public void buttons(JButton b) {
        b.setFont(new Font("Segue UI Emoji", Font.PLAIN, 16));
        b.setBackground(Color.decode("#3e5879")); // Dark blue background
        b.setForeground(Color.decode("#f5efe7")); // Light beige text
        b.setPreferredSize(new Dimension(200, 50));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}
