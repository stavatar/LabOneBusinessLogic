package com.example.LabOneBusinessLogic.service;

import com.example.LabOneBusinessLogic.Security.Manager.RoleType;
import com.example.LabOneBusinessLogic.entity.Comments;
import com.example.LabOneBusinessLogic.entity.Position;
import com.example.LabOneBusinessLogic.entity.Posts;
import com.example.LabOneBusinessLogic.entity.Users;
import com.example.LabOneBusinessLogic.repository.PositionRepository;
import com.example.LabOneBusinessLogic.repository.UsersRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Service
@Log
public class UserService
{
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;

    @Autowired
    private TransactionTemplate transactionTemplate;


    @Autowired
    private PasswordEncoder passwordEncoder;
    public Users createUser(Users userEntity)
    {
        return (Users) transactionTemplate.execute(new TransactionCallback() {
            public Object doInTransaction(TransactionStatus status) {
                Position userRole = positionRepository.findByName(RoleType.ROLE_USER_BASED);
                userEntity.setPosition(userRole);
                userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
               return usersRepository.save(userEntity);
        }});
    }
    public Boolean containPost(String login,long  post_id)
    {

        Users user=usersRepository.findByLogin(login);
        Posts post=postService.get((int) post_id);
        if (user.getListPost().contains(post))
             return true;

        return false;
    }

    public Boolean containComment(String login,long  comment_id)
    {
        Users user=usersRepository.findByLogin(login);
        Comments comment=commentService.get((int) comment_id);
        if (user.getListComment().contains(comment))
            return true;

        return false;
    }
    public Users get(int id) {
       return usersRepository.findById((long)id).get();
    }
    public Users findByLogin(String login) {
        return usersRepository.findByLogin(login);
    }
    public List<Users> getAll()
    {
        return (List<Users>) usersRepository.findAll();
    }

    public void save(Users user)
    {
           transactionTemplate.execute(new TransactionCallback() {
            public Void doInTransaction(TransactionStatus status){
            usersRepository.save(user);
            return null; }});
    }
    public boolean update(Users user, long id)
    {
        return (boolean) transactionTemplate.execute(new TransactionCallback() {
           public Boolean doInTransaction(TransactionStatus status) {
                if (usersRepository.existsById((long) id)) {
                    Users currentUser = usersRepository.findById(id).get();
                    user.setId(id);
                    if (user.getListComment() == null)
                        user.setListComment(currentUser.getListComment());
                    if (user.getListPost() == null)
                        user.setListPost(currentUser.getListPost());
                    if (user.getPassword() == null)
                        user.setPassword(currentUser.getPassword());
                    if (user.getLogin() == null)
                        user.setLogin(currentUser.getLogin());
                    if (user.getListlike() == null)
                        user.setListlike(currentUser.getListlike());
                    if (user.getPosition() == null)
                        user.setPosition(currentUser.getPosition());
                    usersRepository.save(user);
                    return Boolean.TRUE;
                }

                return Boolean.FALSE;
            }});
    }
    public boolean delete(int  id)
    {
        return (boolean) transactionTemplate.execute(new TransactionCallback() {
            public Boolean doInTransaction(TransactionStatus status) {
                if (usersRepository.existsById((long) id)) {
                    Users user = (Users) usersRepository.findById((long) id).get();
                    user.getListPost().forEach(posts -> {
                        posts.setOwner(null);
                        postService.save(posts);
                    });
                    user.getListComment().forEach(comments -> {
                        comments.setOwner(null);
                        commentService.save(comments);
                    });
                    usersRepository.delete(user);
                    return true;
                } else return false;
            }});
    }
    public Users findByLoginAndPassword(String login, String password) {
        Users userEntity = findByLogin(login);
        if (userEntity != null)
        {
           log.severe("ПАРОЛЬ"+passwordEncoder.encode(password)+" ");
            if (passwordEncoder.matches(password, userEntity.getPassword())) {
                return userEntity;
            }
        }
        return null;
    }

}
