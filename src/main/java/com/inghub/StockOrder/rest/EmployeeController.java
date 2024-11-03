package com.inghub.StockOrder.rest;

import com.inghub.StockOrder.dto.BaseResponseDto;
import com.inghub.StockOrder.entity.Employee;
import com.inghub.StockOrder.exceptions.NotFoundException;
import com.inghub.StockOrder.exceptions.StockOrderException;
import com.inghub.StockOrder.repository.EmployeeRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository repository;

    EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }


    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("")
    BaseResponseDto all() {
        return new BaseResponseDto().addResponseData("employees",repository.findAll());
    }
    // end::get-aggregate-root[]

    @PostMapping("/")
    Employee newEmployee(@RequestBody Employee newEmployee) {
        return repository.save(newEmployee);
    }

    // Single item

    @GetMapping("/{id}")
    Employee one(@PathVariable Long id){

        try {
            return repository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Employee not found by id : " + id));
        }
        catch (NotFoundException e) {
            throw e;
        }
        catch (Throwable ex) {
            throw new StockOrderException(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

        return repository.findById(id)
                .map(employee -> {
                    employee.setUsername(newEmployee.getUsername());
                    employee.setPassword(newEmployee.getPassword());
                    return repository.save(employee);
                })
                .orElseGet(() -> {
                    return repository.save(newEmployee);
                });
    }

    @DeleteMapping("/{id}")
    void deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
    }
}