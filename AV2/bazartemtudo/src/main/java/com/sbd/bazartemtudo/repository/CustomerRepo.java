package com.sbd.bazartemtudo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sbd.bazartemtudo.model.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Integer>{

    Optional<Customer> findByCpf(String cpf);

}
