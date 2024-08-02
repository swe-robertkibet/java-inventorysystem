package com.mycompany.inventory.royalmabati.ui;

import com.mycompany.inventory.royalmabati.model.Stock;
import com.mycompany.inventory.royalmabati.service.StockService;

import javax.swing.*;
import java.awt.*;

public class StockPanel extends JPanel {
    private JTextField productNameField, quantityField, priceField;
    private JButton addButton, updateButton, deleteButton;
    private JList<Stock> stockList;
    private DefaultListModel<Stock> listModel;
    private StockService stockService;

    public StockPanel() {
        stockService = new StockService();
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2));
        formPanel.add(new JLabel("Product Name:"));
        productNameField = new JTextField();
        formPanel.add(productNameField);
        formPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        formPanel.add(quantityField);
        formPanel.add(new JLabel("Price:"));
        priceField = new JTextField();
        formPanel.add(priceField);

        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        formPanel.add(buttonPanel);

        add(formPanel, BorderLayout.NORTH);

        // List Panel
        listModel = new DefaultListModel<>();
        stockList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(stockList);
        add(scrollPane, BorderLayout.CENTER);

        // Add action listeners
        addButton.addActionListener(e -> addStock());
        updateButton.addActionListener(e -> updateStock());
        deleteButton.addActionListener(e -> deleteStock());

        // Load initial data
        loadStock();
    }

    private void loadStock() {
        listModel.clear();
        for (Stock stock : stockService.getAllStock()) {
            listModel.addElement(stock);
        }
    }

    private void addStock() {
        Stock stock = new Stock(
                productNameField.getText(),
                Integer.parseInt(quantityField.getText()),
                Double.parseDouble(priceField.getText()));
        stockService.addStock(stock);
        loadStock();
        clearFields();
    }

    private void updateStock() {
        Stock selectedStock = stockList.getSelectedValue();
        if (selectedStock != null) {
            selectedStock.setProductName(productNameField.getText());
            selectedStock.setQuantity(Integer.parseInt(quantityField.getText()));
            selectedStock.setPrice(Double.parseDouble(priceField.getText()));
            stockService.updateStock(selectedStock);
            loadStock();
            clearFields();
        }
    }

    private void deleteStock() {
        Stock selectedStock = stockList.getSelectedValue();
        if (selectedStock != null) {
            stockService.deleteStock(selectedStock.getId());
            loadStock();
            clearFields();
        }
    }

    private void clearFields() {
        productNameField.setText("");
        quantityField.setText("");
        priceField.setText("");
    }
}