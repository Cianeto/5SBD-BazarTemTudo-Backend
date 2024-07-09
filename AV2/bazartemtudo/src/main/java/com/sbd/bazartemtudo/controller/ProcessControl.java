package com.sbd.bazartemtudo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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
    @Operation(summary = "Automatically create purchases for items lacking in inventory upon request.")
    public ResponseEntity<?> insertPurchases() {
        try {

            procServ.processPendingOrdersByPriceSum();

            return ResponseEntity.status(HttpStatus.OK).body("purchase creation process successful.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("purchase creation process failed.");
        }
    }

    @PutMapping("/auto-update-purchase&inventory")
    @Operation(summary = "Change purchase status to 'RECEIVED' and adds quantity purchased to inventory.")
    public ResponseEntity<?> updatePurchase() { // REALIZA A COMPRA DO ITEMPEDIDO INDIVIDUALMENTE MAIS CARO DO PEDIDO
                                                // MAIS CARO
        try {

            procServ.unqueuePurchase();

            return ResponseEntity.status(HttpStatus.OK).body("purchase unqueued successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("purchase unqueue failed.");
        }
    }

    @PutMapping("/auto-update-order")
    @Operation(summary = "Change highest price_sum order status to 'SENT' if there is enough inventory.")
    public ResponseEntity<?> updateOrder() {
        try {

            procServ.unqueueOrder();

            return ResponseEntity.status(HttpStatus.OK).body("order updated successfuly.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("order update failed.");
        }
    }

    @PutMapping("/update-inventory/{sku}/{addInventory}")
    @Operation(summary = "Update the inventory for a specific item.")
    public ResponseEntity<?> updateInventory(@PathVariable String sku, @PathVariable Integer addInventory) {
        try {
            String result = procServ.updateItemInventory(sku, addInventory);

            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Inventory update failed.");
        }
    }
}
