package com.example.LabOneBusinessLogic.repository;

import com.example.LabOneBusinessLogic.entity.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<Users, Long>
{
    Users findByLogin(String login);
}