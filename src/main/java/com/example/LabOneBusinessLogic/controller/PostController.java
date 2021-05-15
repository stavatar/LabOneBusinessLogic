package com.example.LabOneBusinessLogic.controller;

import com.example.LabOneBusinessLogic.config.CustomUserDetails;
import com.example.LabOneBusinessLogic.entity.Posts;
import com.example.LabOneBusinessLogic.entity.Users;
import com.example.LabOneBusinessLogic.service.PostService;
import com.example.LabOneBusinessLogic.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@Log
public class PostController
{
    @Autowired
    private  PostService postService;
    @Autowired
    private UserService userService;

    @GetMapping(value = "/user/posts/")
    public ResponseEntity<List<Posts>> read()
    {
        final List<Posts> posts = postService.getAll();

        return posts != null &&  !posts.isEmpty()
                ? new ResponseEntity<>(posts, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/user/create_post/",consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> create(@RequestBody Posts posts,@AuthenticationPrincipal CustomUserDetails customUserDetails)
    {
        postService.create(posts, customUserDetails.getUsername());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping(value = "/user/post{id}/read/")
    public ResponseEntity<Posts> read(@PathVariable(name = "id") int id) {
        final Posts client = postService.get(id);

        return client != null
                ? new ResponseEntity<>(client, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping(value = "/user/post{id}/like/")
    public  ResponseEntity<?> like(@PathVariable(name = "id") int post_id,@AuthenticationPrincipal CustomUserDetails customUserDetails)
    {
       return postService.add_like(post_id,customUserDetails.getUsername(),true);
    }
    @GetMapping(value = "/user/post{id}/dislike/")
    public  ResponseEntity<?> dislike(@PathVariable(name = "id") int post_id,@AuthenticationPrincipal CustomUserDetails customUserDetails)
    {
        return postService.add_like(post_id,customUserDetails.getUsername(),false);

    }
    @DeleteMapping(value = "/user/post{id}/delete/")
    public  ResponseEntity<?>  delete(@PathVariable(name = "id") int id,@AuthenticationPrincipal CustomUserDetails customUserDetails)
    {

        boolean deleted;
        if ((userService.containPost(customUserDetails.getUsername(),id))||
                customUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
        {
                deleted = postService.delete(postService.get(id));
            return deleted
                    ? new ResponseEntity<>(HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } else   return   new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    @PutMapping(value = "/user/post{id}/update/")
    public  ResponseEntity<?>  update(@RequestBody Posts new_posts,@PathVariable(name = "id") int id,@AuthenticationPrincipal CustomUserDetails customUserDetails)
    {
        boolean deleted;
        if ((userService.containPost(customUserDetails.getUsername(),id))||
                customUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
        {
            deleted =  postService.update(new_posts, id);

               return deleted
                        ? new ResponseEntity<>(HttpStatus.OK)
                        : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } else   return   new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
