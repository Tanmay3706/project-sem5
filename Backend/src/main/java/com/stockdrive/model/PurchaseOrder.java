package com.stockdrive.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="purchase_orders")
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="order_number",nullable=false,unique=true)
    private String orderNumber;

    @Column(name="supplier_id",nullable=false)
    private Long supplierId;

    @Column(name="order_date",nullable=false)
    private LocalDate orderDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Status status=Status.PENDING;

    @Column(name="total_amount",nullable=false)
    private BigDecimal totalAmount=BigDecimal.ZERO;

    @Column(name="notes")
    private String notes;

    @Column(name="created_by")
    private Long createdBy;

    @JsonIgnore
    @Column(name="created_at",insertable=false,updatable=false)
    private LocalDateTime createdAt;

    public enum Status { PENDING, APPROVED, COMPLETED, CANCELLED }

    public Long getId(){return id;}
    public String getOrderNumber(){return orderNumber;}
    public Long getSupplierId(){return supplierId;}
    public LocalDate getOrderDate(){return orderDate;}
    public Status getStatus(){return status;}
    public BigDecimal getTotalAmount(){return totalAmount;}
    public String getNotes(){return notes;}
    public Long getCreatedBy(){return createdBy;}

    public void setId(Long id){this.id=id;}
    public void setOrderNumber(String orderNumber){this.orderNumber=orderNumber;}
    public void setSupplierId(Long supplierId){this.supplierId=supplierId;}
    public void setOrderDate(LocalDate orderDate){this.orderDate=orderDate;}
    public void setStatus(Status status){this.status=status;}
    public void setTotalAmount(BigDecimal totalAmount){this.totalAmount=totalAmount;}
    public void setNotes(String notes){this.notes=notes;}
    public void setCreatedBy(Long createdBy){this.createdBy=createdBy;}
}