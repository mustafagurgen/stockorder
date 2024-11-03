package com.inghub.StockOrder.services;

import com.inghub.StockOrder.entity.Customer;
import com.inghub.StockOrder.entity.CustomerAsset;
import com.inghub.StockOrder.entity.CustomerOrder;
import com.inghub.StockOrder.enums.AssetEnum;
import com.inghub.StockOrder.enums.OrderSideEnum;
import com.inghub.StockOrder.enums.OrderStatusEnum;
import com.inghub.StockOrder.exceptions.NotFoundException;
import com.inghub.StockOrder.exceptions.StockOrderException;
import com.inghub.StockOrder.repository.CustomerAssetRepository;
import com.inghub.StockOrder.repository.CustomerRepository;
import com.inghub.StockOrder.repository.OrderRepository;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AssetService {

    private final CustomerAssetRepository customerAssetRepository;
    private final CustomerRepository customerRepository;

    public AssetService(CustomerAssetRepository customerAssetRepository, CustomerRepository customerRepository)
    {
        this.customerAssetRepository = customerAssetRepository;
        this.customerRepository = customerRepository;
    }


    public List<CustomerAsset> getAssets(@NotNull Long customerId)
    {
         return customerAssetRepository.findByCustomerId(customerId);
    }

    public void deposit(@NotNull Long customerId, double amount)
    {

        Customer customer = customerRepository.findById(customerId).orElse(null);

        if (customer == null) {
            throw new NotFoundException("Customer not found");
        }

        CustomerAsset customerAsset;

        Optional<CustomerAsset> customerAssetOptional = customerAssetRepository.findByCustomerIdAndAssetName(customerId,AssetEnum.TRY.name()) ;

        if (customerAssetOptional.isEmpty()) {
            customerAsset = new CustomerAsset();
            customerAsset.setAssetName(AssetEnum.TRY.name());
            customerAsset.setSize((int) (amount * 100));
            customerAsset.setUsableSize((int) (amount * 100));
            customerAsset.setCustomer(customer);

        } else {
            customerAsset = customerAssetOptional.get();
            customerAsset.setSize(customerAsset.getSize() + (int) (amount * 100));
            customerAsset.setUsableSize(customerAsset.getUsableSize() + (int) (amount * 100));
        }
        customerAssetRepository.save(customerAsset);
    }


    public void withdraw(@NotNull Long customerId, double amount)
    {

        Customer customer = customerRepository.findById(customerId).orElse(null);

        if (customer == null) {
            throw new NotFoundException("Customer not found");
        }

        Optional<CustomerAsset> customerAsset = customerAssetRepository.findByCustomerIdAndAssetName(customerId,AssetEnum.TRY.name()) ;

        if (customerAsset.isEmpty() || customerAsset.get().getUsableSize() < (int) (amount * 100)) {
            throw new NotFoundException("Customer not found");
        }
        customerAsset.get().setSize(customerAsset.get().getSize() - (int) (amount * 100));
        customerAsset.get().setUsableSize(customerAsset.get().getUsableSize() - (int) (amount * 100));
        customerAssetRepository.save(customerAsset.get());
    }

}
