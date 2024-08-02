package com.mycompany.inventory.royalmabati.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mycompany.inventory.royalmabati.config.DatabaseConfig;
import com.mycompany.inventory.royalmabati.model.Sale;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class SalesDAO {
    private final MongoCollection<Document> collection;

    public SalesDAO() {
        this.collection = DatabaseConfig.getDatabase().getCollection("sales");
    }

    public void addSale(Sale sale) {
        Document doc = new Document("clientId", sale.getClientId())
                .append("productId", sale.getProductId())
                .append("quantity", sale.getQuantity())
                .append("totalPrice", sale.getTotalPrice())
                .append("date", sale.getDate());
        collection.insertOne(doc);
    }

    public List<Sale> getAllSales() {
        List<Sale> sales = new ArrayList<>();
        for (Document doc : collection.find()) {
            sales.add(documentToSale(doc));
        }
        return sales;
    }

    public Sale getSaleById(String id) {
        Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        return doc != null ? documentToSale(doc) : null;
    }

    public boolean updateSale(Sale sale) {
        Document update = new Document("$set", new Document("clientId", sale.getClientId())
                .append("productId", sale.getProductId())
                .append("quantity", sale.getQuantity())
                .append("totalPrice", sale.getTotalPrice())
                .append("date", sale.getDate()));
        UpdateResult result = collection.updateOne(Filters.eq("_id", new ObjectId(sale.getId())), update);
        return result.getModifiedCount() > 0;
    }

    public boolean deleteSale(String id) {
        DeleteResult result = collection.deleteOne(Filters.eq("_id", new ObjectId(id)));
        return result.getDeletedCount() > 0;
    }

    private Sale documentToSale(Document doc) {
        Sale sale = new Sale();
        sale.setId(doc.getObjectId("_id").toString());
        sale.setClientId(doc.getString("clientId"));
        sale.setProductId(doc.getString("productId"));
        sale.setQuantity(doc.getInteger("quantity"));
        sale.setTotalPrice(doc.getDouble("totalPrice"));
        sale.setDate(doc.getDate("date"));
        return sale;
    }
}