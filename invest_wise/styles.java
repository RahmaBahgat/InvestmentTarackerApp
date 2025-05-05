package invest_wise;

import javax.swing.*;
import java.awt.*;

public class styles extends JFrame{
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
        setSize(500, 800);
        setLocationRelativeTo(null);  // Center on screen
        setVisible(true);
    }
}
