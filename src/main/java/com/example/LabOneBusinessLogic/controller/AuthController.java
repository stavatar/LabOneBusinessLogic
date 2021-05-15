package com.example.LabOneBusinessLogic.controller;

import com.example.LabOneBusinessLogic.config.jwt.JwtProvider;
import com.example.LabOneBusinessLogic.controller.util.AuthRequest;
import com.example.LabOneBusinessLogic.controller.util.AuthResponse;
import com.example.LabOneBusinessLogic.controller.util.RegistrationRequest;
import com.example.LabOneBusinessLogic.entity.Users;
import com.example.LabOneBusinessLogic.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Log
public class AuthController
{
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/register/")
    public String registerUser(@RequestBody @Valid RegistrationRequest registrationRequest) {
        Users u = new Users();
        log.info("Пароль"+registrationRequest.getPassword());
        log.info("Логин"+registrationRequest.getLogin());
        u.setPassword(registrationRequest.getPassword());
        u.setLogin(registrationRequest.getLogin());

        userService.createUser(u);
        return "OK";
    }

    @PostMapping("/auth/")
    public AuthResponse auth(@RequestBody AuthRequest request)
    {
        Users userEntity = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        String token = jwtProvider.generateToken(userEntity.getLogin());
        log.info("Токен = "+token);
        return new AuthResponse(token);
    }
}
