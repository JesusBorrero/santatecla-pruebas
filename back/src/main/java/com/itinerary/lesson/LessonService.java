package com.itinerary.lesson;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LessonService {

    @Autowired
    private LessonRepository repository;
    
    public Optional<Lesson> findOne(long id) {
		return repository.findById(id);
	}

	public List<Lesson> findAll() {
		return repository.findAll();
	}

    public void save(Lesson slide) {
		repository.save(slide);
  	}

	public void delete(long id) {
		repository.deleteById(id);
	}
}