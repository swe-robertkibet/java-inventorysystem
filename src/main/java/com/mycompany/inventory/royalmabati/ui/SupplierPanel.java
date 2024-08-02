package com.mycompany.inventory.royalmabati.ui;

import com.mycompany.inventory.royalmabati.model.Supplier;
import com.mycompany.inventory.royalmabati.service.SupplierService;

import javax.swing.*;
import java.awt.*;

public class SupplierPanel extends JPanel {
    private JTextField nameField, contactField, addressField;
    private JButton addButton, updateButton, deleteButton;
    private JList<Supplier> supplierList;
    private DefaultListModel<Supplier> listModel;
    private SupplierService supplierService;

    public SupplierPanel() {
        supplierService = new SupplierService();
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2));
        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);
        formPanel.add(new JLabel("Contact:"));
        contactField = new JTextField();
        formPanel.add(contactField);
        formPanel.add(new JLabel("Address:"));
        addressField = new JTextField();
        formPanel.add(addressField);

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
        supplierList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(supplierList);
        add(scrollPane, BorderLayout.CENTER);

        // Add action listeners
        addButton.addActionListener(e -> addSupplier());
        updateButton.addActionListener(e -> updateSupplier());
        deleteButton.addActionListener(e -> deleteSupplier());

        // Load initial data
        loadSuppliers();
    }

    private void loadSuppliers() {
        listModel.clear();
        for (Supplier supplier : supplierService.getAllSuppliers()) {
            listModel.addElement(supplier);
        }
    }

    private void addSupplier() {
        Supplier supplier = new Supplier(nameField.getText(), contactField.getText(), addressField.getText());
        supplierService.addSupplier(supplier);
        loadSuppliers();
        clearFields();
    }

    private void updateSupplier() {
        Supplier selectedSupplier = supplierList.getSelectedValue();
        if (selectedSupplier != null) {
            selectedSupplier.setName(nameField.getText());
            selectedSupplier.setContact(contactField.getText());
            selectedSupplier.setAddress(addressField.getText());
            supplierService.updateSupplier(selectedSupplier);
            loadSuppliers();
            clearFields();
        }
    }

    private void deleteSupplier() {
        Supplier selectedSupplier = supplierList.getSelectedValue();
        if (selectedSupplier != null) {
            supplierService.deleteSupplier(selectedSupplier.getId());
            loadSuppliers();
            clearFields();
        }
    }

    private void clearFields() {
        nameField.setText("");
        contactField.setText("");
        addressField.setText("");
    }
}