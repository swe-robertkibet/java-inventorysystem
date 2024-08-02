package com.mycompany.inventory.royalmabati.ui;

import com.mycompany.inventory.royalmabati.model.ManufacturingOrder;
import com.mycompany.inventory.royalmabati.service.ManufacturingService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.Date;
import java.util.List;

public class ManufacturingPanel extends JPanel {
    private JTextField productIdField, quantityField, statusField, searchField;
    private JButton addButton, updateButton, deleteButton, searchButton, clearButton, copyIdButton, completeButton;
    private JTable orderTable;
    private DefaultTableModel tableModel;
    private ManufacturingService manufacturingService;

    public ManufacturingPanel() {
        manufacturingService = new ManufacturingService();
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.add(new JLabel("Product ID:"));
        productIdField = new JTextField();
        formPanel.add(productIdField);
        formPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        formPanel.add(quantityField);
        formPanel.add(new JLabel("Status:"));
        statusField = new JTextField();
        formPanel.add(statusField);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");
        copyIdButton = new JButton("Copy ID");
        completeButton = new JButton("Complete Order");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(copyIdButton);
        buttonPanel.add(completeButton);

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
        String[] columnNames = { "ID", "Product ID", "Quantity", "Start Date", "End Date", "Status" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };
        orderTable = new JTable(tableModel);
        orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(orderTable);
        add(scrollPane, BorderLayout.CENTER);

        // Add action listeners
        addButton.addActionListener(e -> addOrder());
        updateButton.addActionListener(e -> updateOrder());
        deleteButton.addActionListener(e -> deleteOrder());
        clearButton.addActionListener(e -> clearFields());
        searchButton.addActionListener(e -> searchOrders());
        copyIdButton.addActionListener(e -> copySelectedOrderId());
        completeButton.addActionListener(e -> completeOrder());
        orderTable.getSelectionModel().addListSelectionListener(e -> fillFieldsFromSelection());

        // Load initial data
        loadOrders();
    }

    private void loadOrders() {
        tableModel.setRowCount(0);
        List<ManufacturingOrder> orders = manufacturingService.getAllManufacturingOrders();
        for (ManufacturingOrder order : orders) {
            tableModel.addRow(new Object[] {
                    order.getId(), order.getProductId(), order.getQuantity(),
                    order.getStartDate(), order.getEndDate(), order.getStatus()
            });
        }
    }

    private void addOrder() {
        ManufacturingOrder order = new ManufacturingOrder(
                productIdField.getText(),
                Integer.parseInt(quantityField.getText()),
                new Date(),
                null,
                statusField.getText());
        manufacturingService.addManufacturingOrder(order);
        loadOrders();
        clearFields();
    }

    private void updateOrder() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow != -1) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            ManufacturingOrder order = new ManufacturingOrder(
                    productIdField.getText(),
                    Integer.parseInt(quantityField.getText()),
                    (Date) tableModel.getValueAt(selectedRow, 3),
                    (Date) tableModel.getValueAt(selectedRow, 4),
                    statusField.getText());
            order.setId(id);
            manufacturingService.updateManufacturingOrder(order);
            loadOrders();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Please select an order to update.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteOrder() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow != -1) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this order?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                manufacturingService.deleteManufacturingOrder(id);
                loadOrders();
                clearFields();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an order to delete.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearFields() {
        productIdField.setText("");
        quantityField.setText("");
        statusField.setText("");
        orderTable.clearSelection();
    }

    private void searchOrders() {
        String searchTerm = searchField.getText().trim();
        if (!searchTerm.isEmpty()) {
            List<ManufacturingOrder> searchResults = manufacturingService.searchOrders(searchTerm);
            tableModel.setRowCount(0);
            for (ManufacturingOrder order : searchResults) {
                tableModel.addRow(new Object[] {
                        order.getId(), order.getProductId(), order.getQuantity(),
                        order.getStartDate(), order.getEndDate(), order.getStatus()
                });
            }
        } else {
            loadOrders();
        }
    }

    private void fillFieldsFromSelection() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow != -1) {
            productIdField.setText((String) tableModel.getValueAt(selectedRow, 1));
            quantityField.setText(tableModel.getValueAt(selectedRow, 2).toString());
            statusField.setText((String) tableModel.getValueAt(selectedRow, 5));
        }
    }

    private void copySelectedOrderId() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow != -1) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            StringSelection stringSelection = new StringSelection(id);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
            JOptionPane.showMessageDialog(this, "Order ID copied to clipboard!", "ID Copied",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Please select an order to copy the ID.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void completeOrder() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow != -1) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            manufacturingService.completeManufacturingOrder(id);
            loadOrders();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Please select an order to complete.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}