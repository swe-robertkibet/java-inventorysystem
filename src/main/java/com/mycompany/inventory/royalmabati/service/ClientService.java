package com.mycompany.inventory.royalmabati.service;

import com.mycompany.inventory.royalmabati.dao.ClientDAO;
import com.mycompany.inventory.royalmabati.model.Client;

import java.util.List;

public class ClientService {
    private final ClientDAO clientDAO;

    public ClientService() {
        this.clientDAO = new ClientDAO();
    }

    public void addClient(Client client) {
        clientDAO.addClient(client);
    }

    public List<Client> getAllClients() {
        return clientDAO.getAllClients();
    }

    public Client getClientById(String id) {
        return clientDAO.getClientById(id);
    }

    public boolean updateClient(Client client) {
        return clientDAO.updateClient(client);
    }

    public boolean deleteClient(String id) {
        return clientDAO.deleteClient(id);
    }

    public List<Client> searchClients(String searchTerm) {
        // Implement search logic here
        // This is a placeholder implementation
        List<Client> allClients = getAllClients();
        return allClients.stream()
                .filter(client -> client.getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        client.getContact().contains(searchTerm) ||
                        client.getAddress().toLowerCase().contains(searchTerm.toLowerCase()))
                .toList();
    }
}