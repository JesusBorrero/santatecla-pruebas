package com.question.test.test_answer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestAnswerRepository extends JpaRepository<TestAnswer, Long> {

    @Query(value = "SELECT * FROM question JOIN question_test_answers ON " +
            "question.id = question_test_answers.test_question_id JOIN test_answer ON " +
            "question_test_answers.test_answers_id = test_answer.id JOIN user ON " +
            "test_answer.user_id = user.id " +
            "WHERE question.id = ?1 AND user.id = ?2 AND test_answer.block_id = ?3 " +
            "AND test_answer.course_id = ?4", nativeQuery = true)
    List<TestAnswer> findUserAnswers(long questionId, long userId, long blockId, long courseId);

}
