package com.example.LabOneBusinessLogic.controller;

import com.example.LabOneBusinessLogic.entity.Position;
import com.example.LabOneBusinessLogic.entity.Posts;
import com.example.LabOneBusinessLogic.entity.Users;
import com.example.LabOneBusinessLogic.service.PositionService;
import com.example.LabOneBusinessLogic.service.PostService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log
public class PositionController
{
    @Autowired
    private PositionService positionService;

    @GetMapping(value = "/user/role/all/")
    public ResponseEntity<List<Position>> read()
    {
        final List<Position> posts = positionService.getAll();

        return posts != null &&  !posts.isEmpty()
                ? new ResponseEntity<>(posts, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
