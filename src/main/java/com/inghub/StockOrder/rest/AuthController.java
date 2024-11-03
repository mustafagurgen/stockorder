package com.inghub.StockOrder.rest;

import com.inghub.StockOrder.dto.BaseResponseDto;
import com.inghub.StockOrder.dto.LoginRequestDto;
import com.inghub.StockOrder.entity.OauthToken;
import com.inghub.StockOrder.repository.EmployeeRepository;
import com.inghub.StockOrder.services.LoginService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final EmployeeRepository repository;
    private final LoginService loginService;

    AuthController(EmployeeRepository repository, LoginService loginService) {
        this.repository = repository;
        this.loginService = loginService;
    }

    @PostMapping("/login")
    BaseResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {
        OauthToken oauthToken = loginService.signin(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        return new BaseResponseDto().addResponseData("token", oauthToken.getToken()) ;
    }

}