package com.sbd.bazartemtudo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teste")
public class ControllerTest {

    @GetMapping("/oi")
    public String teste() {
        return "Olá mundo!";
    }
}
