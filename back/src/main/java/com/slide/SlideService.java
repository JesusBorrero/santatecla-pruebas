package com.slide;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SlideService {

    @Autowired
    private SlideRepository repository;
    
    public Optional<Slide> findOne(long id) {
		return repository.findById(id);
	}

	public List<Slide> findByName(String unitName, String lessonName, String slideName) { return repository.findByName(unitName, lessonName, slideName); }

	public List<Slide> findAll() {
		return repository.findAll();
	}

    public void save(Slide slide) {
		repository.save(slide);
  	}

	public void delete(long id) {
		repository.deleteById(id);
	}

}