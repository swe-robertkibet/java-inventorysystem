package com.mycompany.inventory.royalmabati.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mycompany.inventory.royalmabati.config.DatabaseConfig;
import com.mycompany.inventory.royalmabati.model.Supplier;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {
    private final MongoCollection<Document> collection;

    public SupplierDAO() {
        this.collection = DatabaseConfig.getDatabase().getCollection("suppliers");
    }

    public void addSupplier(Supplier supplier) {
        Document doc = new Document("name", supplier.getName())
                .append("contact", supplier.getContact())
                .append("address", supplier.getAddress());
        collection.insertOne(doc);
    }

    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        for (Document doc : collection.find()) {
            suppliers.add(documentToSupplier(doc));
        }
        return suppliers;
    }

    public Supplier getSupplierById(String id) {
        Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        return doc != null ? documentToSupplier(doc) : null;
    }

    public boolean updateSupplier(Supplier supplier) {
        Document update = new Document("$set", new Document("name", supplier.getName())
                .append("contact", supplier.getContact())
                .append("address", supplier.getAddress()));
        UpdateResult result = collection.updateOne(Filters.eq("_id", new ObjectId(supplier.getId())), update);
        return result.getModifiedCount() > 0;
    }

    public boolean deleteSupplier(String id) {
        DeleteResult result = collection.deleteOne(Filters.eq("_id", new ObjectId(id)));
        return result.getDeletedCount() > 0;
    }

    private Supplier documentToSupplier(Document doc) {
        Supplier supplier = new Supplier();
        supplier.setId(doc.getObjectId("_id").toString());
        supplier.setName(doc.getString("name"));
        supplier.setContact(doc.getString("contact"));
        supplier.setAddress(doc.getString("address"));
        return supplier;
    }
}