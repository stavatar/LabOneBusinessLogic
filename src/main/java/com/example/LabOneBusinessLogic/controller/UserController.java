package com.example.LabOneBusinessLogic.controller;

import com.example.LabOneBusinessLogic.config.CustomUserDetails;
import com.example.LabOneBusinessLogic.entity.Comments;
import com.example.LabOneBusinessLogic.entity.Posts;
import com.example.LabOneBusinessLogic.entity.Users;
import com.example.LabOneBusinessLogic.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@Log
public class UserController
{
    @Autowired
    private UserService userService;
    @GetMapping(value = "/user/users/all/")
    public ResponseEntity<List<Users>> read()
    {
        final List<Users> users = userService.getAll();

        return users != null &&  !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping(value = "/user/users/{id}/")
    public ResponseEntity<Users> read(@PathVariable(name = "id") int id)
    {
        final Users client = userService.get(id);

        return client != null
                ? new ResponseEntity<>(client, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/user/users/delete/{id}/")
    public ResponseEntity<?> delete(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable(name = "id") int delete_user_id)
    {
        if ((userService.findByLogin(customUserDetails.getUsername()).getId()==delete_user_id)||
                customUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
        {
            return userService.delete(delete_user_id)
                    ? new ResponseEntity<>("Данные успешно изменены",HttpStatus.OK)
                    : new ResponseEntity<>("Не получилось обновить данные.Возможно,такого обьекта не существует",HttpStatus.NOT_MODIFIED);
        } else   return   new ResponseEntity<>("Не хватает прав",HttpStatus.FORBIDDEN);
    }


    @PutMapping(value = "/user/users/update{id}/")
    public ResponseEntity<?> update(@RequestBody Users new_user, @PathVariable(name = "id") int update_user_id,@AuthenticationPrincipal CustomUserDetails customUserDetails)
    {
        if ((userService.findByLogin(customUserDetails.getUsername()).getId()==update_user_id)||
                customUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
        {
            return userService.update(new_user,update_user_id)
                    ? new ResponseEntity<>("Данные успешно изменены",HttpStatus.OK)
                    : new ResponseEntity<>("Не получилось обновить данные.Возможно,такого обьекта не существует",HttpStatus.NOT_MODIFIED);
        } else   return   new ResponseEntity<>("Не хватает прав",HttpStatus.FORBIDDEN);
    }

}
