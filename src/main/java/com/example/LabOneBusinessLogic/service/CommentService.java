package com.example.LabOneBusinessLogic.service;

import com.example.LabOneBusinessLogic.entity.Comments;
import com.example.LabOneBusinessLogic.entity.Posts;
import com.example.LabOneBusinessLogic.entity.Users;
import com.example.LabOneBusinessLogic.repository.CommentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
public class CommentService
{
    @Autowired
    private CommentsRepository commentsRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
     public void save(Comments comment)
     {
         commentsRepository.save(comment);
     }
    public List<Comments> getAll()
    {
        return (List<Comments>) commentsRepository.findAll();
    }
    public void create(Comments new_comment,Long  parent_id, String login, int post_id)
    {
        Users user=userService.findByLogin(login);
        Posts post= postService.get(post_id);

        post.getListComments().add(new_comment);

        user.getListComment().add(new_comment);

        new_comment.setOwner(user);
        new_comment.setPost(post);
        if((parent_id!=null)&&(commentsRepository.existsById(parent_id)))
        {
            Comments parentComment=commentsRepository.findById(parent_id).get();
            new_comment.setParentComment(parentComment);
        }

        commentsRepository.save(new_comment);
    }

    public boolean update(Comments post, int id) {
        if (commentsRepository.existsById((long) id))
        {
            Comments currentcomment=commentsRepository.findById((long) id).get();
            if (post.getOwner()==null)
                post.setOwner(currentcomment.getOwner());
            if (post.getPost()==null)
                post.setPost(currentcomment.getPost());
            if (post.getChildComment()==null)
                post.setChildComment(currentcomment.getChildComment());
            if (post.getTitle()==null)
                post.setTitle(currentcomment.getTitle());
            if (post.getDateCreate()==null)
                post.setDateCreate(currentcomment.getDateCreate());
            if (post.getContent()==null)
                post.setContent(currentcomment.getContent());
            if (post.getParentComment()==null)
                post.setParentComment(currentcomment.getParentComment());

            post.setId((long)id);
            commentsRepository.save(post);
            return true;
        }

        return false;
    }
    public boolean delete(Comments comment)
    {
        if (commentsRepository.existsById(comment.getId()))
        {
            Users user = comment.getOwner();
            Posts post= comment.getPost();

          /*  for (Comments comment1:comment.getChildComment())
                delete(comment1);*/

            comment.setOwner(null);
            comment.setPost(null);
            comment.setParentComment(null);

            commentsRepository.delete(comment);




            return true;
        }
        return false;
    }
    public  Comments get(int id)
    {
        return  commentsRepository.findById((long) id).get();
    }
}
