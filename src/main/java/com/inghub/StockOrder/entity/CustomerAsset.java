package com.inghub.StockOrder.entity;

import jakarta.persistence.*;


@Entity
public class CustomerAsset {

    private @Id
    @GeneratedValue Long id;
    private String assetName;
    private int size;
    private int usableSize;

    @ManyToOne( targetEntity=Customer.class )
    @JoinColumn(name="customer_id")
    private Customer customer;


    public CustomerAsset () {

    }

    public Long getId() {
        return id;
    }


    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getUsableSize() {
        return usableSize;
    }

    public void setUsableSize(int usableSize) {
        this.usableSize = usableSize;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "CustomerAsset{" +
                "id=" + id +
                ", assetName='" + assetName + '\'' +
                ", size=" + size +
                ", usableSize=" + usableSize +
                ", customer=" + customer +
                '}';
    }
}