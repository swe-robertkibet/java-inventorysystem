package com.mycompany.inventory.royalmabati.service;

import com.mycompany.inventory.royalmabati.dao.SupplierDAO;
import com.mycompany.inventory.royalmabati.model.Supplier;

import java.util.List;

public class SupplierService {
    private final SupplierDAO supplierDAO;

    public SupplierService() {
        this.supplierDAO = new SupplierDAO();
    }

    public void addSupplier(Supplier supplier) {
        supplierDAO.addSupplier(supplier);
    }

    public List<Supplier> getAllSuppliers() {
        return supplierDAO.getAllSuppliers();
    }

    public Supplier getSupplierById(String id) {
        return supplierDAO.getSupplierById(id);
    }

    public boolean updateSupplier(Supplier supplier) {
        return supplierDAO.updateSupplier(supplier);
    }

    public boolean deleteSupplier(String id) {
        return supplierDAO.deleteSupplier(id);
    }

    public List<Supplier> searchSuppliers(String searchTerm) {
        // Implement search logic here
        // This is a placeholder implementation
        List<Supplier> allSuppliers = getAllSuppliers();
        return allSuppliers.stream()
                .filter(supplier -> supplier.getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        supplier.getContact().contains(searchTerm) ||
                        supplier.getAddress().toLowerCase().contains(searchTerm.toLowerCase()))
                .toList();
    }
}