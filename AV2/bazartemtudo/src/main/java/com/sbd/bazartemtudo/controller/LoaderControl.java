package com.sbd.bazartemtudo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sbd.bazartemtudo.model.Load;
import com.sbd.bazartemtudo.repository.LoadRepo;

import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/load-in")
public class LoaderControl {
    @Autowired
    private LoadRepo loadRepo;

    @PostMapping("insert-load")
    @Operation(summary = "import JSON into database.")
    public ResponseEntity<?> insertLoad(@RequestBody List<Load> loads) {
        List<Load> allLoads = loadRepo.findAll();
        try {
            for (Load load : loads) {
                boolean exists = false;
                for (Load lo : allLoads) { // VERIFICAR SE JÁ EXISTE UMA CARGA COM O MESMO ORDER_ITEM_ID
                    if (load.getOrderItemId().equals(lo.getOrderItemId())) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    loadRepo.save(load);
                }
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("insert-load process successful.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("insert-load process failed.");
        }
    }
}
