package com.mycompany.inventory.royalmabati.service;

import com.mycompany.inventory.royalmabati.model.Sale;
import com.mycompany.inventory.royalmabati.model.Stock;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportingService {
    private final SalesService salesService;
    private final StockService stockService;

    public ReportingService() {
        this.salesService = new SalesService();
        this.stockService = new StockService();
    }

    public double getTotalSalesRevenue(Date startDate, Date endDate) {
        List<Sale> sales = salesService.getSalesByDateRange(startDate, endDate);
        return sales.stream().mapToDouble(Sale::getTotalPrice).sum();
    }

    public Map<String, Integer> getTopSellingProducts(Date startDate, Date endDate, int limit) {
        List<Sale> sales = salesService.getSalesByDateRange(startDate, endDate);
        return sales.stream()
                .collect(Collectors.groupingBy(Sale::getProductId, Collectors.summingInt(Sale::getQuantity)))
                .entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(limit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public List<Stock> getLowStockReport(int threshold) {
        return stockService.getLowStockItems(threshold);
    }

    public Map<String, Double> getSalesByClient(Date startDate, Date endDate) {
        List<Sale> sales = salesService.getSalesByDateRange(startDate, endDate);
        return sales.stream()
                .collect(Collectors.groupingBy(Sale::getClientId, Collectors.summingDouble(Sale::getTotalPrice)));
    }

    public List<Sale> searchSales(String searchTerm) {
        List<Sale> allSales = salesService.getAllSales();
        return allSales.stream()
                .filter(sale -> sale.getProductId().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        sale.getClientId().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Stock> searchStock(String searchTerm) {
        List<Stock> allStock = stockService.getAllStock();
        return allStock.stream()
                .filter(stock -> stock.getProductName().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
    }
}
