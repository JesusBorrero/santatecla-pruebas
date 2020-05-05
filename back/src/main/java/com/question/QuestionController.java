package com.question;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Api
public interface QuestionController<T extends Question, E extends Answer> {

    @ApiOperation(value = "Return all the question of the unit with the requested unit id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "All the questions"),
            @ApiResponse(code = 404, message = "Unit not found")
    })
    ResponseEntity<List<T>> getQuestions(long unitID);

    @ApiOperation(value = "Return the question with the requested id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Question"),
            @ApiResponse(code = 404, message = "Question or unit not found")
    })
    ResponseEntity<T> getQuestion(long unitID, long questionID);

    @ApiOperation(value = "Add a question to the provided Unit (indicated by Id)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Added question"),
            @ApiResponse(code = 404, message = "Unit not found")
    })
    ResponseEntity<T> addQuestion(long unitID, T question);

    @ApiOperation(value = "Delete the question with the requested id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Deleted question"),
            @ApiResponse(code = 404, message = "Question or unit not found")
    })
    ResponseEntity<T> deleteQuestion(long unitID, long questionID);

    @ApiOperation(value = "Update the question with the requested id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Updated question"),
            @ApiResponse(code = 404, message = "Question or unit not found")
    })
    ResponseEntity<T> updateQuestion(long unitID, long questionID, T newQuestion);

    /*@ApiOperation(value = "Return all the answer to the question with the requested question id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "All the answers to a question"),
            @ApiResponse(code = 404, message = "Question or unit not found")
    })
    ResponseEntity<List<E>> getAnswers(long unitID, long questionID, String corrected);*/

    @ApiOperation(value = "Add a answer to a question with requested id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Added answer"),
            @ApiResponse(code = 404, message = "Question or unit not found")
    })
    ResponseEntity<E> addAnswer(long unitID, long questionID, E answer);

    /*@ApiOperation(value = "Update the answer to the question with the requested id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Updated answer"),
            @ApiResponse(code = 404, message = "Answer, question or unit not found")
    })
    ResponseEntity<E> updateAnswer(
            @PathVariable long unitID,
            @PathVariable long questionID,
            @PathVariable long answerID,
            @RequestBody E newAnswer);*/

    @ApiOperation(value = "Return the user's answers to a question , within a specific course an block")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User's answers"),
            @ApiResponse(code = 404, message = "Question, user, block or course not found")
    })
    ResponseEntity<List<E>> getUserAnswers(@RequestParam long blockId,
                                                          @RequestParam long courseId,
                                                          @PathVariable long questionID,
                                                          @PathVariable long userID);


}
