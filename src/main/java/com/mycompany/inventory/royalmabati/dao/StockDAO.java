package com.mycompany.inventory.royalmabati.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mycompany.inventory.royalmabati.config.DatabaseConfig;
import com.mycompany.inventory.royalmabati.model.Stock;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class StockDAO {
    private final MongoCollection<Document> collection;

    public StockDAO() {
        this.collection = DatabaseConfig.getDatabase().getCollection("stock");
    }

    public void addStock(Stock stock) {
        Document doc = new Document("productName", stock.getProductName())
                .append("quantity", stock.getQuantity())
                .append("price", stock.getPrice());
        collection.insertOne(doc);
    }

    public List<Stock> getAllStock() {
        List<Stock> stockList = new ArrayList<>();
        for (Document doc : collection.find()) {
            stockList.add(documentToStock(doc));
        }
        return stockList;
    }

    public Stock getStockById(String id) {
        Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        return doc != null ? documentToStock(doc) : null;
    }

    public boolean updateStock(Stock stock) {
        Document update = new Document("$set", new Document("productName", stock.getProductName())
                .append("quantity", stock.getQuantity())
                .append("price", stock.getPrice()));
        UpdateResult result = collection.updateOne(Filters.eq("_id", new ObjectId(stock.getId())), update);
        return result.getModifiedCount() > 0;
    }

    public boolean deleteStock(String id) {
        DeleteResult result = collection.deleteOne(Filters.eq("_id", new ObjectId(id)));
        return result.getDeletedCount() > 0;
    }

    private Stock documentToStock(Document doc) {
        Stock stock = new Stock();
        stock.setId(doc.getObjectId("_id").toString());
        stock.setProductName(doc.getString("productName"));
        stock.setQuantity(doc.getInteger("quantity"));
        stock.setPrice(doc.getDouble("price"));
        return stock;
    }
}