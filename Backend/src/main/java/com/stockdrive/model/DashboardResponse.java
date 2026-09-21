package com.stockdrive.model;

public class DashboardResponse {

    private long totalProducts;
    private long totalSuppliers;
    private long lowStockProducts;
    private long pendingPurchaseOrders;

    public DashboardResponse(
            long totalProducts,
            long totalSuppliers,
            long lowStockProducts,
            long pendingPurchaseOrders) {

        this.totalProducts = totalProducts;
        this.totalSuppliers = totalSuppliers;
        this.lowStockProducts = lowStockProducts;
        this.pendingPurchaseOrders = pendingPurchaseOrders;
    }

    public long getTotalProducts() {
        return totalProducts;
    }

    public long getTotalSuppliers() {
        return totalSuppliers;
    }

    public long getLowStockProducts() {
        return lowStockProducts;
    }

    public long getPendingPurchaseOrders() {
        return pendingPurchaseOrders;
    }
}