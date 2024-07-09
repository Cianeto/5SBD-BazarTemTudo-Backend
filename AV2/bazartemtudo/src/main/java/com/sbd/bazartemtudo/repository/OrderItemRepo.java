package com.sbd.bazartemtudo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sbd.bazartemtudo.model.Order;
import com.sbd.bazartemtudo.model.OrderItem;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, String>{
    
    List<OrderItem> findByOrder(Order order);

}
