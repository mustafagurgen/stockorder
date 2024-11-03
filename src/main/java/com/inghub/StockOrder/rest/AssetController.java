package com.inghub.StockOrder.rest;

import com.inghub.StockOrder.dto.AssetRequestDto;
import com.inghub.StockOrder.dto.BaseResponseDto;
import com.inghub.StockOrder.dto.OrderRequestDto;
import com.inghub.StockOrder.repository.EmployeeRepository;
import com.inghub.StockOrder.services.AssetService;
import com.inghub.StockOrder.services.OrderService;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@RestController
@RequestMapping("/assets")
public class AssetController {

    private final EmployeeRepository repository;
    private final OrderService orderService;
    private final AssetService assetService;

    AssetController(EmployeeRepository repository, OrderService orderService, AssetService assetService) {
        this.repository = repository;
        this.orderService = orderService;
        this.assetService = assetService;
    }

    @GetMapping("")
    BaseResponseDto getAssets(@RequestParam @NotNull Long customerId)
    {
        return new BaseResponseDto().addResponseData("assets", assetService.getAssets(customerId));
    }

    @PostMapping("/deposit")
    BaseResponseDto deposit(@RequestBody AssetRequestDto assetRequestDto) {
        assetService.deposit(assetRequestDto.getCustomerId(), assetRequestDto.getAmount());
        return new BaseResponseDto();
    }

    @PostMapping("/withdraw")
    BaseResponseDto withdraw(@RequestBody AssetRequestDto assetRequestDto) {
        assetService.withdraw(assetRequestDto.getCustomerId(), assetRequestDto.getAmount());
        return new BaseResponseDto();
    }

}