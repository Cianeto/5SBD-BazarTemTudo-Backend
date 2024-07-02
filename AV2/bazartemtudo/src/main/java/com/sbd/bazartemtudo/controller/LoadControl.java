package com.sbd.bazartemtudo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sbd.bazartemtudo.model.OrderItem;
import com.sbd.bazartemtudo.repository.LoadRepo;

@RestController
@RequestMapping("/load-control")
public class LoadControl {
    
    @Autowired
    private LoadRepo loadRepo;

    @PostMapping("/insert")
    public String insertOrderItems(@RequestBody List<OrderItem> orderItems) {
        loadRepo.saveAll(orderItems);
        return "Data inserted successfully";
    }

}
