package com.mbbank.ctv.orderservice.repo;

import com.mbbank.ctv.orderservice.dto.entity.Orders;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrdersRepo extends CrudRepository<Orders, Integer> {
    Optional<Orders> findByOrderId(String orderId);
}
