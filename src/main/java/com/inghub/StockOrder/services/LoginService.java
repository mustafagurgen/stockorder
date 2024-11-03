package com.inghub.StockOrder.services;

import com.inghub.StockOrder.entity.Employee;
import com.inghub.StockOrder.entity.OauthToken;
import com.inghub.StockOrder.exceptions.NotFoundException;
import com.inghub.StockOrder.exceptions.StockOrderException;
import com.inghub.StockOrder.repository.EmployeeRepository;
import com.inghub.StockOrder.repository.TokenRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class LoginService {

    private final TokenRepository tokenRepository;
    private final EmployeeRepository employeeRepository;

    public LoginService(TokenRepository tokenRepository, EmployeeRepository employeeRepository)
    {
        this.tokenRepository = tokenRepository;
        this.employeeRepository = employeeRepository;
    }

    public Employee isTokenValid(String token) {
        OauthToken oauthToken = tokenRepository.findByToken(token).orElse(null);

        if(oauthToken == null) {
            return null;
        }
        return oauthToken.getEmployee();
    }

    public OauthToken signin(String username, String password) {
        try {
            Optional<Employee> employee = employeeRepository.findByUsernameAndPassword(username, password);
            if (employee.isEmpty()) {
                throw new NotFoundException("Employee not found");
            }
            OauthToken oauthToken = new OauthToken();
            oauthToken.setToken(UUID.randomUUID().toString());
            oauthToken.setEmployee(employee.get());
            tokenRepository.save(oauthToken);
            return oauthToken;
        }
        catch (NotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw new StockOrderException(e.getMessage());
        }
    }
}
