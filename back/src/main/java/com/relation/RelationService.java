package com.relation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelationService {

	@Autowired
	private RelationRepository relationRepository;

	public Optional<Relation> findOne(long id) {
		return relationRepository.findById(id);
	}

	public Optional<Relation> findRelationByIncomingAndOutgoing(Long incoming, Long outgoing) {
		return relationRepository.findRelationByIncomingAndOutgoing(incoming, outgoing);
	};

	public void save(Relation relation) {
		relationRepository.save(relation);
	}

	public void delete(long id) {
		relationRepository.deleteById(id);
	}

}