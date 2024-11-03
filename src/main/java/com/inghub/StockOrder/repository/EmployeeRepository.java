package com.inghub.StockOrder.repository;


import com.inghub.StockOrder.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query(value = "SELECT e FROM Employee e WHERE e.username =:username and e.password =:password ", nativeQuery = false)
    public Optional<Employee> findByUsernameAndPassword(String username, String password);
}