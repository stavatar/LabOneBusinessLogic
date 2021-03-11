package com.example.LabOneBusinessLogic.service;

import com.example.LabOneBusinessLogic.entity.Comments;
import com.example.LabOneBusinessLogic.entity.Posts;
import com.example.LabOneBusinessLogic.entity.Users;
import com.example.LabOneBusinessLogic.repository.PostsRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log
public class PostService
{
    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;

    public List<Posts> getAll()
{
    return (List<Posts>) postsRepository.findAll();
}
    public void create(Posts post,String nameUser)
    {
         Users user=userService.findByLogin(nameUser);
         post.setOwner(user);
        postsRepository.save(post);
    }
    public void save(Posts post)
    {
        postsRepository.save(post);
    }


     public void make_rate(Posts post,Users user,boolean likeordislike)
     {
         post.setCountLike(likeordislike ? (post.getCountLike() + 1) : (post.getCountLike() - 1));
         user.getListlike().put(post, likeordislike);

         userService.save(user);
         postsRepository.save(post);
     }

     public void remove_rate(Posts post,Users user,boolean likeordislike)
     {
          post.setCountLike(likeordislike?(post.getCountLike() - 1):(post.getCountLike()+1));
          user.getListlike().remove(post);
         userService.save(user);
         postsRepository.save(post);
     }
     public ResponseEntity<?> add_like(int id_post, String login, boolean likeordislike)
     {
         Users user=userService.findByLogin(login);
         Posts post=postsRepository.findById((long)id_post).get();
            StringBuilder answer=new StringBuilder();
         if (likeordislike)answer.append("Лайк ");
           else answer.append("Дизлайк ");

         if (!user.getListlike().containsKey(post))
         {
             make_rate( post, user, likeordislike);
             answer.append(" поставлен");
         }
         else
             {
                 if(user.getListlike().get(post)==likeordislike)
                     remove_rate( post, user, likeordislike);
                 else
                 {
                     remove_rate( post, user, likeordislike);
                     make_rate(post, user, likeordislike);
                 };
                 answer.append(" убран");
             }
           return new ResponseEntity<>(answer.toString(), HttpStatus.OK)  ;

     }





    public boolean update(Posts post, int id)
    {
        if (postsRepository.existsById((long) id))
        {
            Posts post_old=postsRepository.findById((long)id).get();
            post_old.setListComments((!post_old.getListComments().isEmpty())?post_old.getListComments():post_old.getListComments());
            post.setId((long)id);
            postsRepository.save(post);
            return true;
        }

        return false;
    }
    public boolean delete(Posts post)
    {
        if (postsRepository.existsById(post.getId()))
        {

            for (Comments comment1:post.getListComments())
                commentService.delete(comment1);


            postsRepository.save(post);
            post.setOwner(null);
            post.setListComments(null);
            postsRepository.delete(post);

            return true;
        }
        return false;
    }
    public  Posts get(int id)
    {
        return  postsRepository.findById((long) id).get();
    }
}
