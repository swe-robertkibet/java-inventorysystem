package com.mycompany.inventory.royalmabati.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private JMenuBar menuBar;

    public MainFrame() {
        setTitle("Royal Mabati Mills Inventory System");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen

        // Set custom look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create menu bar
        createMenuBar();

        // Create tabbed pane with custom colors
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(0xF0F0F0)); // Light grey background
        tabbedPane.setForeground(new Color(0x333333)); // Dark grey text
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));

        // Add tabs
        tabbedPane.addTab("Clients", new ImageIcon("icons/clients.png"), new ClientPanel(), "Manage Clients");
        tabbedPane.addTab("Stock", new ImageIcon("icons/stock.png"), new StockPanel(), "Manage Stock");
        tabbedPane.addTab("Suppliers", new ImageIcon("icons/suppliers.png"), new SupplierPanel(), "Manage Suppliers");
        tabbedPane.addTab("Sales", new ImageIcon("icons/sales.png"), new SalesPanel(), "Manage Sales");
        tabbedPane.addTab("Manufacturing", new ImageIcon("icons/manufacturing.png"), new ManufacturingPanel(),
                "Manage Manufacturing");
        tabbedPane.addTab("Reports", new ImageIcon("icons/reports.png"), new ReportPanel(), "Generate Reports");

        add(tabbedPane, BorderLayout.CENTER);

        // Add status bar with custom color
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBackground(new Color(0xE0E0E0)); // Light grey background
        statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
        statusBar.add(new JLabel("Ready"));
        add(statusBar, BorderLayout.SOUTH);
    }

    private void createMenuBar() {
        menuBar = new JMenuBar();
        menuBar.setBackground(new Color(0x333333)); // Dark grey background
        menuBar.setForeground(Color.WHITE); // White text
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));

        JMenu fileMenu = new JMenu("File");
        fileMenu.setForeground(Color.WHITE);
        fileMenu.setFont(new Font("Arial", Font.BOLD, 14));
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setForeground(Color.WHITE);
        helpMenu.setFont(new Font("Arial", Font.BOLD, 14));
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Royal Mabati Mills Inventory System\nVersion 1.0", "About", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
