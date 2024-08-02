package com.mycompany.inventory.royalmabati.service;

import com.mycompany.inventory.royalmabati.dao.StockDAO;
import com.mycompany.inventory.royalmabati.model.Stock;

import java.util.List;

public class StockService {
    private final StockDAO stockDAO;

    public StockService() {
        this.stockDAO = new StockDAO();
    }

    public void addStock(Stock stock) {
        stockDAO.addStock(stock);
    }

    public List<Stock> getAllStock() {
        return stockDAO.getAllStock();
    }

    public Stock getStockById(String id) {
        return stockDAO.getStockById(id);
    }

    public boolean updateStock(Stock stock) {
        return stockDAO.updateStock(stock);
    }

    public boolean deleteStock(String id) {
        return stockDAO.deleteStock(id);
    }

    public void updateStockQuantity(String productId, int quantityChange) {
        Stock stock = getStockById(productId);
        if (stock != null) {
            int newQuantity = stock.getQuantity() + quantityChange;
            stock.setQuantity(newQuantity);
            updateStock(stock);
        }
    }

    public List<Stock> searchStock(String searchTerm) {
        List<Stock> allStock = getAllStock();
        return allStock.stream()
                .filter(stock -> stock.getProductName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        String.valueOf(stock.getQuantity()).contains(searchTerm) ||
                        String.valueOf(stock.getPrice()).contains(searchTerm))
                .toList();
    }

    public List<Stock> getLowStockItems(int threshold) {
        return getAllStock().stream()
                .filter(stock -> stock.getQuantity() < threshold)
                .toList();
    }
}
