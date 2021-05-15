package com.example.LabOneBusinessLogic.repository;

import com.example.LabOneBusinessLogic.Security.Manager.RoleType;
import com.example.LabOneBusinessLogic.entity.Position;
import org.springframework.data.repository.CrudRepository;

public interface PositionRepository extends CrudRepository<Position, Long> {
    Position findByName(RoleType name);
}