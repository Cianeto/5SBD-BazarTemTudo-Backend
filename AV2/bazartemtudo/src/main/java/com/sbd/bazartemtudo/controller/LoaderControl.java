package com.sbd.bazartemtudo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sbd.bazartemtudo.model.Load;
import com.sbd.bazartemtudo.repository.LoadRepo;
import com.sbd.bazartemtudo.service.LoaderService;

import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/load-in")
public class LoaderControl {
    @Autowired
    private LoadRepo loadRepo;

    @Autowired
    private LoaderService loadServ;

    @PostMapping("/insert-loads")
    @Operation(summary = "import JSON into database and populate other tables with each load.")
    public ResponseEntity<?> insertLoad(@RequestBody List<Load> loads) { // INSERIR CARGA NA TABELA tb_load E REPASSAR
                                                                         // PARA AS DEMAIS TABELAS
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

            loadServ.transferLoadToTables();

            return ResponseEntity.status(HttpStatus.OK).body("insert-load process successful.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("insert-load process failed.");
        }
    }

    @DeleteMapping("delete-loads")
    @Operation(summary = "Truncate tb_loader.")
    public ResponseEntity<?> template() {
        try {

            loadRepo.truncateTable();

            return ResponseEntity.status(HttpStatus.OK).body("tb_loader truncated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("tb_loader truncate failed.");
        }
    }

}
