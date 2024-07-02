package com.sbd.bazartemtudo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbd.bazartemtudo.model.Customer;
import com.sbd.bazartemtudo.model.Load;
import com.sbd.bazartemtudo.repository.CustomerRepo;
import com.sbd.bazartemtudo.repository.ItemRepo;
import com.sbd.bazartemtudo.repository.LoadRepo;
import com.sbd.bazartemtudo.repository.OrderItemRepo;
import com.sbd.bazartemtudo.repository.OrderRepo;

@Service
public class LoaderService {
    @Autowired
    private LoadRepo loadRepo;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private ItemRepo itemRepo;
    @Autowired
    private OrderItemRepo itemOrderRepo;

    public String transferLoadToTables(){
        List<Load> loads = loadRepo.findAll();
        for(Load load : loads){
            Optional<Customer> customer = customerRepo.findByCpf(load.getCpf());
            if(customer.empty()){
                Optional<Customer> customer = customerRepo.findByEmail(load.getCpf());
            }
        }
    }

}
