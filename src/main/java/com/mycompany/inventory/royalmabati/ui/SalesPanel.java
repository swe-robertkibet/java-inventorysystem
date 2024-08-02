package com.mycompany.inventory.royalmabati.ui;

import com.mycompany.inventory.royalmabati.model.Sale;
import com.mycompany.inventory.royalmabati.service.SalesService;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class SalesPanel extends JPanel {
    private JTextField clientIdField, productIdField, quantityField, totalPriceField;
    private JButton addButton, updateButton, deleteButton;
    private JList<Sale> salesList;
    private DefaultListModel<Sale> listModel;
    private SalesService salesService;

    public SalesPanel() {
        salesService = new SalesService();
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2));
        formPanel.add(new JLabel("Client ID:"));
        clientIdField = new JTextField();
        formPanel.add(clientIdField);
        formPanel.add(new JLabel("Product ID:"));
        productIdField = new JTextField();
        formPanel.add(productIdField);
        formPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        formPanel.add(quantityField);
        formPanel.add(new JLabel("Total Price:"));
        totalPriceField = new JTextField();
        formPanel.add(totalPriceField);

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
        salesList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(salesList);
        add(scrollPane, BorderLayout.CENTER);

        // Add action listeners
        addButton.addActionListener(e -> addSale());
        updateButton.addActionListener(e -> updateSale());
        deleteButton.addActionListener(e -> deleteSale());

        // Load initial data
        loadSales();
    }

    private void loadSales() {
        listModel.clear();
        for (Sale sale : salesService.getAllSales()) {
            listModel.addElement(sale);
        }
    }

    private void addSale() {
        Sale sale = new Sale(
                clientIdField.getText(),
                productIdField.getText(),
                Integer.parseInt(quantityField.getText()),
                Double.parseDouble(totalPriceField.getText()),
                new Date());
        salesService.addSale(sale);
        loadSales();
        clearFields();
    }

    private void updateSale() {
        Sale selectedSale = salesList.getSelectedValue();
        if (selectedSale != null) {
            selectedSale.setClientId(clientIdField.getText());
            selectedSale.setProductId(productIdField.getText());
            selectedSale.setQuantity(Integer.parseInt(quantityField.getText()));
            selectedSale.setTotalPrice(Double.parseDouble(totalPriceField.getText()));
            salesService.updateSale(selectedSale);
            loadSales();
            clearFields();
        }
    }

    private void deleteSale() {
        Sale selectedSale = salesList.getSelectedValue();
        if (selectedSale != null) {
            salesService.deleteSale(selectedSale.getId());
            loadSales();
            clearFields();
        }
    }

    private void clearFields() {
        clientIdField.setText("");
        productIdField.setText("");
        quantityField.setText("");
        totalPriceField.setText("");
    }
}