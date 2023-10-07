package com.isariev.orderservice.repository;

import com.isariev.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.MANDATORY)
public interface OrderRepository extends JpaRepository<Order, Long> {
}
