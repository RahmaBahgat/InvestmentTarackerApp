// PortfolioDashboard.java
package invest_wise;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PortfolioDashboard extends styles {
    public PortfolioDashboard(JFrame previousFrame, String platform) {
        window();
        setTitle("Portfolio - " + platform);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.decode("#f5efe7"));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel welcomeLabel = new JLabel("Connected to " + platform, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.decode("#3e5879"));

        JTextArea summary = new JTextArea();
        summary.setEditable(false);
        summary.setText("""
                🔹 Portfolio Value: $150,000
                🔹 Gains Today: +$1,200 (+0.8%)
                🔹 Top Holdings: AAPL, MSFT, NVDA
                """);

        JButton backButton = styledButton("Back");

        backButton.addActionListener(e -> {
            previousFrame.setVisible(true);
            dispose();
        });

        panel.add(welcomeLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(summary), BorderLayout.CENTER);
        panel.add(backButton, BorderLayout.SOUTH);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JButton styledButton(String text) {
        JButton button = new JButton(text);
        buttons(button); // From styles.java
        return button;
    }
}
