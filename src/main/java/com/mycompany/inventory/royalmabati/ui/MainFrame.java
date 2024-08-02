package com.mycompany.inventory.royalmabati.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;

    public MainFrame() {
        setTitle("Royal Mabati Mills Inventory System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Clients", new ClientPanel());
        tabbedPane.addTab("Stock", new StockPanel());
        tabbedPane.addTab("Suppliers", new SupplierPanel());
        tabbedPane.addTab("Sales", new SalesPanel());
        tabbedPane.addTab("Manufacturing", new ManufacturingPanel());
        tabbedPane.addTab("Reports", new ReportPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}