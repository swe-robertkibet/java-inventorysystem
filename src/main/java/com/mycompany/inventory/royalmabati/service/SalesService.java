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
            int quantityDifference = oldSale.getQuantity() - sale.getQuantity();
            stockService.updateStockQuantity(sale.getProductId(), quantityDifference);
        }
        return salesDAO.updateSale(sale);
    }

    public boolean deleteSale(String id) {
        Sale sale = getSaleById(id);
        if (sale != null) {
            // Restore stock quantity
            stockService.updateStockQuantity(sale.getProductId(), sale.getQuantity());
        }
        return salesDAO.deleteSale(id);
    }

    public List<Sale> getSalesByDateRange(Date startDate, Date endDate) {
        // Implement logic to get sales within a date range
        return getAllSales().stream()
                .filter(sale -> !sale.getDate().before(startDate) && !sale.getDate().after(endDate))
                .toList();
    }
}