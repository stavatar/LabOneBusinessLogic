package com.example.LabOneBusinessLogic.service;

import com.example.LabOneBusinessLogic.entity.Position;
import com.example.LabOneBusinessLogic.entity.Posts;
import com.example.LabOneBusinessLogic.entity.Users;
import com.example.LabOneBusinessLogic.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionService
{
    @Autowired
    private PositionRepository positionRepository;
    public List<Position> getAll()
    {
        return (List<Position>) positionRepository.findAll();
    }
    public void create(Position position)
    {
        positionRepository.save(position);
    }

    public boolean update(Position position, int id) {
        if (positionRepository.existsById((long) id)) {
            position.setId((long)id);
            positionRepository.save(position);
            return true;
        }

        return false;
    }
    public boolean delete(Position position)
    {
        if (positionRepository.existsById(position.getId()))
        {
            positionRepository.delete(position);
            return true;
        }
        return false;
    }
    public  Position get(int id)
    {
        return  positionRepository.findById((long) id).get();
    }
}
