package com.inghub.StockOrder.entity;

import jakarta.persistence.*;


@Entity
public class OauthToken {

    private @Id
    @GeneratedValue Long id;
    private String token;

    @ManyToOne( targetEntity=Employee.class )
    @JoinColumn(name="employee_id")
    private Employee employee;



    public OauthToken(String token) {
        this.token = token;
    }

    public OauthToken() {

    }

    public Long getId() {
        return id;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "OauthToken{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", employee=" + employee +
                '}';
    }
}