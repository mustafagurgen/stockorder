package com.inghub.StockOrder.repository;


import com.inghub.StockOrder.entity.CustomerAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerAssetRepository extends JpaRepository<CustomerAsset, Long> {

    public Optional<CustomerAsset> findByCustomerIdAndAssetName(Long customerId, String assetName);

    public List<CustomerAsset> findByCustomerId(Long customerId);

}