package com.question.list;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.GeneralRestController;

import com.question.QuestionController;
import com.question.list.list_answer.ListAnswerDto;
import com.question.list.list_question.ListQuestionDto;
import com.question.list.list_answer.ListAnswer;
import com.question.list.list_question.ListQuestion;
import com.unit.Unit;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/units/{unitID}/question/list")
public class ListQuestionRestController
        extends GeneralRestController
        implements QuestionController<ListQuestionDto, ListAnswerDto> {

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    public ResponseEntity<List<ListQuestionDto>> getQuestions(@PathVariable long unitID) {
        Optional<Unit> unit = this.unitService.findOne(unitID);

        return unit.map(value -> new ResponseEntity<>(value.getListQuestions().stream()
                .map(this::convertToQuestionDto)
                .collect(Collectors.toList()), HttpStatus.OK)).orElseGet(() ->
                new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @GetMapping("/{questionID}")
    public ResponseEntity<ListQuestionDto> getQuestion(@PathVariable long unitID, @PathVariable long questionID) {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<ListQuestion> question = this.listQuestionService.findOne(questionID);

        if (unit.isPresent() && question.isPresent())
            return new ResponseEntity<>(convertToQuestionDto(question.get()), HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    public ResponseEntity<ListQuestionDto> addQuestion(@PathVariable long unitID, @RequestBody ListQuestionDto questionDto) {
        Optional<Unit> unit = this.unitService.findOne(unitID);

        ListQuestion question = convertToQuestionEntity(questionDto);

        if (unit.isPresent()) {
            this.listQuestionService.save(question);
            unit.get().addListQuestion(question);
            this.unitService.save(unit.get());
            return new ResponseEntity<>(convertToQuestionDto(question), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{questionID}")
    public ResponseEntity<ListQuestionDto> deleteQuestion(@PathVariable long unitID, @PathVariable long questionID) {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<ListQuestion> question = this.listQuestionService.findOne(questionID);

        if (unit.isPresent() && question.isPresent()) {
            this.listQuestionService.delete(questionID);
            return new ResponseEntity<>(convertToQuestionDto(question.get()), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/{questionID}")
    public ResponseEntity<ListQuestionDto> updateQuestion(
            @PathVariable long unitID,
            @PathVariable long questionID,
            @RequestBody ListQuestionDto newQuestionDto) {

        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<ListQuestion> oldQuestion = this.listQuestionService.findOne(questionID);

        ListQuestion newQuestion = convertToQuestionEntity(newQuestionDto);

        if (unit.isPresent() && oldQuestion.isPresent()) {
            oldQuestion.get().update(newQuestion);
            this.listQuestionService.save(oldQuestion.get());
            return new ResponseEntity<>(convertToQuestionDto(oldQuestion.get()), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{questionID}/answer")
    public ResponseEntity<List<ListAnswerDto>> getAnswers(@PathVariable long unitID, @PathVariable long questionID) {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<ListQuestion> question = this.listQuestionService.findOne(questionID);

        if (unit.isPresent() && question.isPresent()) {
            return new ResponseEntity<>(question.get().getListAnswers().stream()
                    .map(this::convertToAnswerDto)
                    .collect(Collectors.toList()), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{questionID}/answer")
    public ResponseEntity<ListAnswerDto> addAnswer(@PathVariable long unitID, @PathVariable long questionID, @RequestBody ListAnswerDto answerDto) {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<ListQuestion> question = this.listQuestionService.findOne(questionID);

        ListAnswer answer = convertToAnswerEntity(answerDto);

        if (unit.isPresent() && question.isPresent()) {
            question.get().addAnswer(answer);
            this.listQuestionService.save(question.get());
            return new ResponseEntity<>(convertToAnswerDto(answer), HttpStatus.CREATED);
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
    public ResponseEntity<List<ListAnswerDto>> getUserAnswers(@RequestParam long blockId,
                                                           @RequestParam long courseId,
                                                           @PathVariable long questionID,
                                                           @PathVariable long userID) {
        return new ResponseEntity<>(this.listQuestionService.findUserAnswers(questionID, userID, blockId, courseId)
                .stream()
                .map(this::convertToAnswerDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    private ListQuestionDto convertToQuestionDto(ListQuestion question) {
        ListQuestionDto dto = modelMapper.map(question, ListQuestionDto.class);
        dto.setListAnswers(question.getListAnswers()
                .stream()
                .map(this::convertToAnswerDto)
                .collect(Collectors.toList()));
        return  dto;
    }

    private ListQuestion convertToQuestionEntity(ListQuestionDto dto) {
        return modelMapper.map(dto, ListQuestion.class);
    }

    private ListAnswerDto convertToAnswerDto(ListAnswer answer) {
        return modelMapper.map(answer, ListAnswerDto.class);
    }

    private ListAnswer convertToAnswerEntity(ListAnswerDto dto) {
        return modelMapper.map(dto, ListAnswer.class);
    }
}