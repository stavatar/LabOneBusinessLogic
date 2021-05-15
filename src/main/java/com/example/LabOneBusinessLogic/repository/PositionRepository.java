package com.example.LabOneBusinessLogic.repository;

import com.example.LabOneBusinessLogic.entity.Comments;
import com.example.LabOneBusinessLogic.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface PositionRepository extends CrudRepository<Position, Long> {
    Position findByName(String name);
}