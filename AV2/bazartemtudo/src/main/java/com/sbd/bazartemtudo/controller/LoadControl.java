package com.sbd.bazartemtudo.controller;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import com.sbd.bazartemtudo.model.Load;
import com.sbd.bazartemtudo.repository.LoadRepo;

@RestController
@RequestMapping("/load-control")
public class LoadControl {

    @Autowired
    private LoadRepo loadRepo;

    @PostMapping("/insert")
    public ResponseEntity<?> insertLoad(@RequestBody List<Load> loads) {
        try {
            loadRepo.saveAll(loads);
            return ResponseEntity.status(HttpStatus.CREATED).body("Data inserted successfully.");
        } catch (DataAccessException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data insert failed.");
        }
    }
}