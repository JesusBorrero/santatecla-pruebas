package com.relation;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RelationRepository extends JpaRepository<Relation, Long> {
    
    public List<Relation> findAll();

    public Optional<Relation> findRelationByIncomingAndOutgoing(Long incoming, Long outgoing);
    
}