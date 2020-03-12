package com.question.test.test_question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TestQuestionRepository extends JpaRepository<TestQuestion, Long> {

    List<TestQuestion> findAll();

    @Query(value = "select test_answer.id, test_answer.answer_text, test_answer.correct, test_answer.user_id from question join question_test_answers on question.id = question_test_answers.test_question_id join test_answer on question_test_answers.test_answers_id = test_answer.id join user on test_answer.user_id = user.id where question.id = ?2 and user.id = ?1", nativeQuery = true)
    List<Object> findUserAnswers(long userId, long questionId);

    @Query(value = "select DISTINCT test_answer.answer_text, COUNT(question.id) from question join question_test_answers on question.id = question_test_answers.test_question_id join test_answer on question_test_answers.test_answers_id = test_answer.id where question.id = ?1 and test_answer.correct = false group by test_answer.answer_text", nativeQuery = true)
    List<Object> findChosenAnswers(long questionId);
}