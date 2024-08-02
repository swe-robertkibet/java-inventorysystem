package com.mycompany.inventory.royalmabati.ui;

import com.mycompany.inventory.royalmabati.model.ManufacturingOrder;
import com.mycompany.inventory.royalmabati.service.ManufacturingService;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class ManufacturingPanel extends JPanel {
    private JTextField productIdField, quantityField, statusField;
    private JButton addButton, updateButton, deleteButton, completeButton;
    private JList<ManufacturingOrder> orderList;
    private DefaultListModel<ManufacturingOrder> listModel;
    private ManufacturingService manufacturingService;

    public ManufacturingPanel() {
        manufacturingService = new ManufacturingService();
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2));
        formPanel.add(new JLabel("Product ID:"));
        productIdField = new JTextField();
        formPanel.add(productIdField);
        formPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        formPanel.add(quantityField);
        formPanel.add(new JLabel("Status:"));
        statusField = new JTextField();
        formPanel.add(statusField);

        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        completeButton = new JButton("Complete Order");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(completeButton);

        formPanel.add(buttonPanel);

        add(formPanel, BorderLayout.NORTH);

        // List Panel
        listModel = new DefaultListModel<>();
        orderList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(orderList);
        add(scrollPane, BorderLayout.CENTER);

        // Add action listeners
        addButton.addActionListener(e -> addOrder());
        updateButton.addActionListener(e -> updateOrder());
        deleteButton.addActionListener(e -> deleteOrder());
        completeButton.addActionListener(e -> completeOrder());

        // Load initial data
        loadOrders();
    }

    private void loadOrders() {
        listModel.clear();
        for (ManufacturingOrder order : manufacturingService.getAllManufacturingOrders()) {
            listModel.addElement(order);
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
        ManufacturingOrder selectedOrder = orderList.getSelectedValue();
        if (selectedOrder != null) {
            selectedOrder.setProductId(productIdField.getText());
            selectedOrder.setQuantity(Integer.parseInt(quantityField.getText()));
            selectedOrder.setStatus(statusField.getText());
            manufacturingService.updateManufacturingOrder(selectedOrder);
            loadOrders();
            clearFields();
        }
    }

    private void deleteOrder() {
        ManufacturingOrder selectedOrder = orderList.getSelectedValue();
        if (selectedOrder != null) {
            manufacturingService.deleteManufacturingOrder(selectedOrder.getId());
            loadOrders();
            clearFields();
        }
    }

    private void completeOrder() {
        ManufacturingOrder selectedOrder = orderList.getSelectedValue();
        if (selectedOrder != null) {
            manufacturingService.completeManufacturingOrder(selectedOrder.getId());
            loadOrders();
            clearFields();
        }
    }

    private void clearFields() {
        productIdField.setText("");
        quantityField.setText("");
        statusField.setText("");
    }
}