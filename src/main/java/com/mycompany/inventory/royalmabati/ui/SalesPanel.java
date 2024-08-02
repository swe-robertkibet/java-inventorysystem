package com.mycompany.inventory.royalmabati.ui;

import com.mycompany.inventory.royalmabati.model.Sale;
import com.mycompany.inventory.royalmabati.service.SalesService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class SalesPanel extends JPanel {
    private JTextField clientIdField, productIdField, quantityField, totalPriceField, searchField;
    private JButton addButton, updateButton, deleteButton, searchButton, clearButton;
    private JTable salesTable;
    private DefaultTableModel tableModel;
    private SalesService salesService;

    public SalesPanel() {
        salesService = new SalesService();
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        formPanel.add(buttonPanel);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Combine form and search panels
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // Table Panel
        String[] columnNames = { "ID", "Client ID", "Product ID", "Quantity", "Total Price", "Date" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };
        salesTable = new JTable(tableModel);
        salesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(salesTable);
        add(scrollPane, BorderLayout.CENTER);

        // Add action listeners
        addButton.addActionListener(e -> addSale());
        updateButton.addActionListener(e -> updateSale());
        deleteButton.addActionListener(e -> deleteSale());
        clearButton.addActionListener(e -> clearFields());
        searchButton.addActionListener(e -> searchSales());
        salesTable.getSelectionModel().addListSelectionListener(e -> fillFieldsFromSelection());

        // Load initial data
        loadSales();
    }

    private void loadSales() {
        tableModel.setRowCount(0);
        List<Sale> sales = salesService.getAllSales();
        for (Sale sale : sales) {
            tableModel.addRow(new Object[] {
                    sale.getId(), sale.getClientId(), sale.getProductId(), sale.getQuantity(),
                    sale.getTotalPrice(), sale.getDate()
            });
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
        int selectedRow = salesTable.getSelectedRow();
        if (selectedRow != -1) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            Sale sale = new Sale(
                    clientIdField.getText(),
                    productIdField.getText(),
                    Integer.parseInt(quantityField.getText()),
                    Double.parseDouble(totalPriceField.getText()),
                    (Date) tableModel.getValueAt(selectedRow, 5));
            sale.setId(id);
            salesService.updateSale(sale);
            loadSales();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a sale to update.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteSale() {
        int selectedRow = salesTable.getSelectedRow();
        if (selectedRow != -1) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this sale?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                salesService.deleteSale(id);
                loadSales();
                clearFields();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a sale to delete.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearFields() {
        clientIdField.setText("");
        productIdField.setText("");
        quantityField.setText("");
        totalPriceField.setText("");
        salesTable.clearSelection();
    }

    private void searchSales() {
        String searchTerm = searchField.getText().trim();
        if (!searchTerm.isEmpty()) {
            List<Sale> searchResults = salesService.searchSales(searchTerm);
            tableModel.setRowCount(0);
            for (Sale sale : searchResults) {
                tableModel.addRow(new Object[] {
                        sale.getId(), sale.getClientId(), sale.getProductId(), sale.getQuantity(),
                        sale.getTotalPrice(), sale.getDate()
                });
            }
        } else {
            loadSales();
        }
    }

    private void fillFieldsFromSelection() {
        int selectedRow = salesTable.getSelectedRow();
        if (selectedRow != -1) {
            clientIdField.setText((String) tableModel.getValueAt(selectedRow, 1));
            productIdField.setText((String) tableModel.getValueAt(selectedRow, 2));
            quantityField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 3)));
            totalPriceField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 4)));
        }
    }
}
