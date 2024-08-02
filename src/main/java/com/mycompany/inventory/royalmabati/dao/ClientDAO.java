package com.mycompany.inventory.royalmabati.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mycompany.inventory.royalmabati.config.DatabaseConfig;
import com.mycompany.inventory.royalmabati.model.Client;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class ClientDAO {
    private final MongoCollection<Document> collection;

    public ClientDAO() {
        this.collection = DatabaseConfig.getDatabase().getCollection("clients");
    }

    public void addClient(Client client) {
        Document doc = new Document("name", client.getName())
                .append("contact", client.getContact())
                .append("address", client.getAddress());
        collection.insertOne(doc);
    }

    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        for (Document doc : collection.find()) {
            clients.add(documentToClient(doc));
        }
        return clients;
    }

    public Client getClientById(String id) {
        Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        return doc != null ? documentToClient(doc) : null;
    }

    public boolean updateClient(Client client) {
        Document update = new Document("$set", new Document("name", client.getName())
                .append("contact", client.getContact())
                .append("address", client.getAddress()));
        UpdateResult result = collection.updateOne(Filters.eq("_id", new ObjectId(client.getId())), update);
        return result.getModifiedCount() > 0;
    }

    public boolean deleteClient(String id) {
        DeleteResult result = collection.deleteOne(Filters.eq("_id", new ObjectId(id)));
        return result.getDeletedCount() > 0;
    }

    private Client documentToClient(Document doc) {
        Client client = new Client();
        client.setId(doc.getObjectId("_id").toString());
        client.setName(doc.getString("name"));
        client.setContact(doc.getString("contact"));
        client.setAddress(doc.getString("address"));
        return client;
    }
}