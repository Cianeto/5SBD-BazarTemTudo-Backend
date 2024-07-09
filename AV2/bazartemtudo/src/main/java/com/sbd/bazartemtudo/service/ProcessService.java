package com.sbd.bazartemtudo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbd.bazartemtudo.repository.ItemRepo;
import com.sbd.bazartemtudo.repository.OrderItemRepo;
import com.sbd.bazartemtudo.repository.OrderRepo;
import com.sbd.bazartemtudo.repository.PurchaseRepo;

@Service
public class ProcessService {
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private ItemRepo itemRepo;
    @Autowired
    private OrderItemRepo orderItemRepo;
    @Autowired
    private PurchaseRepo purchaseRepo;


    
}
