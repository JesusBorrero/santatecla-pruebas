package com.question;

import com.GeneralRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/questions")
public class QuestionRestController extends GeneralRestController {

    @GetMapping(value = "/{questionID}")
    public ResponseEntity<Question> getQuestion(@PathVariable long questionID) {
        Optional<Question> question = this.questionService.findOne(questionID);
        if (question.isPresent()) {
            return new ResponseEntity<>(question.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
