package com.question.test;

import com.GeneralRestController;
import com.question.test.test_answer.TestAnswer;
import com.question.test.test_question.TestQuestion;
import com.unit.Unit;
import org.aspectj.weaver.ast.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/units/{unitID}/question/test")
public class TestQuestionRestController extends GeneralRestController {

    @GetMapping("")
    public ResponseEntity<List<TestQuestion>> getTestQuestions(@PathVariable long unitID) {
        Optional<Unit> unit = this.unitService.findOne(unitID);

        if (unit.isPresent())
            return new ResponseEntity<>(unit.get().getTestQuestions(), HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{questionID}")
    public ResponseEntity<TestQuestion> getTestQuestion(@PathVariable long unitID, @PathVariable long questionID) {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<TestQuestion> question = this.testQuestionService.findOne(questionID);

        if (unit.isPresent() && question.isPresent())
            return new ResponseEntity<>(question.get(), HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    public ResponseEntity<TestQuestion> addTestQuestion(@PathVariable long unitID, @RequestBody TestQuestion question) {
        Optional<Unit> unit = this.unitService.findOne(unitID);

        if (unit.isPresent()) {
            this.testQuestionService.save(question);
            unit.get().addTestQuestion(question);
            this.unitService.save(unit.get());
            return new ResponseEntity<>(question, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{questionID}")
    public ResponseEntity<TestQuestion> deleteTestQuestion(@PathVariable long unitID, @PathVariable long questionID) {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<TestQuestion> question = this.testQuestionService.findOne(questionID);

        if (unit.isPresent() && question.isPresent()) {
            this.testQuestionService.delete(questionID);
            return new ResponseEntity<>(question.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/{questionID}")
    public ResponseEntity<TestQuestion> updateQuestion(@PathVariable long unitID, @PathVariable long questionID, @RequestBody TestQuestion newQuestion) {

        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<TestQuestion> oldQuestion = this.testQuestionService.findOne(questionID);

        if (unit.isPresent() && oldQuestion.isPresent()) {
            oldQuestion.get().update(newQuestion);
            this.testQuestionService.save(oldQuestion.get());
            return new ResponseEntity<>(oldQuestion.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{questionID}/answer")
    public ResponseEntity<List<TestAnswer>> getTestAnswers(@PathVariable long unitID, @PathVariable long questionID) {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<TestQuestion> question = this.testQuestionService.findOne(questionID);

        if (unit.isPresent() && question.isPresent()) {
            return new ResponseEntity<>(question.get().getTestAnswers(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{questionID}/answer")
    public ResponseEntity<TestAnswer> addTestAnswer(@PathVariable long unitID, @PathVariable long questionID, @RequestBody TestAnswer answer) {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<TestQuestion> question = this.testQuestionService.findOne(questionID);

        if (unit.isPresent() && question.isPresent()) {
            question.get().addAnswer(answer);
            this.testQuestionService.save(question.get());
            return new ResponseEntity<>(answer, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}/answer/user/{userId}")
    public ResponseEntity<List<Object>> getUserAnswers(@PathVariable long id, @PathVariable long userId) {
        return new ResponseEntity<>(this.testQuestionService.findUserAnswers(userId, id), HttpStatus.OK);
    }

    @GetMapping(value = "/{questionID}/chosenWrongAnswersCount")
    public ResponseEntity<Object> getChosenWrongAnswersCount(@PathVariable long unitID, @PathVariable long questionID) {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<TestQuestion> question = this.testQuestionService.findOne(questionID);

        if (unit.isPresent() && question.isPresent()) {
            return new ResponseEntity<>(this.testQuestionService.findChosenWrongAnswersCount(questionID), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}