package com.example.LabOneBusinessLogic.controller.util;

import lombok.Data;

@Data
public class AuthRequest
{
    private String login;
    private String password;
}