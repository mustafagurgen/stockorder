package com.inghub.StockOrder.entity;

import jakarta.persistence.*;

import java.util.Date;


@Entity
public class CustomerOrder {

    private @Id
    @GeneratedValue Long id;

    private String assetName;
    private String orderSide;
    private int size;
    private int awaitingSize;
    private double price;
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;


    @ManyToOne( targetEntity=Customer.class )
    @JoinColumn(name="customer_id")
    private Customer customer;

    public Long getId() {
        return id;
    }


    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getOrderSide() {
        return orderSide;
    }

    public void setOrderSide(String orderSide) {
        this.orderSide = orderSide;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getAwaitingSize() {
        return awaitingSize;
    }

    public void setAwaitingSize(int awaitingSize) {
        this.awaitingSize = awaitingSize;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "CustomerOrder{" +
                "id=" + id +
                ", assetName='" + assetName + '\'' +
                ", orderSide='" + orderSide + '\'' +
                ", size=" + size +
                ", awaitingSize=" + awaitingSize +
                ", price=" + price +
                ", status='" + status + '\'' +
                ", createDate=" + createDate +
                ", customer=" + customer +
                '}';
    }
}