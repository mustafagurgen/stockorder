package com.inghub.StockOrder.rest;

import com.inghub.StockOrder.dto.BaseResponseDto;
import com.inghub.StockOrder.dto.OrderRequestDto;
import com.inghub.StockOrder.entity.CustomerOrder;
import com.inghub.StockOrder.repository.EmployeeRepository;
import com.inghub.StockOrder.services.OrderService;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final EmployeeRepository repository;
    private final OrderService orderService;

    OrderController(EmployeeRepository repository, OrderService orderService) {
        this.repository = repository;
        this.orderService = orderService;
    }

    @GetMapping("/list")
    BaseResponseDto orderList(@RequestParam @NotNull Long customerId, @RequestParam @NotNull String createDateStart, @RequestParam @NotNull String createDateEnd)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = formatter.parse(createDateStart);
            endDate = formatter.parse(createDateEnd);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return new BaseResponseDto().addResponseData("orders", orderService.listOrders(customerId, startDate, endDate));
    }

    @PostMapping("/buy")
    BaseResponseDto buy(@RequestBody OrderRequestDto orderRequestDto) {
        orderService.buy(orderRequestDto.getCustomerId(), orderRequestDto.getAssetName(), orderRequestDto.getSize(), orderRequestDto.getPrice());
        return new BaseResponseDto();
    }

    @PostMapping("/sell")
    BaseResponseDto sell(@RequestBody OrderRequestDto orderRequestDto) {
        orderService.sell(orderRequestDto.getCustomerId(), orderRequestDto.getAssetName(), orderRequestDto.getSize(), orderRequestDto.getPrice());
        return new BaseResponseDto();
    }

    @DeleteMapping("/{orderId}")
    BaseResponseDto delete(@PathVariable("orderId") Long orderId ) {
        orderService.deleteOrder(orderId);
        return new BaseResponseDto();
    }

}