package com.sbd.bazartemtudo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sbd.bazartemtudo.model.Purchase;

@Repository
public interface PurchaseRepo extends JpaRepository<Purchase, Integer>{
    
}
