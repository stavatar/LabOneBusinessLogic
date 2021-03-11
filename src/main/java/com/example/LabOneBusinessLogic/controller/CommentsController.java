package com.example.LabOneBusinessLogic.controller;

import com.example.LabOneBusinessLogic.config.CustomUserDetails;
import com.example.LabOneBusinessLogic.entity.Comments;
import com.example.LabOneBusinessLogic.entity.Posts;
import com.example.LabOneBusinessLogic.entity.Users;
import com.example.LabOneBusinessLogic.service.CommentService;
import com.example.LabOneBusinessLogic.service.PostService;
import com.example.LabOneBusinessLogic.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

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
    public ResponseEntity<List<Comments>> readAll()
    {
        final List<Comments> posts = commentService.getAll();

        return posts != null &&  !posts.isEmpty()
                ? new ResponseEntity<>(posts, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
       @PostMapping(value = "/user/post{id}/{parent_id}add_comments/")
    public ResponseEntity<?> create(@RequestBody Comments new_comments, @PathVariable(name = "parent_id") Optional<Long> parent_comments, @PathVariable(name = "id") int posts_id, @AuthenticationPrincipal CustomUserDetails customUserDetails)
    {

        log.severe(String.valueOf(parent_comments));
        if (parent_comments.isPresent())
            commentService.create(new_comments,parent_comments.get(),customUserDetails.getUsername(),posts_id);
        else commentService.create(new_comments,null,customUserDetails.getUsername(),posts_id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping(value = "/user/comment{id}/read/")
    public ResponseEntity<Comments> read(@PathVariable(name = "id") int id)
    {

        final Comments client = commentService.get(id);

        return client != null
                ? new ResponseEntity<>(client, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/user/comment{id}/getChild/")
    public ResponseEntity<List<Comments>> getChild(@PathVariable(name = "id") int parent_id)
    {

        final Comments parent_comment = commentService.get(parent_id);

        return parent_comment != null
                ? new ResponseEntity<>(parent_comment.getChildComment(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @DeleteMapping(value = "/user/comment{id}/delete/")
    public  ResponseEntity<?>  delete(@PathVariable(name = "id") int id,@AuthenticationPrincipal CustomUserDetails customUserDetails)
    {

        boolean deleted;
        if ((userService.containComment(customUserDetails.getUsername(),id))||
                customUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
        {
            deleted =commentService.delete(commentService.get(id));
                return deleted
                        ? new ResponseEntity<>("Данные успешно изменены",HttpStatus.OK)
                        : new ResponseEntity<>("Не получилось обновить данные.Возможно,такого обьекта не существует",HttpStatus.NOT_MODIFIED);
        } else   return   new ResponseEntity<>("Не хватает прав",HttpStatus.FORBIDDEN);

    }
    @PutMapping(value = "/user/comment/update/{id}/")
    public  ResponseEntity<?>  update(@RequestBody Comments posts,@PathVariable(name = "id") int id,@AuthenticationPrincipal CustomUserDetails customUserDetails)
    {
        boolean deleted;
        if ((userService.containComment(customUserDetails.getUsername(),id))||
                customUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
        {
            deleted = commentService.update(posts, id);
            return deleted
                    ? new ResponseEntity<>("Данные успешно изменены",HttpStatus.OK)
                    : new ResponseEntity<>("Не получилось обновить данные.Возможно,такого обьекта не существует",HttpStatus.NOT_MODIFIED);
        } else   return   new ResponseEntity<>("Не хватает прав",HttpStatus.FORBIDDEN);
    }
}
