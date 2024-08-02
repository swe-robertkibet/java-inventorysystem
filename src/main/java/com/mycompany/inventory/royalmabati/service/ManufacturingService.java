package com.mycompany.inventory.royalmabati.service;

import com.mycompany.inventory.royalmabati.dao.ManufacturingDAO;
import com.mycompany.inventory.royalmabati.model.ManufacturingOrder;

import java.util.Date;
import java.util.List;

public class ManufacturingService {
    private final ManufacturingDAO manufacturingDAO;
    private final StockService stockService;

    public ManufacturingService() {
        this.manufacturingDAO = new ManufacturingDAO();
        this.stockService = new StockService();
    }

    public void addManufacturingOrder(ManufacturingOrder order) {
        manufacturingDAO.addManufacturingOrder(order);
    }

    public List<ManufacturingOrder> getAllManufacturingOrders() {
        return manufacturingDAO.getAllManufacturingOrders();
    }

    public ManufacturingOrder getManufacturingOrderById(String id) {
        return manufacturingDAO.getManufacturingOrderById(id);
    }

    public boolean updateManufacturingOrder(ManufacturingOrder order) {
        return manufacturingDAO.updateManufacturingOrder(order);
    }

    public boolean deleteManufacturingOrder(String id) {
        return manufacturingDAO.deleteManufacturingOrder(id);
    }

    public void completeManufacturingOrder(String orderId) {
        ManufacturingOrder order = getManufacturingOrderById(orderId);
        if (order != null && !"Completed".equals(order.getStatus())) {
            order.setStatus("Completed");
            order.setEndDate(new Date());
            updateManufacturingOrder(order);
            // Update stock
            stockService.updateStockQuantity(order.getProductId(), order.getQuantity());
        }
    }

    public List<ManufacturingOrder> getActiveManufacturingOrders() {
        return getAllManufacturingOrders().stream()
                .filter(order -> !"Completed".equals(order.getStatus()))
                .toList();
    }
}