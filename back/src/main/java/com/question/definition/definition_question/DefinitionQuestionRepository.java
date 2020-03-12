package com.question.definition.definition_question;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DefinitionQuestionRepository extends JpaRepository<DefinitionQuestion, Long> {

    @Query(value = "select definition_answer.id, definition_answer.answer_text, definition_answer.correct, definition_answer.corrected, definition_answer.justification, definition_answer.user_id from question join question_definition_answers on question.id = question_definition_answers.definition_question_id join definition_answer on question_definition_answers.definition_answers_id = definition_answer.id join user on definition_answer.user_id = user.id where question.id = ?2 and user.id = ?1", nativeQuery = true)
    List<Object> findUserAnswers(long userId, long questionId);

    @Query(value = "select COUNT(question.id) from question join question_definition_answers on question.id = question_definition_answers.definition_question_id join definition_answer on question_definition_answers.definition_answers_id = definition_answer.id where definition_answer.corrected = false and question.id = ?1", nativeQuery = true)
    Integer findNotCorrectedAnswersCount(long questionId);

    /*@Query(value = "select definition_answer.id, definition_answer.answer_text, definition_answer.correct, definition_answer.justification, definition_answer.user_id from question join question_definition_answers on question.id = question_definition_answers.definition_question_id join definition_answer on question_definition_answers.definition_answers_id = definition_answer.id where question.id = ?1 and definiion_answer.corrected = true", nativeQuery = true)
    List<Object> findCorrectedAnswers(long questionId);

    @Query(value = "select definition_answer.id, definition_answer.answer_text, definition_answer.correct, definition_answer.justification, definition_answer.user_id from question join question_definition_answers on question.id = question_definition_answers.definition_question_id join definition_answer on question_definition_answers.definition_answers_id = definition_answer.id where question.id = ?1 and definiion_answer.corrected = false", nativeQuery = true)
    List<Object> findNotCorrectedAnswers(long questionId);*/
}