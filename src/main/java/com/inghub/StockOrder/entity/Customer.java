package com.inghub.StockOrder.entity;

import jakarta.persistence.*;


@Entity
public class Customer {

    private @Id
    @GeneratedValue Long id;

    private String customerName;

    @ManyToOne( targetEntity=Employee.class )
    @JoinColumn(name="employee_id")
    private Employee employee;

    public Long getId() {
        return id;
    }


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", employee=" + employee +
                '}';
    }
}