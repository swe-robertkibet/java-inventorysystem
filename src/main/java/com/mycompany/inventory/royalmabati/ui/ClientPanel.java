package com.mycompany.inventory.royalmabati.ui;

import com.mycompany.inventory.royalmabati.model.Client;
import com.mycompany.inventory.royalmabati.service.ClientService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.util.List;

public class ClientPanel extends JPanel {
    private JTextField nameField, contactField, addressField, searchField;
    private JButton addButton, updateButton, deleteButton, searchButton, clearButton, copyIdButton;
    private JTable clientTable;
    private DefaultTableModel tableModel;
    private ClientService clientService;

    public ClientPanel() {
        clientService = new ClientService();
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
        String[] columnNames = { "ID", "Name", "Contact", "Address" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };
        clientTable = new JTable(tableModel);
        clientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(clientTable);
        add(scrollPane, BorderLayout.CENTER);

        // Add action listeners
        addButton.addActionListener(e -> addClient());
        updateButton.addActionListener(e -> updateClient());
        deleteButton.addActionListener(e -> deleteClient());
        clearButton.addActionListener(e -> clearFields());
        searchButton.addActionListener(e -> searchClients());
        copyIdButton.addActionListener(e -> copySelectedClientId());
        clientTable.getSelectionModel().addListSelectionListener(e -> fillFieldsFromSelection());

        // Load initial data
        loadClients();
    }

    private void loadClients() {
        tableModel.setRowCount(0);
        List<Client> clients = clientService.getAllClients();
        for (Client client : clients) {
            tableModel.addRow(
                    new Object[] { client.getId(), client.getName(), client.getContact(), client.getAddress() });
        }
    }

    private void addClient() {
        Client client = new Client(nameField.getText(), contactField.getText(), addressField.getText());
        clientService.addClient(client);
        loadClients();
        clearFields();
    }

    private void updateClient() {
        int selectedRow = clientTable.getSelectedRow();
        if (selectedRow != -1) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            Client client = new Client(nameField.getText(), contactField.getText(), addressField.getText());
            client.setId(id);
            clientService.updateClient(client);
            loadClients();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a client to update.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteClient() {
        int selectedRow = clientTable.getSelectedRow();
        if (selectedRow != -1) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this client?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                clientService.deleteClient(id);
                loadClients();
                clearFields();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a client to delete.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearFields() {
        nameField.setText("");
        contactField.setText("");
        addressField.setText("");
        clientTable.clearSelection();
    }

    private void searchClients() {
        String searchTerm = searchField.getText().trim();
        if (!searchTerm.isEmpty()) {
            List<Client> searchResults = clientService.searchClients(searchTerm);
            tableModel.setRowCount(0);
            for (Client client : searchResults) {
                tableModel.addRow(
                        new Object[] { client.getId(), client.getName(), client.getContact(), client.getAddress() });
            }
        } else {
            loadClients();
        }
    }

    private void fillFieldsFromSelection() {
        int selectedRow = clientTable.getSelectedRow();
        if (selectedRow != -1) {
            nameField.setText((String) tableModel.getValueAt(selectedRow, 1));
            contactField.setText((String) tableModel.getValueAt(selectedRow, 2));
            addressField.setText((String) tableModel.getValueAt(selectedRow, 3));
        }
    }

    private void copySelectedClientId() {
        int selectedRow = clientTable.getSelectedRow();
        if (selectedRow != -1) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            StringSelection stringSelection = new StringSelection(id);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
            JOptionPane.showMessageDialog(this, "Client ID copied to clipboard!", "ID Copied",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a client to copy the ID.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}