package com.question.test;

import com.GeneralRestController;
import com.question.QuestionController;
import com.question.test.test_answer.TestAnswerDto;
import com.question.test.test_question.TestQuestionDto;
import com.question.test.test_answer.TestAnswer;
import com.question.test.test_question.TestQuestion;
import com.unit.Unit;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/units/{unitID}/question/test")
public class TestQuestionRestController
        extends GeneralRestController
        implements QuestionController<TestQuestionDto, TestAnswerDto> {

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    public ResponseEntity<List<TestQuestionDto>> getQuestions(@PathVariable long unitID) {
        Optional<Unit> unit = this.unitService.findOne(unitID);

        return unit.map(value -> new ResponseEntity<>(value.getTestQuestions().stream()
                .map(this::convertToQuestionDto)
                .collect(Collectors.toList()), HttpStatus.OK)).orElseGet(() ->
                new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @GetMapping("/{questionID}")
    public ResponseEntity<TestQuestionDto> getQuestion(@PathVariable long unitID, @PathVariable long questionID) {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<TestQuestion> question = this.testQuestionService.findOne(questionID);



        if (unit.isPresent() && question.isPresent())
            return new ResponseEntity<>(convertToQuestionDto(question.get()), HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    public ResponseEntity<TestQuestionDto> addQuestion(@PathVariable long unitID, @RequestBody TestQuestionDto questionDto) {
        Optional<Unit> unit = this.unitService.findOne(unitID);

        TestQuestion question = convertToQuestionEntity(questionDto);

        if (unit.isPresent()) {
            this.testQuestionService.save(question);
            unit.get().addTestQuestion(question);
            this.unitService.save(unit.get());
            return new ResponseEntity<>(convertToQuestionDto(question), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{questionID}")
    public ResponseEntity<TestQuestionDto> deleteQuestion(@PathVariable long unitID, @PathVariable long questionID) {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<TestQuestion> question = this.testQuestionService.findOne(questionID);

        if (unit.isPresent() && question.isPresent()) {
            this.testQuestionService.delete(questionID);
            return new ResponseEntity<>(convertToQuestionDto(question.get()), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/{questionID}")
    public ResponseEntity<TestQuestionDto> updateQuestion(
            @PathVariable long unitID,
            @PathVariable long questionID,
            @RequestBody TestQuestionDto newQuestionDto) {

        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<TestQuestion> oldQuestion = this.testQuestionService.findOne(questionID);

        TestQuestion newQuestion = convertToQuestionEntity(newQuestionDto);

        if (unit.isPresent() && oldQuestion.isPresent()) {
            oldQuestion.get().update(newQuestion);
            this.testQuestionService.save(oldQuestion.get());
            return new ResponseEntity<>(convertToQuestionDto(oldQuestion.get()), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{questionID}/answer")
    public ResponseEntity<List<TestAnswerDto>> getAnswers(@PathVariable long unitID, @PathVariable long questionID) {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<TestQuestion> question = this.testQuestionService.findOne(questionID);

        if (unit.isPresent() && question.isPresent()) {
            return new ResponseEntity<>(question.get().getTestAnswers().stream()
                    .map(this::convertToAnswerDto)
                    .collect(Collectors.toList()), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{questionID}/answer")
    public ResponseEntity<TestAnswerDto> addAnswer(
            @PathVariable long unitID,
            @PathVariable long questionID,
            @RequestBody TestAnswerDto answerDto) {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<TestQuestion> question = this.testQuestionService.findOne(questionID);

        TestAnswer answer = convertToAnswerEntity(answerDto);

        if (unit.isPresent() && question.isPresent()) {
            question.get().addAnswer(answer);
            this.testQuestionService.save(question.get());
            return new ResponseEntity<>(convertToAnswerDto(answer), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{questionID}/answer/user/{userID}")
    public ResponseEntity<List<TestAnswerDto>> getUserAnswers(@RequestParam long blockId,
                                                           @RequestParam long courseId,
                                                           @PathVariable long questionID,
                                                           @PathVariable long userID) {
        return new ResponseEntity<>(
                this.testQuestionService.findUserAnswers(questionID, userID, blockId, courseId)
                        .stream()
                        .map(this::convertToAnswerDto)
                        .collect(Collectors.toList()), HttpStatus.OK);
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

    private TestQuestionDto convertToQuestionDto(TestQuestion question) {
        TestQuestionDto dto = modelMapper.map(question, TestQuestionDto.class);
        dto.setTestAnswers(question.getTestAnswers()
                .stream()
                .map(this::convertToAnswerDto)
                .collect(Collectors.toList()));
        return  dto;
    }

    private TestQuestion convertToQuestionEntity(TestQuestionDto dto) {
        return modelMapper.map(dto, TestQuestion.class);
    }

    private TestAnswerDto convertToAnswerDto(TestAnswer answer) {
        return modelMapper.map(answer, TestAnswerDto.class);
    }

    private TestAnswer convertToAnswerEntity(TestAnswerDto dto) {
        return modelMapper.map(dto, TestAnswer.class);
    }
}