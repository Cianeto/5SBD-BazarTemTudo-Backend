package com.sbd.bazartemtudo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sbd.bazartemtudo.model.OrderItem;
import com.sbd.bazartemtudo.model.Purchase;

@Repository
public interface PurchaseRepo extends JpaRepository<Purchase, Integer>{
    Optional<Purchase> findByOrderItem(OrderItem orderItem);
}
