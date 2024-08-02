package com.mycompany.inventory.royalmabati.ui;

import com.mycompany.inventory.royalmabati.model.Client;
import com.mycompany.inventory.royalmabati.service.ClientService;

import javax.swing.*;
import java.awt.*;

public class ClientPanel extends JPanel {
    private JTextField nameField, contactField, addressField;
    private JButton addButton, updateButton, deleteButton;
    private JList<Client> clientList;
    private DefaultListModel<Client> listModel;
    private ClientService clientService;

    public ClientPanel() {
        clientService = new ClientService();
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
        clientList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(clientList);
        add(scrollPane, BorderLayout.CENTER);

        // Add action listeners
        addButton.addActionListener(e -> addClient());
        updateButton.addActionListener(e -> updateClient());
        deleteButton.addActionListener(e -> deleteClient());

        // Load initial data
        loadClients();
    }

    private void loadClients() {
        listModel.clear();
        for (Client client : clientService.getAllClients()) {
            listModel.addElement(client);
        }
    }

    private void addClient() {
        Client client = new Client(nameField.getText(), contactField.getText(), addressField.getText());
        clientService.addClient(client);
        loadClients();
        clearFields();
    }

    private void updateClient() {
        Client selectedClient = clientList.getSelectedValue();
        if (selectedClient != null) {
            selectedClient.setName(nameField.getText());
            selectedClient.setContact(contactField.getText());
            selectedClient.setAddress(addressField.getText());
            clientService.updateClient(selectedClient);
            loadClients();
            clearFields();
        }
    }

    private void deleteClient() {
        Client selectedClient = clientList.getSelectedValue();
        if (selectedClient != null) {
            clientService.deleteClient(selectedClient.getId());
            loadClients();
            clearFields();
        }
    }

    private void clearFields() {
        nameField.setText("");
        contactField.setText("");
        addressField.setText("");
    }
}