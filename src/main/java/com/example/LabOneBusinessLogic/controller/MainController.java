package com.example.LabOneBusinessLogic.controller;

import com.example.LabOneBusinessLogic.entity.Position;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Tag(name = "MainController", description = "Содержит методы для тестирования ролей")

@RestController
@Log
public class MainController
{


    @GetMapping(value="/admin/get/")
    @Operation(summary = "Тестирование роли админа")
    public String getAdmin() {
        return "Hi admin";
    }

    @GetMapping(value="/user/get/")

    public String getUser() {
        return "Hi user";
    }

}
