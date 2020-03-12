package com.question;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.GeneralRestController;

import com.itinerary.block.Block;
import com.question.definition.definition_answer.DefinitionAnswer;
import com.unit.Unit;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/units/{unitID}/question")
public class UnitQuestionRestController extends GeneralRestController {

    @GetMapping("")
    public ResponseEntity<List<Question>> getQuestions(@PathVariable long unitID) {
        Optional<Unit> unit = this.unitService.findOne(unitID);

        if (unit.isPresent()) {
            List<Question> questions = new ArrayList<>();
            questions.addAll(unit.get().getDefinitionQuestions());
            questions.addAll(unit.get().getListQuestions());
            questions.addAll(unit.get().getTestQuestions());
            return new ResponseEntity<>(questions, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{questionID}")
    public ResponseEntity<Question> getQuestion(@PathVariable long unitID, @PathVariable long questionID) {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<Question> question = this.questionService.findOne(questionID);

        if (unit.isPresent() && question.isPresent()) {
            return new ResponseEntity<>(question.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{questionID}")
    public ResponseEntity<Question> deleteQuestion(@PathVariable long unitID, @PathVariable long questionID) {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<Question> question = this.questionService.findOne(questionID);

        if (unit.isPresent() && question.isPresent()) {
            this.questionService.delete(questionID);
            return new ResponseEntity<>(question.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{questionID}/correctCount")
    public ResponseEntity<Integer> getQuestionCorrectCount(@PathVariable long unitID, @PathVariable long questionID) {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<Question> question = this.questionService.findOne(questionID);

        if (unit.isPresent() && question.isPresent()) {
            return new ResponseEntity<>(question.get().getTotalCorrectAnswers(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{questionID}/wrongCount")
    public ResponseEntity<Integer> getQuestionWrongCount(@PathVariable long unitID, @PathVariable long questionID) {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<Question> question = this.questionService.findOne(questionID);

        if (unit.isPresent() && question.isPresent()) {
            return new ResponseEntity<>(question.get().getTotalWrongAnswers(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{questionID}/addBlock/{blockID}")
    public ResponseEntity<Question> addBlockToQuestion(
            @PathVariable long unitID,
            @PathVariable long questionID,
            @PathVariable long blockID)
    {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<Question> question = this.questionService.findOne(questionID);
        Optional<Block> block = this.blockService.findOne(blockID);

        if (unit.isPresent() && question.isPresent() && block.isPresent()) {
            question.get().addBlock(block.get());
            this.questionService.save(question.get());
            return new ResponseEntity<>(question.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{questionID}/deleteBlock/{blockID}")
    public ResponseEntity<Question> deleteQuestionBlock(
            @PathVariable long unitID,
            @PathVariable long questionID,
            @PathVariable long blockID)
    {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<Question> question = this.questionService.findOne(questionID);
        Optional<Block> block = this.blockService.findOne(blockID);

        if (unit.isPresent() && question.isPresent() && block.isPresent()) {
            question.get().deleteBlock(block.get());
            this.questionService.save(question.get());
            return new ResponseEntity<>(question.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}