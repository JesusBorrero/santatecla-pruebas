package com.question.definition;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.GeneralRestController;
import com.question.QuestionController;
import com.question.definition.definition_answer.DefinitionAnswer;
import com.question.definition.definition_question.DefinitionQuestion;

import com.question.definition.definition_answer.DefinitionAnswerDto;
import com.question.definition.definition_question.DefinitionQuestionDto;
import com.unit.Unit;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/units/{unitID}/question/definition")
public class DefinitionQuestionRestController
        extends GeneralRestController
        implements QuestionController<DefinitionQuestionDto, DefinitionAnswerDto> {

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    public ResponseEntity<List<DefinitionQuestionDto>> getQuestions(@PathVariable long unitID) {
        Optional<Unit> unit = this.unitService.findOne(unitID);

        if (unit.isPresent()) {
            List<DefinitionQuestion> definitionQuestions = unit.get().getDefinitionQuestions();
            return new ResponseEntity<>(definitionQuestions.stream()
                    .map(this::convertToQuestionDto)
                    .collect(Collectors.toList()),
                    HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{questionID}")
    public ResponseEntity<DefinitionQuestionDto> getQuestion(@PathVariable long unitID, @PathVariable long questionID) {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<DefinitionQuestion> question = this.definitionQuestionService.findOne(questionID);

        if (unit.isPresent() && question.isPresent())
            return new ResponseEntity<>(convertToQuestionDto(question.get()), HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    public ResponseEntity<DefinitionQuestionDto> addQuestion(@PathVariable long unitID, @RequestBody DefinitionQuestionDto questionDto) {
        Optional<Unit> unit = this.unitService.findOne(unitID);

        DefinitionQuestion question = convertToQuestionEntity(questionDto);

        if (unit.isPresent()) {
            this.definitionQuestionService.save(question);
            unit.get().addDefinitionQuestion(question);
            this.unitService.save(unit.get());
            DefinitionQuestionDto dtoReturned = modelMapper.map(questionDto, DefinitionQuestionDto.class);
            return new ResponseEntity<>(dtoReturned, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{questionID}")
    public ResponseEntity<DefinitionQuestionDto> deleteQuestion(@PathVariable long unitID, @PathVariable long questionID) {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<DefinitionQuestion> question = this.definitionQuestionService.findOne(questionID);

        if (unit.isPresent() && question.isPresent()) {
            this.definitionQuestionService.delete(questionID);
            return new ResponseEntity<>(convertToQuestionDto(question.get()), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{questionID}")
    public ResponseEntity<DefinitionQuestionDto> updateQuestion(@PathVariable long unitID, @PathVariable long questionID, @RequestBody DefinitionQuestionDto newQuestionDto) {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<DefinitionQuestion> oldQuestion = this.definitionQuestionService.findOne(questionID);

        DefinitionQuestion newQuestion = convertToQuestionEntity(newQuestionDto);

        if (unit.isPresent() && oldQuestion.isPresent()) {
            oldQuestion.get().update(newQuestion);
            this.definitionQuestionService.save(oldQuestion.get());
            return new ResponseEntity<>(convertToQuestionDto(oldQuestion.get()), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{questionID}/answer")
    public ResponseEntity<List<DefinitionAnswerDto>> getAnswers (
            @PathVariable long unitID,
            @PathVariable long questionID,
            @RequestParam(required = false) String corrected) {

        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<DefinitionQuestion> question = this.definitionQuestionService.findOne(questionID);

        if (unit.isPresent() && question.isPresent()) {
            if(corrected == null) {
                return new ResponseEntity<>(question.get().getAnswers().stream()
                        .map(this::convertToAnswerDto)
                        .collect(Collectors.toList()), HttpStatus.OK);
            } else if (corrected.equals("true")) {
                Optional<List<DefinitionAnswer>> optional = this.definitionQuestionService.findCorrectedAnswers(questionID);
                if(optional.isPresent()) {
                    return new ResponseEntity<>(optional.get().stream()
                            .map(this::convertToAnswerDto)
                            .collect(Collectors.toList()), HttpStatus.OK);
                }
            } else {
                Optional<List<DefinitionAnswer>> optional = this.definitionQuestionService.findNotCorrectedAnswers(questionID);
                if(optional.isPresent()){
                    return new ResponseEntity<>(optional.get().stream()
                            .map(this::convertToAnswerDto)
                            .collect(Collectors.toList()), HttpStatus.OK);
                }
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{questionID}/answer")
    public ResponseEntity<DefinitionAnswerDto> addAnswer(@PathVariable long unitID, @PathVariable long questionID, @RequestBody DefinitionAnswerDto answerDto) {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<DefinitionQuestion> question = this.definitionQuestionService.findOne(questionID);

        DefinitionAnswer answer = convertToAnswerEntity(answerDto);

        if (unit.isPresent() && question.isPresent()) {
            question.get().addAnswer(answer);
            this.definitionQuestionService.save(question.get());
            return new ResponseEntity<>(convertToAnswerDto(answer), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{questionID}/answer/{answerID}")
    public ResponseEntity<DefinitionAnswerDto> updateAnswer(
            @PathVariable long unitID,
            @PathVariable long questionID,
            @PathVariable long answerID,
            @RequestBody DefinitionAnswerDto newAnswerDto)
    {
        Optional<Unit> unit = this.unitService.findOne(unitID);
        Optional<DefinitionQuestion> question = this.definitionQuestionService.findOne(questionID);

        DefinitionAnswer newAnswer = convertToAnswerEntity(newAnswerDto);

        if (unit.isPresent() && question.isPresent()) {
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
                return new ResponseEntity<>(convertToAnswerDto(oldAnswer.get()), HttpStatus.OK);
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
    public ResponseEntity<List<DefinitionAnswerDto>> getUserAnswers(@RequestParam long blockId,
                                                                 @RequestParam long courseId,
                                                                 @PathVariable long questionID,
                                                                 @PathVariable long userID) {
        return new ResponseEntity<>(
                this.definitionQuestionService.findUserAnswers(questionID, userID, blockId, courseId).stream()
                        .map(this::convertToAnswerDto)
                        .collect(Collectors.toList())
                , HttpStatus.OK);
    }

    private DefinitionQuestionDto convertToQuestionDto(DefinitionQuestion definitionQuestion) {
        DefinitionQuestionDto dto = modelMapper.map(definitionQuestion, DefinitionQuestionDto.class);
        dto.setAnswers(definitionQuestion.getAnswers()
                .stream()
                .map(this::convertToAnswerDto)
                .collect(Collectors.toList()));
        return dto;
    }

    private DefinitionQuestion convertToQuestionEntity(DefinitionQuestionDto dto) {
        return modelMapper.map(dto, DefinitionQuestion.class);
    }

    private DefinitionAnswerDto convertToAnswerDto(DefinitionAnswer definitionAnswer) {
        return modelMapper.map(definitionAnswer, DefinitionAnswerDto.class);
    }

    private DefinitionAnswer convertToAnswerEntity(DefinitionAnswerDto dto) {
        return modelMapper.map(dto, DefinitionAnswer.class);
    }
}