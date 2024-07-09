package com.sbd.bazartemtudo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sbd.bazartemtudo.service.ProcessService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/process-orders")
public class ProcessControl {
    @Autowired
    private ProcessService procServ;

    @PostMapping("/auto-create-purchases")
    @Operation(summary = "")
    public ResponseEntity<?> insertPurchases(){
        try {

            procServ.processPendingOrdersByPriceSum();

            return ResponseEntity.status(HttpStatus.OK).body("purchase creation process successful.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("purchase creation process failed.");
        }
    }

    @PutMapping("/auto-update-purchase&inventory")
    @Operation(summary = "")
    public ResponseEntity<?> unqueuePurchase(){ // REALIZA A COMPRA DO ITEMPEDIDO INDIVIDUALMENTE MAIS CARO DO PEDIDO MAIS CARO
        try {

            procServ.unqueuePurchase();

            return ResponseEntity.status(HttpStatus.OK).body("purchase unqueued successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("purchase unqueue failed.");
        }
    }

    @PutMapping("/auto-update-order")
    @Operation(summary = "")
    public ResponseEntity<?> unqueueOrder(){
        try {

            unqueueOrder();

            return ResponseEntity.status(HttpStatus.OK).body(" successful.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(" failed.");
        }
    }

    /*
    @
    @Operation(summary = "")
    public ResponseEntity<?> template(){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(" successful.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(" failed.");
        }
    } */
    
}
