package com.mycompany.inventory.royalmabati.service;

import com.mycompany.inventory.royalmabati.dao.SalesDAO;
import com.mycompany.inventory.royalmabati.model.Sale;

import java.util.Date;
import java.util.List;

public class SalesService {
    private final SalesDAO salesDAO;
    private final StockService stockService;

    public SalesService() {
        this.salesDAO = new SalesDAO();
        this.stockService = new StockService();
    }

    public void addSale(Sale sale) {
        salesDAO.addSale(sale);
        // Update stock quantity
        stockService.updateStockQuantity(sale.getProductId(), -sale.getQuantity());
    }

    public List<Sale> getAllSales() {
        return salesDAO.getAllSales();
    }

    public Sale getSaleById(String id) {
        return salesDAO.getSaleById(id);
    }

    public boolean updateSale(Sale sale) {
        Sale oldSale = getSaleById(sale.getId());
        if (oldSale != null) {
            // Adjust stock quantity
            int quantityDifference = sale.getQuantity() - oldSale.getQuantity();
            stockService.updateStockQuantity(sale.getProductId(), quantityDifference);
            return salesDAO.updateSale(sale);
        }
        return false;
    }

    public boolean deleteSale(String id) {
        Sale sale = getSaleById(id);
        if (sale != null) {
            // Restore stock quantity
            stockService.updateStockQuantity(sale.getProductId(), sale.getQuantity());
            return salesDAO.deleteSale(id);
        }
        return false;
    }

    public List<Sale> getSalesByDateRange(Date startDate, Date endDate) {
        return getAllSales().stream()
                .filter(sale -> !sale.getDate().before(startDate) && !sale.getDate().after(endDate))
                .toList();
    }

    public List<Sale> searchSales(String searchTerm) {
        List<Sale> allSales = getAllSales();
        return allSales.stream()
                .filter(sale -> sale.getClientId().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        sale.getProductId().toLowerCase().contains(searchTerm.toLowerCase()))
                .toList();
    }
}
