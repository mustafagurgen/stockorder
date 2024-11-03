package com.inghub.StockOrder.entity;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;


@Entity
public class Employee {

    private @Id
    @GeneratedValue Long id;
    private String username;
    private String password;

    public Employee(String username, String password) {

        this.username = username;
        this.password = password;
    }

    public Employee() {

    }




    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}