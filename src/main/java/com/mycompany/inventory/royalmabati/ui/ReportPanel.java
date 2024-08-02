package com.mycompany.inventory.royalmabati.ui;

import com.mycompany.inventory.royalmabati.model.Sale;
import com.mycompany.inventory.royalmabati.model.Stock;
import com.mycompany.inventory.royalmabati.service.ReportingService;
import com.mycompany.inventory.royalmabati.util.ReportGenerator;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReportPanel extends JPanel {
    private JComboBox<String> reportTypeComboBox;
    private JTextField startDateField, endDateField, outputPathField, searchField;
    private JButton generateButton, searchButton;
    private ReportingService reportingService;

    public ReportPanel() {
        reportingService = new ReportingService();
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2));
        formPanel.add(new JLabel("Report Type:"));
        String[] reportTypes = { "Sales Revenue", "Top Selling Products", "Low Stock", "Sales by Client" };
        reportTypeComboBox = new JComboBox<>(reportTypes);
        formPanel.add(reportTypeComboBox);

        formPanel.add(new JLabel("Start Date (yyyy-MM-dd):"));
        startDateField = new JTextField();
        formPanel.add(startDateField);

        formPanel.add(new JLabel("End Date (yyyy-MM-dd):"));
        endDateField = new JTextField();
        formPanel.add(endDateField);

        formPanel.add(new JLabel("Output Path:"));
        outputPathField = new JTextField();
        formPanel.add(outputPathField);

        formPanel.add(new JLabel("Search:"));
        searchField = new JTextField();
        formPanel.add(searchField);

        generateButton = new JButton("Generate Report");
        searchButton = new JButton("Search");
        formPanel.add(generateButton);
        formPanel.add(searchButton);

        add(formPanel, BorderLayout.NORTH);

        // Add action listeners
        generateButton.addActionListener(e -> generateReport());
        searchButton.addActionListener(e -> searchReportData());
    }

    private void generateReport() {
        String reportType = (String) reportTypeComboBox.getSelectedItem();
        Date startDate = parseDate(startDateField.getText());
        Date endDate = parseDate(endDateField.getText());
        String outputPath = outputPathField.getText();

        if (startDate == null || endDate == null) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use yyyy-MM-dd.");
            return;
        }

        switch (reportType) {
            case "Sales Revenue":
                generateSalesRevenueReport(startDate, endDate, outputPath);
                break;
            case "Top Selling Products":
                generateTopSellingProductsReport(startDate, endDate, outputPath);
                break;
            case "Low Stock":
                generateLowStockReport(outputPath);
                break;
            case "Sales by Client":
                generateSalesByClientReport(startDate, endDate, outputPath);
                break;
        }

        JOptionPane.showMessageDialog(this, "Report generated successfully: " + outputPath);
    }

    private void generateSalesRevenueReport(Date startDate, Date endDate, String outputPath) {
        double totalRevenue = reportingService.getTotalSalesRevenue(startDate, endDate);
        ReportGenerator.generatePDFReport(
                "Sales Revenue Report",
                java.util.Arrays.asList("Start Date", "End Date", "Total Revenue"),
                java.util.Arrays.asList(java.util.Arrays.asList(
                        startDate.toString(),
                        endDate.toString(),
                        String.format("%.2f", totalRevenue))),
                outputPath);
    }

    private void generateTopSellingProductsReport(Date startDate, Date endDate, String outputPath) {
        Map<String, Integer> topProducts = reportingService.getTopSellingProducts(startDate, endDate, 10);
        ReportGenerator.generatePDFReport(
                "Top Selling Products Report",
                java.util.Arrays.asList("Product ID", "Quantity Sold"),
                topProducts.entrySet().stream()
                        .map(entry -> java.util.Arrays.asList(entry.getKey(), entry.getValue().toString()))
                        .collect(java.util.stream.Collectors.toList()),
                outputPath);
    }

    private void generateLowStockReport(String outputPath) {
        ReportGenerator.generatePDFReport(
                "Low Stock Report",
                java.util.Arrays.asList("Product ID", "Product Name", "Current Quantity"),
                reportingService.getLowStockReport(10).stream()
                        .map(stock -> java.util.Arrays.asList(stock.getId(), stock.getProductName(),
                                String.valueOf(stock.getQuantity())))
                        .collect(java.util.stream.Collectors.toList()),
                outputPath);
    }

    private void generateSalesByClientReport(Date startDate, Date endDate, String outputPath) {
        Map<String, Double> salesByClient = reportingService.getSalesByClient(startDate, endDate);
        ReportGenerator.generateSalesReport(salesByClient, outputPath);
    }

    private void searchReportData() {
        String searchTerm = searchField.getText().trim();
        if (!searchTerm.isEmpty()) {
            List<Sale> salesResults = reportingService.searchSales(searchTerm);
            List<Stock> stockResults = reportingService.searchStock(searchTerm);

            StringBuilder resultBuilder = new StringBuilder("Search Results:\n\nSales:\n");
            for (Sale sale : salesResults) {
                resultBuilder.append(String.format("Product ID: %s, Client ID: %s, Quantity: %d, Total Price: %.2f\n",
                        sale.getProductId(), sale.getClientId(), sale.getQuantity(), sale.getTotalPrice()));
            }

            resultBuilder.append("\nStock:\n");
            for (Stock stock : stockResults) {
                resultBuilder.append(String.format("Product ID: %s, Product Name: %s, Quantity: %d\n",
                        stock.getId(), stock.getProductName(), stock.getQuantity()));
            }

            JTextArea textArea = new JTextArea(resultBuilder.toString());
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            JOptionPane.showMessageDialog(this, scrollPane, "Search Results", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a search term.");
        }
    }

    private Date parseDate(String dateStr) {
        try {
            return new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (java.text.ParseException e) {
            return null;
        }
    }
}
