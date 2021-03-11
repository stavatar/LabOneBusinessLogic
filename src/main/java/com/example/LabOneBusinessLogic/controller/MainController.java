package com.example.LabOneBusinessLogic.controller;

import com.example.LabOneBusinessLogic.entity.Position;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log
public class MainController
{


    @GetMapping(value="/admin/get/")
    public String getAdmin() {
        return "Hi admin";
    }

    @GetMapping(value="/user/get/")
    public String getUser() {
        return "Hi user";
    }

}
