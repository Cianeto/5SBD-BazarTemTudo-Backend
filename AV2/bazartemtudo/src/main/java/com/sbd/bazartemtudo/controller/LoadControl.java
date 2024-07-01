package com.sbd.bazartemtudo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sbd.bazartemtudo.repository.LoadRepo;

@RestController
@RequestMapping("/load-control")
public class LoadControl {
    
    @Autowired
    private LoadRepo loadRepo;
}
