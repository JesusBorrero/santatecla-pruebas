package com.unit;

import java.util.List;
import java.util.Optional;

import com.card.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UnitRepository extends JpaRepository<Unit, Long> {

    public List<Unit> findByNameContaining(String name);
    
    public List<Unit> findAll();

    @Query(value = "select unit.id, unit.name from unit left join unit_relations on unit.id = unit_relations.unit_id left join relation on unit_relations.relations_id = relation.id where related_to_id = ?1", nativeQuery = true)
    public Unit getParent(Long id);
}