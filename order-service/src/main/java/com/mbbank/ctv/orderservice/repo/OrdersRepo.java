package com.mbbank.ctv.orderservice.repo;

import com.mbbank.ctv.orderservice.dto.entity.Orders;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepo extends CrudRepository<Orders, Integer> {
}
