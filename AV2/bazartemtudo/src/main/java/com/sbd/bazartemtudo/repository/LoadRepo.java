package com.sbd.bazartemtudo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sbd.bazartemtudo.model.Load;

@Repository
public interface LoadRepo extends JpaRepository<Load, Integer>{
    
}
