package com.inghub.StockOrder.mock;

import com.inghub.StockOrder.entity.Customer;
import com.inghub.StockOrder.entity.CustomerAsset;
import com.inghub.StockOrder.entity.Employee;
import com.inghub.StockOrder.entity.OauthToken;
import com.inghub.StockOrder.enums.AssetEnum;
import com.inghub.StockOrder.repository.CustomerAssetRepository;
import com.inghub.StockOrder.repository.CustomerRepository;
import com.inghub.StockOrder.repository.EmployeeRepository;
import com.inghub.StockOrder.repository.TokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class LoadData {

    private static final Logger log = LoggerFactory.getLogger(LoadData.class);

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository repository, TokenRepository tokenRepository, CustomerRepository customerRepository, CustomerAssetRepository customerAssetRepository) {


        Employee employee = new Employee("employee1", "password1");
        OauthToken oauthToken = new OauthToken("token123456789");
        oauthToken.setEmployee(employee);

        Customer customer1 = new Customer();
        customer1.setCustomerName("customer1");
        customer1.setEmployee(employee);

        Customer customer2 = new Customer();
        customer2.setCustomerName("customer2");
        customer2.setEmployee(employee);

        Customer customer3 = new Customer();
        customer3.setCustomerName("customer3");
        customer3.setEmployee(employee);

        CustomerAsset customerAsset1 = new CustomerAsset();
        customerAsset1.setAssetName(AssetEnum.TRY.name());
        customerAsset1.setCustomer(customer1);
        customerAsset1.setSize(10000);
        customerAsset1.setUsableSize(10000);

        CustomerAsset customerAsset2 = new CustomerAsset();
        customerAsset2.setAssetName(AssetEnum.INGBANK.name());
        customerAsset2.setCustomer(customer1);
        customerAsset2.setSize(100);
        customerAsset2.setUsableSize(100);

        CustomerAsset customerAsset3 = new CustomerAsset();
        customerAsset3.setAssetName(AssetEnum.TRY.name());
        customerAsset3.setCustomer(customer2);
        customerAsset3.setSize(20000);
        customerAsset3.setUsableSize(20000);

        CustomerAsset customerAsset4 = new CustomerAsset();
        customerAsset4.setAssetName(AssetEnum.INGBANK.name());
        customerAsset4.setCustomer(customer2);
        customerAsset4.setSize(200);
        customerAsset4.setUsableSize(200);

        CustomerAsset customerAsset5 = new CustomerAsset();
        customerAsset5.setAssetName(AssetEnum.XBANK.name());
        customerAsset5.setCustomer(customer2);
        customerAsset5.setSize(200);
        customerAsset5.setUsableSize(200);

        return args -> {
            log.info("Preloading " + repository.save(employee));
            log.info("Preloading " + tokenRepository.save(oauthToken));
            log.info("Preloading " + customerRepository.saveAll(Arrays.asList(customer1,customer2, customer3)));
            log.info("Preloading " + customerAssetRepository.saveAll(Arrays.asList(customerAsset1,customerAsset2, customerAsset3, customerAsset4, customerAsset5)));
        };
    }
}