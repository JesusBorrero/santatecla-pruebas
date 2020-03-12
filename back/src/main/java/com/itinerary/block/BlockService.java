package com.itinerary.block;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlockService {
    @Autowired
    private BlockRepository repository;

    public Optional<Block> findOne(long id) {
        return repository.findById(id);
    }

    public List<Block> findAll() {
        return repository.findAll();
    }

    public void save(Block block) {
        repository.save(block);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }
}
