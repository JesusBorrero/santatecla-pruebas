package com.question.list.list_answer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListAnswerRepository extends JpaRepository<ListAnswer, Long> {

    @Query(value = "SELECT * FROM question JOIN question_list_answers ON " +
            "question.id = question_list_answers.list_question_id JOIN list_answer ON " +
            "question_list_answers.list_answers_id = list_answer.id JOIN user ON " +
            "list_answer.user_id = user.id " +
            "WHERE question.id = ?1 AND user.id = ?2 AND list_answer.block_id = ?3 " +
            "AND list_answer.course_id = ?4", nativeQuery = true)
    List<ListAnswer> findUserAnswers(long questionId, long userId, long blockId, long courseId);

}
