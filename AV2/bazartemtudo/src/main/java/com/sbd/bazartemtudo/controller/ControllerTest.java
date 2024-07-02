package com.sbd.bazartemtudo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sbd.bazartemtudo.dto.PurchaseUpdateRequest;

@RestController
@RequestMapping("/test")
public class ControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/oi")
    public String test() {
        return "Olá mundo!";
    }

    @PutMapping("/autoPopulateTables")
    public ResponseEntity<?> autoPopulateTables() {
        try {
            jdbcTemplate.execute("CALL loader_zRunAllProcedures()");
            return ResponseEntity.status(HttpStatus.OK).body("Stored Procedure loader_zRunAllProcedures() called.");
        } catch (DataAccessException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Stored Procedure call failed.");
        }
    }

    @PutMapping("/autoManageOrders")
    public ResponseEntity<?> autoManageOrders() {
        try {
            jdbcTemplate.execute("CALL order_UpdateOrderAndItemInventory()");
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Stored Procedure order_UpdateOrderAndItemInventory() called.");
        } catch (DataAccessException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Stored Procedure call failed.");
        }
    }

    @PutMapping("/updatePurchases")
    public ResponseEntity<?> updatePurchases(@RequestBody PurchaseUpdateRequest request) {
        try {
            String query = "CALL purchase_UpdatePurchaseAndOrderManually(?, ?)";

            jdbcTemplate.update(query, request.idOrder(), request.idItem());

            return ResponseEntity.status(HttpStatus.OK)
                    .body("Stored Procedure purchase_UpdatePurchaseAndOrderManually(?, ?) called with idOrder: "
                            + request.idOrder() + " and idItem: " + request.idItem() + ".");
                            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Stored Procedure call failed.");
        }
    }
}
