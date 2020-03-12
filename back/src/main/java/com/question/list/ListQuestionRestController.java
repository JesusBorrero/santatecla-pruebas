package com.question.list;

import java.util.List;
import java.util.Optional;

import com.GeneralRestController;

import com.question.list.list_answer.ListAnswer;
import com.question.list.list_question.ListQuestion;
import com.unit.Unit;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/units/{unitID}/question/list")
public class ListQuestionRestController extends GeneralRestController {

    @GetMapping("")
    public ResponseEntity<List<ListQuestion>> getListQuestions(@PathVariable long unitID) {
        Optional<Unit> unit = this.unitService.findOne(unitID);

        if (unit.isPresent())
            return new ResponseEntity<>(unit.get().getListQuestions(), HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{questionID}")
    public ResponseEntity<ListQuestion> getListQuestion(@PathVariable long unitID, @PathVariable long questionID) {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<ListQuestion> question = this.listQuestionService.findOne(questionID);

        if (unit.isPresent() && question.isPresent())
            return new ResponseEntity<>(question.get(), HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    public ResponseEntity<ListQuestion> addListQuestion(@PathVariable long unitID, @RequestBody ListQuestion question) {
        Optional<Unit> unit = this.unitService.findOne(unitID);

        if (unit.isPresent()) {
            this.listQuestionService.save(question);
            unit.get().addListQuestion(question);
            this.unitService.save(unit.get());
            return new ResponseEntity<>(question, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{questionID}")
    public ResponseEntity<ListQuestion> deleteListQuestion(@PathVariable long unitID, @PathVariable long questionID) {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<ListQuestion> question = this.listQuestionService.findOne(questionID);

        if (unit.isPresent() && question.isPresent()) {
            this.listQuestionService.delete(questionID);
            return new ResponseEntity<>(question.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/{questionID}")
    public ResponseEntity<ListQuestion> updateQuestion(@PathVariable long unitID, @PathVariable long questionID, @RequestBody ListQuestion newQuestion) {

        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<ListQuestion> oldQuestion = this.listQuestionService.findOne(questionID);

        if (unit.isPresent() && oldQuestion.isPresent()) {
            oldQuestion.get().update(newQuestion);
            this.listQuestionService.save(oldQuestion.get());
            return new ResponseEntity<>(oldQuestion.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{questionID}/answer")
    public ResponseEntity<List<ListAnswer>> getListAnswers(@PathVariable long unitID, @PathVariable long questionID) {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<ListQuestion> question = this.listQuestionService.findOne(questionID);

        if (unit.isPresent() && question.isPresent()) {
            return new ResponseEntity<>(question.get().getListAnswers(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{questionID}/answer")
    public ResponseEntity<ListAnswer> addListAnswer(@PathVariable long unitID, @PathVariable long questionID, @RequestBody ListAnswer answer) {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<ListQuestion> question = this.listQuestionService.findOne(questionID);

        if (unit.isPresent() && question.isPresent()) {
            question.get().addAnswer(answer);
            this.listQuestionService.save(question.get());
            return new ResponseEntity<>(answer, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{questionID}/chosenWrongAnswersCount")
    public ResponseEntity<Object> getChosenWrongAnswersCount(@PathVariable long unitID, @PathVariable long questionID) {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<ListQuestion> question = this.listQuestionService.findOne(questionID);

        if (unit.isPresent() && question.isPresent()) {
            return new ResponseEntity<>(this.listQuestionService.findChosenWrongAnswersCount(questionID), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{questionID}/answer/user/{userID}")
    public ResponseEntity<List<Object>> getUserAnswers(@PathVariable long questionID, @PathVariable long userID) {
        return new ResponseEntity<>(this.listQuestionService.findUserAnswers(userID, questionID), HttpStatus.OK);
    }
}