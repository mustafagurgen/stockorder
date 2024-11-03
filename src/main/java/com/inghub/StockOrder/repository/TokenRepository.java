package com.inghub.StockOrder.repository;


import com.inghub.StockOrder.entity.Employee;
import com.inghub.StockOrder.entity.OauthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<OauthToken, Long> {

    public Optional<OauthToken> findByToken(String token);

}