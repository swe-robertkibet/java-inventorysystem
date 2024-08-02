package com.mycompany.inventory.royalmabati.ui;

import java.awt.datatransfer.StringSelection;

import com.mycompany.inventory.royalmabati.model.Stock;
import com.mycompany.inventory.royalmabati.service.StockService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StockPanel extends JPanel {
    private JTextField productNameField, quantityField, priceField, searchField;
    private JButton addButton, updateButton, deleteButton, searchButton, clearButton, copyIdButton;
    private JTable stockTable;
    private DefaultTableModel tableModel;
    private StockService stockService;

    public StockPanel() {
        stockService = new StockService();
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.add(new JLabel("Product Name:"));
        productNameField = new JTextField();
        formPanel.add(productNameField);
        formPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        formPanel.add(quantityField);
        formPanel.add(new JLabel("Price:"));
        priceField = new JTextField();
        formPanel.add(priceField);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");
        copyIdButton = new JButton("Copy ID");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(copyIdButton);

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
        String[] columnNames = { "ID", "Product Name", "Quantity", "Price" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };
        stockTable = new JTable(tableModel);
        stockTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(stockTable);
        add(scrollPane, BorderLayout.CENTER);

        // Add action listeners
        addButton.addActionListener(e -> addStock());
        updateButton.addActionListener(e -> updateStock());
        deleteButton.addActionListener(e -> deleteStock());
        clearButton.addActionListener(e -> clearFields());
        searchButton.addActionListener(e -> searchStock());
        copyIdButton.addActionListener(e -> copySelectedStockId());
        stockTable.getSelectionModel().addListSelectionListener(e -> fillFieldsFromSelection());

        // Load initial data
        loadStock();
    }

    private void loadStock() {
        tableModel.setRowCount(0);
        List<Stock> stocks = stockService.getAllStock();
        for (Stock stock : stocks) {
            tableModel.addRow(
                    new Object[] { stock.getId(), stock.getProductName(), stock.getQuantity(), stock.getPrice() });
        }
    }

    private void addStock() {
        Stock stock = new Stock(productNameField.getText(), Integer.parseInt(quantityField.getText()),
                Double.parseDouble(priceField.getText()));
        stockService.addStock(stock);
        loadStock();
        clearFields();
    }

    private void updateStock() {
        int selectedRow = stockTable.getSelectedRow();
        if (selectedRow != -1) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            Stock stock = new Stock(productNameField.getText(), Integer.parseInt(quantityField.getText()),
                    Double.parseDouble(priceField.getText()));
            stock.setId(id);
            stockService.updateStock(stock);
            loadStock();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a stock item to update.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteStock() {
        int selectedRow = stockTable.getSelectedRow();
        if (selectedRow != -1) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this stock item?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                stockService.deleteStock(id);
                loadStock();
                clearFields();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a stock item to delete.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearFields() {
        productNameField.setText("");
        quantityField.setText("");
        priceField.setText("");
        stockTable.clearSelection();
    }

    private void searchStock() {
        String searchTerm = searchField.getText().trim();
        if (!searchTerm.isEmpty()) {
            List<Stock> searchResults = stockService.searchStock(searchTerm);
            tableModel.setRowCount(0);
            for (Stock stock : searchResults) {
                tableModel.addRow(
                        new Object[] { stock.getId(), stock.getProductName(), stock.getQuantity(), stock.getPrice() });
            }
        } else {
            loadStock();
        }
    }

    private void fillFieldsFromSelection() {
        int selectedRow = stockTable.getSelectedRow();
        if (selectedRow != -1) {
            productNameField.setText((String) tableModel.getValueAt(selectedRow, 1));
            quantityField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 2)));
            priceField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 3)));
        }
    }

    private void copySelectedStockId() {
        int selectedRow = stockTable.getSelectedRow();
        if (selectedRow != -1) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            StringSelection stringSelection = new StringSelection(id);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
            JOptionPane.showMessageDialog(this, "Stock ID copied to clipboard!", "ID Copied",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a stock item to copy the ID.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}
