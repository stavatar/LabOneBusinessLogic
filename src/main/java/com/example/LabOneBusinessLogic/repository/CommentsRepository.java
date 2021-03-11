package com.example.LabOneBusinessLogic.repository;

import com.example.LabOneBusinessLogic.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface CommentsRepository  extends CrudRepository<Comments, Long> {

}