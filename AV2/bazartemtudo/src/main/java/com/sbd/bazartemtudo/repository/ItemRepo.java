package com.sbd.bazartemtudo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sbd.bazartemtudo.model.Item;

@Repository
public interface ItemRepo extends JpaRepository<Item, Integer>{
    
}
