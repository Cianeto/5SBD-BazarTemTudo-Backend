package com.sbd.bazartemtudo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sbd.bazartemtudo.service.ProcessService;

@RestController
@RequestMapping("/process-orders")
public class ProcessControl {
    @Autowired
    private ProcessService procServ;

    @PostMapping("")
    public ResponseEntity<?> something(){
        try {

            procServ.processPendingOrdersByPriceSum();

            return ResponseEntity.status(HttpStatus.OK).body(" successful.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(" failed.");
        }
    }

    /* public ResponseEntity<?> template(){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(" successful.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(" failed.");
        }
    } */
    
}
