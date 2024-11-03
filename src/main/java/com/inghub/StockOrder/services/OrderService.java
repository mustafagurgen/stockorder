package com.inghub.StockOrder.services;

import com.inghub.StockOrder.entity.*;
import com.inghub.StockOrder.enums.AssetEnum;
import com.inghub.StockOrder.enums.OrderSideEnum;
import com.inghub.StockOrder.enums.OrderStatusEnum;
import com.inghub.StockOrder.exceptions.NotFoundException;
import com.inghub.StockOrder.exceptions.StockOrderException;
import com.inghub.StockOrder.repository.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final CustomerAssetRepository customerAssetRepository;

    public OrderService(CustomerRepository customerRepository, CustomerAssetRepository customerAssetRepository, OrderRepository orderRepository)
    {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.customerAssetRepository = customerAssetRepository;
    }


    public void sell(@NotNull Long customerId, String assetName, Integer size, Double price) {

        if (AssetEnum.getAssetEnum(assetName) == null) {
            throw new NotFoundException("Asset not found");
        }

        if (AssetEnum.TRY.name().equals(assetName)) {
            throw new StockOrderException("This asset is not available for this process!");
        }

        Optional<Customer> customer = customerRepository.findById(customerId);

        if (customer.isEmpty()) {
            throw new NotFoundException("Customer not found");
        }

        if (size == null || size <= 0) {
            throw new StockOrderException("Size must be greater than 1");
        }

        if (price == null || price <= 0) {
            throw new StockOrderException("Price must be greater than 1");
        }

        Optional<CustomerAsset> customerAsset = customerAssetRepository.findByCustomerIdAndAssetName(customer.get().getId(), assetName);

        if (customerAsset.isEmpty()  || customerAsset.get().getUsableSize() < size) {
            throw new StockOrderException("Customer usable asset size must be greater than size!");
        }

        List<CustomerOrder> customerOrdersToSave = new ArrayList<>();
        List<CustomerAsset> customerAssetsToSave = new ArrayList<>();

        int actualPrice = (int) (price * 100);
        List<CustomerOrder> awaitingBuyOrders = orderRepository.findByAvailableOrdersForSelling(assetName, actualPrice,customerId);

        int remainingSize = size;

        for (CustomerOrder buyOrder : awaitingBuyOrders) {
            int soldSize;
            if (buyOrder.getAwaitingSize() >= remainingSize)
            {
                soldSize = remainingSize;
            } else {
                soldSize = buyOrder.getAwaitingSize();
            }

            remainingSize -= soldSize;
            buyOrder.setAwaitingSize( buyOrder.getAwaitingSize() - soldSize);

            if (buyOrder.getAwaitingSize() == 0) {
                buyOrder.setStatus(OrderStatusEnum.MATCHED.name());
            }
            customerOrdersToSave.add(buyOrder);

            CustomerAsset customerTryAsset = customerAssetRepository.findByCustomerIdAndAssetName(buyOrder.getCustomer().getId(), AssetEnum.TRY.name()).get();
            customerTryAsset.setSize(customerTryAsset.getSize() - (soldSize * actualPrice));
            customerTryAsset.setUsableSize(customerTryAsset.getUsableSize() - (soldSize * actualPrice));

            Optional<CustomerAsset> customerBoughtAssetOptional = customerAssetRepository.findByCustomerIdAndAssetName(buyOrder.getCustomer().getId(), assetName);

            CustomerAsset customerBoughtAsset;
            if (customerBoughtAssetOptional.isEmpty()) {
                customerBoughtAsset = new CustomerAsset();
                customerBoughtAsset.setCustomer(buyOrder.getCustomer());
                customerBoughtAsset.setAssetName(assetName);
            } else {
                customerBoughtAsset = customerBoughtAssetOptional.get();
            }
            customerBoughtAsset.setSize(customerBoughtAsset.getSize() + soldSize);
            customerBoughtAsset.setUsableSize(customerBoughtAsset.getUsableSize() + soldSize);

            customerAssetsToSave.add(customerTryAsset);
            customerAssetsToSave.add(customerBoughtAsset);

            if (remainingSize == 0) {
                break;
            }
        }

        CustomerOrder customerOrderSell = new CustomerOrder();
        customerOrderSell.setStatus(remainingSize == 0 ? OrderStatusEnum.MATCHED.name() : OrderStatusEnum.PENDING.name());
        customerOrderSell.setAwaitingSize(remainingSize);
        customerOrderSell.setCustomer(customer.get());
        customerOrderSell.setOrderSide(OrderSideEnum.SELL.name());
        customerOrderSell.setSize(size);
        customerOrderSell.setPrice(price);
        customerOrderSell.setAssetName(assetName);
        customerOrderSell.setCreateDate(new Date());
        customerOrdersToSave.add(customerOrderSell);

        orderRepository.saveAll(customerOrdersToSave);

        int soldSize = size - remainingSize;

        CustomerAsset customerSoldAsset = customerAssetRepository.findByCustomerIdAndAssetName(customerId, assetName).get();
        customerSoldAsset.setSize(customerSoldAsset.getSize() - soldSize);
        customerSoldAsset.setUsableSize(customerSoldAsset.getUsableSize() - size);

        Optional<CustomerAsset> customerTryAssetOptional = customerAssetRepository.findByCustomerIdAndAssetName(customerId, AssetEnum.TRY.name());

        CustomerAsset customerTryAsset;
        if (customerTryAssetOptional.isEmpty()) {
            customerTryAsset = new CustomerAsset();
            customerTryAsset.setCustomer(customer.get());
            customerTryAsset.setAssetName(AssetEnum.TRY.name());
        } else {
            customerTryAsset = customerTryAssetOptional.get();
        }
        customerTryAsset.setSize(customerTryAsset.getSize() + (soldSize * actualPrice));
        customerTryAsset.setUsableSize(customerTryAsset.getUsableSize() + (soldSize * actualPrice));

        customerAssetsToSave.add(customerTryAsset);
        customerAssetsToSave.add(customerSoldAsset);
        customerAssetRepository.saveAll(customerAssetsToSave);

    }

    public void buy(@NotNull Long customerId, String assetName, Integer size, Double price) {
        if (AssetEnum.getAssetEnum(assetName) == null) {
            throw new NotFoundException("Asset not found");
        }

        if (AssetEnum.TRY.name().equals(assetName)) {
            throw new StockOrderException("This asset is not available for this process!");
        }

        Optional<Customer> customer = customerRepository.findById(customerId);

        if (customer.isEmpty()) {
            throw new NotFoundException("Customer not found");
        }

        if (size == null || size <= 0) {
            throw new StockOrderException("Size must be atleast 1");
        }

        if (price == null || price <= 0) {
            throw new StockOrderException("Price must be atleast 1");
        }

        Optional<CustomerAsset> customerTryAsset = customerAssetRepository.findByCustomerIdAndAssetName(customer.get().getId(), AssetEnum.TRY.name());

        int actualPrice = (int) (price * 100);

        if (customerTryAsset.isEmpty()  || customerTryAsset.get().getUsableSize() < (size * actualPrice)) {
            throw new StockOrderException("Customer does not have enough try asset!");
        }

        List<CustomerOrder> customerOrdersToSave = new ArrayList<>();
        List<CustomerAsset> customerAssetsToSave = new ArrayList<>();

        List<CustomerOrder> awaitingSellOrders = orderRepository.findByAvailableOrdersForBuying(assetName, actualPrice,customerId);

        int remainingSize = size;

        for (CustomerOrder sellOrder : awaitingSellOrders) {
            int soldSize;
            if (sellOrder.getAwaitingSize() >= remainingSize)
            {
                soldSize = remainingSize;
            } else {
                soldSize = sellOrder.getAwaitingSize();
            }

            remainingSize -= soldSize;
            sellOrder.setAwaitingSize( sellOrder.getAwaitingSize() - soldSize);

            if (sellOrder.getAwaitingSize() == 0) {
                sellOrder.setStatus(OrderStatusEnum.MATCHED.name());
            }
            customerOrdersToSave.add(sellOrder);

            CustomerAsset sellerSoldAsset = customerAssetRepository.findByCustomerIdAndAssetName(sellOrder.getCustomer().getId(), assetName).get();
            sellerSoldAsset.setSize(sellerSoldAsset.getSize() - soldSize);
            sellerSoldAsset.setUsableSize(sellerSoldAsset.getUsableSize() - soldSize);


            Optional<CustomerAsset> sellerTryAssetOptional = customerAssetRepository.findByCustomerIdAndAssetName(sellOrder.getCustomer().getId(), AssetEnum.TRY.name());

            CustomerAsset sellerTryAsset;
            if (sellerTryAssetOptional.isEmpty()) {
                sellerTryAsset = new CustomerAsset();
                sellerTryAsset.setCustomer(sellOrder.getCustomer());
                sellerTryAsset.setAssetName(AssetEnum.TRY.name());
            } else {
                sellerTryAsset = sellerTryAssetOptional.get();
            }
            sellerTryAsset.setSize(sellerTryAsset.getSize() + (soldSize * actualPrice));
            sellerTryAsset.setUsableSize(sellerTryAsset.getUsableSize() + (soldSize * actualPrice));

            customerAssetsToSave.add(sellerTryAsset);
            customerAssetsToSave.add(sellerSoldAsset);

            if (remainingSize == 0) {
                break;
            }
        } // end:  for


        // Buyer processes
        int soldSize = size - remainingSize;

        customerTryAsset.get().setSize(customerTryAsset.get().getSize() - (soldSize * actualPrice));
        customerTryAsset.get().setUsableSize(customerTryAsset.get().getUsableSize() - (size * actualPrice));

        Optional<CustomerAsset> buyerAssetOptional = customerAssetRepository.findByCustomerIdAndAssetName(customerId, assetName);

        CustomerAsset buyerAsset;
        if (buyerAssetOptional.isEmpty()) {
            buyerAsset = new CustomerAsset();
            buyerAsset.setCustomer(customer.get());
            buyerAsset.setAssetName(assetName);
        } else {
            buyerAsset = buyerAssetOptional.get();
        }
        buyerAsset.setSize(buyerAsset.getSize() + soldSize);
        buyerAsset.setUsableSize(buyerAsset.getUsableSize() + soldSize);

        customerAssetsToSave.add(customerTryAsset.get());
        customerAssetsToSave.add(buyerAsset);
        customerAssetRepository.saveAll(customerAssetsToSave);

        CustomerOrder customerOrderBuy = new CustomerOrder();
        customerOrderBuy.setStatus(remainingSize == 0 ? OrderStatusEnum.MATCHED.name() : OrderStatusEnum.PENDING.name());
        customerOrderBuy.setAwaitingSize(remainingSize);
        customerOrderBuy.setCustomer(customer.get());
        customerOrderBuy.setOrderSide(OrderSideEnum.BUY.name());
        customerOrderBuy.setSize(size);
        customerOrderBuy.setAssetName(assetName);
        customerOrderBuy.setPrice(price);
        customerOrderBuy.setCreateDate(new Date());

        customerOrdersToSave.add(customerOrderBuy);

        orderRepository.saveAll(customerOrdersToSave);

    }

    public List<CustomerOrder> listOrders(@NotNull Long customerId, @NotNull Date createDateStart, @NotNull Date createDateEnd)
    {

         return orderRepository.findByCustomerIdEqualsAndCreateDateIsBetween(customerId, createDateStart, createDateEnd);

    }

    public void deleteOrder(@NotNull Long orderId)
    {

        Optional<CustomerOrder> customerOrder = orderRepository.findByIdAndStatus(orderId, OrderStatusEnum.PENDING.name());
        if (customerOrder.isEmpty()) {
            throw new NotFoundException("Order not found");
        }

        if (OrderSideEnum.SELL.name().equals(customerOrder.get().getOrderSide())) {
            CustomerAsset sellerAsset = customerAssetRepository.findByCustomerIdAndAssetName(customerOrder.get().getCustomer().getId(), customerOrder.get().getAssetName()).get();
            sellerAsset.setUsableSize(sellerAsset.getUsableSize() + customerOrder.get().getAwaitingSize());
            customerAssetRepository.save(sellerAsset);
        } else {
            CustomerAsset buyerAsset = customerAssetRepository.findByCustomerIdAndAssetName(customerOrder.get().getCustomer().getId(), AssetEnum.TRY.name()).get();
            buyerAsset.setUsableSize(buyerAsset.getUsableSize() + (int) (customerOrder.get().getAwaitingSize() * customerOrder.get().getPrice() * 100));
            customerAssetRepository.save(buyerAsset);
        }

        customerOrder.get().setStatus(OrderStatusEnum.CANCELED.name());
        orderRepository.save(customerOrder.get());

    }

}
