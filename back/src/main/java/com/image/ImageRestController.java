package com.image;

import com.GeneralRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/images")
public class ImageRestController extends GeneralRestController {

    @Autowired
    protected ImageService imageService;

    @GetMapping(value="/")
    public MappingJacksonValue images(){
        MappingJacksonValue result = new MappingJacksonValue(this.imageService.findAll());
        return result;
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<Image> getImage(@PathVariable int id) {
        Optional<Image> image = this.imageService.findOne(id);
        return (image.isPresent())?(new ResponseEntity<>(image.get(), HttpStatus.OK)):(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Image addImage(@RequestParam(value = "imageFile") MultipartFile imageFile) {

        Image newImage = new Image(imageFile.getOriginalFilename());
        this.imageService.save(newImage);
        this.imageService.setImage(newImage, imageFile);
        this.imageService.save(newImage);

        return newImage;
    }

}
