package com.sbd.bazartemtudo.controller;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

import com.sbd.bazartemtudo.model.Load;
import com.sbd.bazartemtudo.repository.LoadRepo;

@RestController
@RequestMapping("/load-control")
public class LoadControl {

    @Autowired
    private LoadRepo loadRepo;

    @PostMapping("/insert")
    @Transactional
    public String insertLoad(@RequestBody List<Load> loads) {
        try {
            loadRepo.saveAll(loads);
            return "Data inserted successfully";
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error inserting data", e);
        }
    }
}