package com.slide;

import java.util.List;
import java.util.Optional;

import com.GeneralRestController;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/slides")
public class SlideRestController extends GeneralRestController implements SlideController {

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(value="/")
    public MappingJacksonValue slides() {
        return new MappingJacksonValue(this.slideService.findAll());
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<Slide> slide(@PathVariable long id){

        Optional<Slide> s = this.slideService.findOne(id);

        if (s.isPresent()) {
            return new ResponseEntity<>(s.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Slide> deleteSlide(@PathVariable long id){

        Optional<Slide> s = this.slideService.findOne(id);
        
        if(s.isPresent()){
            this.slideService.delete(id);
            return new ResponseEntity<>(s.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<Slide> updateSlide(@PathVariable long id, @RequestBody SlideDto slideDto){
        Optional<Slide> s = this.slideService.findOne(id);

        Slide slide = convertToSlideEntity(slideDto);
        
        if(s.isPresent()){
            s.get().update(slide);
            this.slideService.save(s.get());
            return new ResponseEntity<>(s.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<Slide>> getSlideByName(@RequestParam String unitName, @RequestParam String lessonName, @RequestParam String slideName) {
        List<Slide> slides = this.slideService.findByName(unitName, lessonName, slideName);
        if (slides.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(slides, HttpStatus.OK);
    }

    private Slide convertToSlideEntity(SlideDto dto) {
        return modelMapper.map(dto, Slide.class);
    }

}