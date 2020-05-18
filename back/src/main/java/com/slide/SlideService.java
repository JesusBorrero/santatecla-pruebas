package com.slide;

import java.util.List;
import java.util.Optional;

import com.itinerary.lesson.Lesson;
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

	public void updateAllSlidesCardName(String unitName, String oldName, String newName){
    	String insertCard = "insert.card/";
		List<Slide> slides = this.repository.findByContentContaining(insertCard + unitName + "/" + oldName);
		String newContent;

		for(Slide slide : slides){
			newContent = slide.getContent().replace(insertCard + unitName + "/" + oldName, insertCard + unitName + "/" + newName);
			slide.setContent(newContent);
			save(slide);
		}
	}

	public void updateAllSlidesSlideName(String lessonName, String oldName, String newName, Lesson lesson){
    	String insertSlide = "insert.slide";
    	List<Slide> slides = this.repository.findByContentContainingAndContentContaining(insertSlide, lessonName + "/" + oldName);
    	String newContent;

    	for(Slide slide : slides){
    		newContent = slide.getContent().replace(lessonName+ "/" + oldName, lessonName + "/" + newName);
    		slide.setContent(newContent);
    		save(slide);
    	}

		for(Slide slide : lesson.getSlides()){
			if(slide.getContent().contains(insertSlide) && slide.getContent().contains(lessonName + "/" + oldName)){
				newContent = slide.getContent().replace(lessonName+ "/" + oldName, lessonName + "/" + newName);
				slide.setContent(newContent);
			}
		}
	}

	public void updateAllSlidesLessonName(String oldName, String newName, String unitName, Lesson lesson){
    	String insertSlide = "insert.slide";
		List<Slide> slides = this.repository.findByContentContainingAndContentContaining(insertSlide, unitName + "/" + oldName + "/");
		String newContent;

		for(Slide slide : slides){
			newContent = slide.getContent().replace(unitName + "/" + oldName + "/", unitName + "/" + newName + "/");
			slide.setContent(newContent);
			save(slide);
		}

		for(Slide slide : lesson.getSlides()){
			if(slide.getContent().contains(insertSlide) && slide.getContent().contains("/" + oldName + "/")){
				newContent = slide.getContent().replace(unitName + "/" + oldName + "/", unitName + "/" + newName + "/");
				slide.setContent(newContent);
			}
		}
	}

	public void updateAllSlidesUnitName(String oldName, String newName){
    	String insertCard = "insert.card/";
    	String insertSlide = "insert.slide/";
		List<Slide> slides = this.repository.findByContentContainingOrContentContaining(insertCard + oldName + "/",
				insertSlide + oldName + "/");
		String newContent;

		for(Slide slide : slides){
			if(slide.getContent().contains(insertCard + oldName + "/")){
				newContent = slide.getContent().replace(insertCard + oldName + "/", insertCard + newName + "/");
				slide.setContent(newContent);
				save(slide);
			} else {
				newContent = slide.getContent().replace(insertSlide + oldName + "/", insertSlide + newName + "/");
				slide.setContent(newContent);
				save(slide);
			}
		}
	}

}