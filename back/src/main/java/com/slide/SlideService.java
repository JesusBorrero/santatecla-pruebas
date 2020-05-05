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
		List<Slide> slides = this.repository.findByContentContaining("insert.card/" + unitName + "/" + oldName);
		String newContent;

		for(Slide slide : slides){
			newContent = slide.getContent().replace("insert.card/" + unitName + "/" + oldName, "insert.card/" + unitName + "/" + newName);
			slide.setContent(newContent);
			save(slide);
		}
	}

	public void updateAllSlidesSlideName(String lessonName, String oldName, String newName, Lesson lesson){
    	List<Slide> slides = this.repository.findByContentContainingAndContentContaining("insert.slide", lessonName + "/" + oldName);
    	String newContent;

    	for(Slide slide : slides){
    		newContent = slide.getContent().replace(lessonName+ "/" + oldName, lessonName + "/" + newName);
    		slide.setContent(newContent);
    		save(slide);
    	}

		for(Slide slide : lesson.getSlides()){
			if(slide.getContent().contains("insert.slide") && slide.getContent().contains(lessonName + "/" + oldName)){
				newContent = slide.getContent().replace(lessonName+ "/" + oldName, lessonName + "/" + newName);
				slide.setContent(newContent);
			}
		}
	}

	public void updateAllSlidesLessonName(String oldName, String newName, String unitName, Lesson lesson){
		List<Slide> slides = this.repository.findByContentContainingAndContentContaining("insert.slide", unitName + "/" + oldName + "/");
		String newContent;

		for(Slide slide : slides){
			newContent = slide.getContent().replace(unitName + "/" + oldName + "/", unitName + "/" + newName + "/");
			slide.setContent(newContent);
			save(slide);
		}

		for(Slide slide : lesson.getSlides()){
			if(slide.getContent().contains("insert.slide") && slide.getContent().contains("/" + oldName + "/")){
				newContent = slide.getContent().replace(unitName + "/" + oldName + "/", unitName + "/" + newName + "/");
				slide.setContent(newContent);
			}
		}
	}

	public void updateAllSlidesUnitName(String oldName, String newName){
		List<Slide> slides = this.repository.findByContentContainingOrContentContaining("insert.card/" + oldName + "/",
				"insert.slide/" + oldName + "/");
		String newContent;

		for(Slide slide : slides){
			if(slide.getContent().contains("insert.card/" + oldName + "/")){
				newContent = slide.getContent().replace("insert.card/" + oldName + "/", "insert.card/" + newName + "/");
				slide.setContent(newContent);
				save(slide);
			} else {
				newContent = slide.getContent().replace("insert.slide/" + oldName + "/", "insert.slide/" + newName + "/");
				slide.setContent(newContent);
				save(slide);
			}
		}
	}

}