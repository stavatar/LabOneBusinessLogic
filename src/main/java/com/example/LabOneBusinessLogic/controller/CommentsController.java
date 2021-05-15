package com.example.LabOneBusinessLogic.controller;

import com.example.LabOneBusinessLogic.Security.Manager.ActionType;
import com.example.LabOneBusinessLogic.Security.Manager.SecurityRolesManager;
import com.example.LabOneBusinessLogic.entity.Comments;
import com.example.LabOneBusinessLogic.service.CommentService;
import com.example.LabOneBusinessLogic.service.PostService;
import com.example.LabOneBusinessLogic.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@Tag(name = "CommentsController", description = "Содержит методы для работы с комментариями")

@RestController
@Log
public class CommentsController
{
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;

    @GetMapping(value = "/user/comments_all/")
    @Operation(summary = "Вывод всех комментариев")
    public ResponseEntity<List<Comments>> readAll()
    {
        final List<Comments> posts = commentService.getAll();

        return posts != null &&  !posts.isEmpty()
                ? new ResponseEntity<>(posts, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
       @PostMapping(value = "/user/post{id}/{parent_id}add_comments/")
       @Operation(summary = "Создание комментария")
       public ResponseEntity<?> create(@RequestBody Comments new_comments, @PathVariable(name = "parent_id") @Parameter(description = "id комментария-родителя") Optional<Long> parent_comments, @PathVariable(name = "id") int posts_id)
    {

        if (SecurityRolesManager.checkPermission(ActionType.WRITE_COMMENTS))
        {
            if (parent_comments.isPresent())
                commentService.create(new_comments, parent_comments.get(), SecurityRolesManager.getNameCurrentUser(), posts_id);
            else commentService.create(new_comments, null, SecurityRolesManager.getNameCurrentUser(), posts_id);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    @GetMapping(value = "/user/comment{id}/read/")
    @Operation(summary = "Создание комментария")
    public ResponseEntity<Comments> read(@PathVariable(name = "id") int id)
    {

        final Comments client = commentService.get(id);

        return client != null
                ? new ResponseEntity<>(client, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/user/comment{id}/getChild/")
    @Operation(summary = "Вывод дочерних комментариев")
    public ResponseEntity<List<Comments>> getChild(@PathVariable(name = "id") @Parameter(description = "ID комменатрия-родителя") int parent_id)
    {

        final Comments parent_comment = commentService.get(parent_id);

        return parent_comment != null
                ? new ResponseEntity<>(parent_comment.getChildComment(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @DeleteMapping(value = "/user/comment{id}/delete/")
    @Operation(summary = "Удалить комментарий")
    public  ResponseEntity<?>  delete(@PathVariable(name = "id") int id)
    {

        boolean deleted;

        boolean checkPermission;
        if (userService.containComment(SecurityRolesManager.getNameCurrentUser(),id))
            checkPermission = SecurityRolesManager.checkPermission(ActionType.DELETE_YOUR_COMMENTS);
        else checkPermission =  SecurityRolesManager.checkPermission(ActionType.DELETE_ALIEN_COMMENTS);

       if (checkPermission)
        {
            deleted =commentService.delete(commentService.get(id));
                return deleted
                        ? new ResponseEntity<>(HttpStatus.OK)
                        : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } else   return   new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }
    @PutMapping(value = "/user/comment/update/{id}/")
    @Operation(summary = "Изменить комментарий")
    public  ResponseEntity<?>  update(@RequestBody @Parameter(description = "Изменяемый коммент") Comments comments, @PathVariable(name = "id") @Parameter(description = "Новый коммент") int id)
    {
        boolean deleted;
        boolean checkPermission;
        if (userService.containComment(SecurityRolesManager.getNameCurrentUser(),id))
            checkPermission = SecurityRolesManager.checkPermission(ActionType.UPDATE_YOUR_COMMENTS);
        else checkPermission =  SecurityRolesManager.checkPermission(ActionType.UPDATE_ALIEN_COMMENTS);
        if (checkPermission)
        {
            deleted = commentService.update(comments, id);
            return deleted
                    ? new ResponseEntity<>(HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } else   return   new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
