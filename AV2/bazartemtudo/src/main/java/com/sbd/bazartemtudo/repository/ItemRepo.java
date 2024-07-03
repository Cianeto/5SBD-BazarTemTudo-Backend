package com.sbd.bazartemtudo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sbd.bazartemtudo.model.Item;

@Repository
public interface ItemRepo extends JpaRepository<Item, String>{
    
    Optional<Item> findBySku(String sku);

}
