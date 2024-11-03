package com.inghub.StockOrder.services;

import com.inghub.StockOrder.entity.Customer;
import com.inghub.StockOrder.entity.CustomerAsset;
import com.inghub.StockOrder.entity.CustomerOrder;
import com.inghub.StockOrder.enums.AssetEnum;
import com.inghub.StockOrder.enums.OrderSideEnum;
import com.inghub.StockOrder.enums.OrderStatusEnum;
import com.inghub.StockOrder.exceptions.NotFoundException;
import com.inghub.StockOrder.repository.CustomerAssetRepository;
import com.inghub.StockOrder.repository.CustomerRepository;
import com.inghub.StockOrder.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    private OrderService orderService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerAssetRepository customerAssetRepository;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    public void setup() {
        orderService = new OrderService(this.customerRepository, this.customerAssetRepository, this.orderRepository);


    }

    @Test
    public void deleteOrder_customerOrderNotFound_test() {
        Long orderId = 1L;
        Mockito.when(orderRepository.findByIdAndStatus(orderId,OrderStatusEnum.PENDING.name()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> orderService.deleteOrder(orderId));
    }

    @Test
    public void deleteOrder_success_test() {
        Long orderId = 1L;

        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setSize(10);
        customerOrder.setCustomer(new Customer());
        customerOrder.setAwaitingSize(5);
        customerOrder.setOrderSide(OrderSideEnum.SELL.name());
        customerOrder.setStatus(OrderStatusEnum.PENDING.name());

        CustomerAsset customerAsset = new CustomerAsset();
        customerAsset.setUsableSize(11);
        customerAsset.setSize(11);
        customerAsset.setAssetName(AssetEnum.INGBANK.name());

        Mockito.when(orderRepository.findByIdAndStatus(orderId,OrderStatusEnum.PENDING.name()))
                .thenReturn(Optional.of(customerOrder));

        Mockito.when(customerAssetRepository.findByCustomerIdAndAssetName(Mockito.any(),Mockito.any()))
                        .thenReturn(Optional.of(customerAsset));

        Mockito.when(customerAssetRepository.save(Mockito.any())).thenReturn(customerAsset);
        Mockito.when(orderRepository.save(Mockito.any())).thenReturn(customerOrder);

        orderService.deleteOrder(orderId);

        Assertions.assertEquals(16, customerAsset.getUsableSize());
        Assertions.assertEquals(OrderStatusEnum.CANCELED.name(), customerOrder.getStatus());

    }




}
