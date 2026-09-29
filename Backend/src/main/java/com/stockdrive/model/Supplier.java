package com.stockdrive.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="suppliers")
public class Supplier {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String supplierName;

    private String contactPerson;
    private String phone;
    private String email;
    private String address;
    private String city;
    private String state;

    @Column(name="created_at",insertable=false,updatable=false)
    private LocalDateTime createdAt;

    public Long getId(){return id;}
    public String getSupplierName(){return supplierName;}
    public String getContactPerson(){return contactPerson;}
    public String getPhone(){return phone;}
    public String getEmail(){return email;}
    public String getAddress(){return address;}
    public String getCity(){return city;}
    public String getState(){return state;}
    public LocalDateTime getCreatedAt(){return createdAt;}

    public void setId(Long id){this.id=id;}
    public void setSupplierName(String supplierName){this.supplierName=supplierName;}
    public void setContactPerson(String contactPerson){this.contactPerson=contactPerson;}
    public void setPhone(String phone){this.phone=phone;}
    public void setEmail(String email){this.email=email;}
    public void setAddress(String address){this.address=address;}
    public void setCity(String city){this.city=city;}
    public void setState(String state){this.state=state;}
    public void setCreatedAt(LocalDateTime createdAt){this.createdAt=createdAt;}
}