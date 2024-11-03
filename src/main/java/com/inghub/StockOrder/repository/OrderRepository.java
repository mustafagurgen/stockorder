package com.inghub.StockOrder.repository;


import com.inghub.StockOrder.entity.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {

    @Query(value = "SELECT o FROM CustomerOrder o WHERE o.assetName = :assetName AND o.customer.id != :customerId AND o.status = 'PENDING' AND o.orderSide = 'BUY' AND o.price >= :price ORDER BY o.price DESC")
    public List<CustomerOrder> findByAvailableOrdersForSelling(String assetName, int price, Long customerId);

    @Query(value = "SELECT o FROM CustomerOrder o WHERE o.assetName = :assetName AND o.customer.id != :customerId AND o.status = 'PENDING' AND o.orderSide = 'SELL' AND o.price <= :price ORDER BY o.price ASC")
    public List<CustomerOrder> findByAvailableOrdersForBuying(String assetName, int price, Long customerId);

    public List<CustomerOrder> findByCustomerIdEqualsAndCreateDateIsBetween(Long customerId, Date createdDateStart, Date createdDateEnd);

    public Optional<CustomerOrder> findByIdAndStatus(Long customerId, String status);
}