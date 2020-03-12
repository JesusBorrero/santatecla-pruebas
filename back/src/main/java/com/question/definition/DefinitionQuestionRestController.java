package com.question.definition;

import java.util.List;
import java.util.Optional;

import com.GeneralRestController;
import com.question.definition.definition_answer.DefinitionAnswer;
import com.question.definition.definition_question.DefinitionQuestion;

import com.unit.Unit;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/units/{unitID}/question/definition")
public class DefinitionQuestionRestController extends GeneralRestController {

    @GetMapping("")
    public ResponseEntity<List<DefinitionQuestion>> getQuestions(@PathVariable long unitID) {
        Optional<Unit> unit = this.unitService.findOne(unitID);

        if (unit.isPresent())
            return new ResponseEntity<>(unit.get().getDefinitionQuestions(), HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{questionID}")
    public ResponseEntity<DefinitionQuestion> getDefinitionQuestion(@PathVariable long unitID, @PathVariable long questionID) {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<DefinitionQuestion> question = this.definitionQuestionService.findOne(questionID);

        if (unit.isPresent() && question.isPresent())
            return new ResponseEntity<>(question.get(), HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    public ResponseEntity<DefinitionQuestion> addDefinitionQuestion(@PathVariable long unitID, @RequestBody DefinitionQuestion question) {
        Optional<Unit> unit = this.unitService.findOne(unitID);

        if (unit.isPresent()) {
            this.definitionQuestionService.save(question);
            unit.get().addDefinitionQuestion(question);
            this.unitService.save(unit.get());
            return new ResponseEntity<>(question, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{questionID}")
    public ResponseEntity<DefinitionQuestion> deleteDefinitionQuestion(@PathVariable long unitID, @PathVariable long questionID) {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<DefinitionQuestion> question = this.definitionQuestionService.findOne(questionID);

        if (unit.isPresent() && question.isPresent()) {
            this.definitionQuestionService.delete(questionID);
            return new ResponseEntity<>(question.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{questionID}")
    public ResponseEntity<DefinitionQuestion> updateQuestion(@PathVariable long unitID, @PathVariable long questionID, @RequestBody DefinitionQuestion newQuestion) {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<DefinitionQuestion> oldQuestion = this.definitionQuestionService.findOne(questionID);

        if (unit.isPresent() && oldQuestion.isPresent()) {
            oldQuestion.get().update(newQuestion);
            this.definitionQuestionService.save(oldQuestion.get());
            return new ResponseEntity<>(oldQuestion.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{questionID}/answer")
    public ResponseEntity<List<DefinitionAnswer>> getDefinitionAnswers(
            @PathVariable long unitID,
            @PathVariable long questionID,
            @RequestParam(required = false) String corrected) {

        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<DefinitionQuestion> question = this.definitionQuestionService.findOne(questionID);

        if (unit.isPresent() && question.isPresent()) {
            if(corrected == null) {
                return new ResponseEntity<>(question.get().getAnswers(), HttpStatus.OK);
            } else if (corrected.equals("true")) {
                return new ResponseEntity<>(this.definitionQuestionService.findCorrectedAnswers(questionID).get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(this.definitionQuestionService.findNotCorrectedAnswers(questionID).get(), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{questionID}/answer")
    public ResponseEntity<DefinitionAnswer> addDefinitionAnswer(@PathVariable long unitID, @PathVariable long questionID, @RequestBody DefinitionAnswer answer) {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<DefinitionQuestion> question = this.definitionQuestionService.findOne(questionID);

        if (unit.isPresent() && question.isPresent()) {
            question.get().addAnswer(answer);
            this.definitionQuestionService.save(question.get());
            return new ResponseEntity<>(answer, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{questionID}/answer/{answerID}")
    public ResponseEntity<DefinitionAnswer> updateDefinitionAnswer(
            @PathVariable long unitID,
            @PathVariable long questionID,
            @PathVariable long answerID,
            @RequestBody DefinitionAnswer newAnswer)
    {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<DefinitionQuestion> question = this.definitionQuestionService.findOne(questionID);

        if (unit.isPresent() && question.isPresent()) {
            //TODO query
            Optional<DefinitionAnswer> oldAnswer = this.definitionQuestionService.findOneAnswer(question.get(), answerID);
            if (oldAnswer.isPresent()) {
                if(newAnswer.isCorrected()) {
                    if (newAnswer.isCorrect()) {
                        question.get().increaseTotalCorrectAnswers();
                    } else {
                        question.get().increaseTotalWrongAnswers();
                    }
                }
                oldAnswer.get().update(newAnswer);
                question.get().getAnswers().remove(oldAnswer.get());
                question.get().addAnswer(oldAnswer.get());
                this.definitionQuestionService.save(question.get());
                return new ResponseEntity<>(oldAnswer.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{questionID}/uncorrectedCount")
    public ResponseEntity<Integer> getUncorrected(@PathVariable long unitID, @PathVariable long questionID) {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<DefinitionQuestion> question = this.definitionQuestionService.findOne(questionID);

        if (unit.isPresent() && question.isPresent())
            return new ResponseEntity<>(this.definitionQuestionService.findNotCorrectedAnswersCount(questionID), HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{questionID}/answer/user/{userID}")
    public ResponseEntity<List<Object>> getUserAnswers(@PathVariable long questionID, @PathVariable long userID) {
        return new ResponseEntity<>(this.definitionQuestionService.findUserAnswers(userID, questionID), HttpStatus.OK);
    }
}