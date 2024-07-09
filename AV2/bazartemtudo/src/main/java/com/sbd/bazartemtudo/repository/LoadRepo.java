package com.sbd.bazartemtudo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sbd.bazartemtudo.model.Load;

import jakarta.transaction.Transactional;

@Repository
public interface LoadRepo extends JpaRepository<Load, Integer>{
    
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE tb_loader;", nativeQuery = true)
    void truncateTable();

}
