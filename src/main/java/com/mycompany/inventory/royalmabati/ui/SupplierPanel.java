package com.mycompany.inventory.royalmabati.ui;

import com.mycompany.inventory.royalmabati.model.Supplier;
import com.mycompany.inventory.royalmabati.service.SupplierService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SupplierPanel extends JPanel {
    private JTextField nameField, contactField, addressField, searchField;
    private JButton addButton, updateButton, deleteButton, searchButton;
    private JTable supplierTable;
    private DefaultTableModel tableModel;
    private SupplierService supplierService;

    public SupplierPanel() {
        supplierService = new SupplierService();
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);
        formPanel.add(new JLabel("Contact:"));
        contactField = new JTextField();
        formPanel.add(contactField);
        formPanel.add(new JLabel("Address:"));
        addressField = new JTextField();
        formPanel.add(addressField);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        searchButton = new JButton("Search");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);

        formPanel.add(buttonPanel);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        searchField = new JTextField(20);
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);

        // Combine form and search panels
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // Table Panel
        String[] columnNames = { "ID", "Name", "Contact", "Address" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };
        supplierTable = new JTable(tableModel);
        supplierTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(supplierTable);
        add(scrollPane, BorderLayout.CENTER);

        // Add action listeners
        addButton.addActionListener(e -> addSupplier());
        updateButton.addActionListener(e -> updateSupplier());
        deleteButton.addActionListener(e -> deleteSupplier());
        searchButton.addActionListener(e -> searchSuppliers());

        // Load initial data
        loadSuppliers();
    }

    private void loadSuppliers() {
        tableModel.setRowCount(0);
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        for (Supplier supplier : suppliers) {
            tableModel.addRow(
                    new Object[] { supplier.getId(), supplier.getName(), supplier.getContact(),
                            supplier.getAddress() });
        }
    }

    private void addSupplier() {
        Supplier supplier = new Supplier(nameField.getText(), contactField.getText(), addressField.getText());
        supplierService.addSupplier(supplier);
        loadSuppliers();
        clearFields();
    }

    private void updateSupplier() {
        int selectedRow = supplierTable.getSelectedRow();
        if (selectedRow != -1) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            Supplier supplier = new Supplier(nameField.getText(), contactField.getText(), addressField.getText());
            supplier.setId(id);
            supplierService.updateSupplier(supplier);
            loadSuppliers();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a supplier to update.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteSupplier() {
        int selectedRow = supplierTable.getSelectedRow();
        if (selectedRow != -1) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this supplier?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                supplierService.deleteSupplier(id);
                loadSuppliers();
                clearFields();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a supplier to delete.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void searchSuppliers() {
        String searchTerm = searchField.getText().trim();
        if (!searchTerm.isEmpty()) {
            List<Supplier> searchResults = supplierService.searchSuppliers(searchTerm);
            tableModel.setRowCount(0);
            for (Supplier supplier : searchResults) {
                tableModel.addRow(
                        new Object[] { supplier.getId(), supplier.getName(), supplier.getContact(),
                                supplier.getAddress() });
            }
        } else {
            loadSuppliers();
        }
    }

    private void clearFields() {
        nameField.setText("");
        contactField.setText("");
        addressField.setText("");
        supplierTable.clearSelection();
    }
}
