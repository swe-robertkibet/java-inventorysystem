package com.mycompany.inventory.royalmabati.ui;

import com.mycompany.inventory.royalmabati.service.ReportingService;
import com.mycompany.inventory.royalmabati.util.ReportGenerator;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.Map;

public class ReportPanel extends JPanel {
    private JComboBox<String> reportTypeComboBox;
    private JTextField startDateField, endDateField, outputPathField;
    private JButton generateButton;
    private ReportingService reportingService;

    public ReportPanel() {
        reportingService = new ReportingService();
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2));
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

        generateButton = new JButton("Generate Report");
        formPanel.add(generateButton);

        add(formPanel, BorderLayout.NORTH);

        // Add action listener
        generateButton.addActionListener(e -> generateReport());
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

    private Date parseDate(String dateStr) {
        try {
            return new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (java.text.ParseException e) {
            return null;
        }
    }
}