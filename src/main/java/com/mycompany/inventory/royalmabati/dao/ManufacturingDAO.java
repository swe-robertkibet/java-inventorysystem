package com.mycompany.inventory.royalmabati.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mycompany.inventory.royalmabati.config.DatabaseConfig;
import com.mycompany.inventory.royalmabati.model.ManufacturingOrder;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class ManufacturingDAO {
    private final MongoCollection<Document> collection;

    public ManufacturingDAO() {
        this.collection = DatabaseConfig.getDatabase().getCollection("manufacturing");
    }

    public void addManufacturingOrder(ManufacturingOrder order) {
        Document doc = new Document("productId", order.getProductId())
                .append("quantity", order.getQuantity())
                .append("startDate", order.getStartDate())
                .append("endDate", order.getEndDate())
                .append("status", order.getStatus());
        collection.insertOne(doc);
    }

    public List<ManufacturingOrder> getAllManufacturingOrders() {
        List<ManufacturingOrder> orders = new ArrayList<>();
        for (Document doc : collection.find()) {
            orders.add(documentToManufacturingOrder(doc));
        }
        return orders;
    }

    public ManufacturingOrder getManufacturingOrderById(String id) {
        Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        return doc != null ? documentToManufacturingOrder(doc) : null;
    }

    public boolean updateManufacturingOrder(ManufacturingOrder order) {
        Document update = new Document("$set", new Document("productId", order.getProductId())
                .append("quantity", order.getQuantity())
                .append("startDate", order.getStartDate())
                .append("endDate", order.getEndDate())
                .append("status", order.getStatus()));
        UpdateResult result = collection.updateOne(Filters.eq("_id", new ObjectId(order.getId())), update);
        return result.getModifiedCount() > 0;
    }

    public boolean deleteManufacturingOrder(String id) {
        DeleteResult result = collection.deleteOne(Filters.eq("_id", new ObjectId(id)));
        return result.getDeletedCount() > 0;
    }

    private ManufacturingOrder documentToManufacturingOrder(Document doc) {
        ManufacturingOrder order = new ManufacturingOrder();
        order.setId(doc.getObjectId("_id").toString());
        order.setProductId(doc.getString("productId"));
        order.setQuantity(doc.getInteger("quantity"));
        order.setStartDate(doc.getDate("startDate"));
        order.setEndDate(doc.getDate("endDate"));
        order.setStatus(doc.getString("status"));
        return order;
    }
}