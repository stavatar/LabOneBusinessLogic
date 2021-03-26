package com.example.LabOneBusinessLogic.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @ManyToOne( cascade = CascadeType.REFRESH)
    @JoinColumn(name = "position_id")
    private Position position;


    @ElementCollection
    @CollectionTable(name = "likes_dislikes",
            joinColumns = @JoinColumn(name= "id_user"))
    @Column(name = "action")
    @MapKeyJoinColumn(name = "id_post")
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnore
    private Map<Posts,Boolean> listlike=new HashMap<>();

    @OneToMany(cascade=CascadeType.REFRESH,orphanRemoval=true,mappedBy = "owner")
    @JsonIgnoreProperties("listComments")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Posts> listPost;

    @OneToMany(cascade=CascadeType.REFRESH,mappedBy = "owner" )
    @JsonIgnoreProperties("childComment")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Comments> listComment;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Users)) return false;

        Users users = (Users) o;

        if (!Objects.equals(id, users.id)) return false;
        if (!Objects.equals(login, users.login)) return false;
        if (!Objects.equals(password, users.password)) return false;
        if (!Objects.equals(position, users.position)) return false;
        if (!Objects.equals(listPost, users.listPost)) return false;
        return Objects.equals(listComment, users.listComment);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (listPost != null ? listPost.hashCode() : 0);
        result = 31 * result + (listComment != null ? listComment.hashCode() : 0);
        return result;
    }



}
