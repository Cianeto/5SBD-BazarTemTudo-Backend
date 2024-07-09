package com.sbd.bazartemtudo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sbd.bazartemtudo.enums.OrderStatus;
import com.sbd.bazartemtudo.model.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, String>{
    
    List<Order> findByStatusOrderByPriceSumDesc(OrderStatus status);

}
