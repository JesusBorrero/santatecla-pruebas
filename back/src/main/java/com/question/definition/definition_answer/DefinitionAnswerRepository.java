package com.question.definition.definition_answer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DefinitionAnswerRepository extends JpaRepository<DefinitionAnswer, Long> {

    @Query(value = "SELECT * FROM question JOIN question_definition_answers ON " +
            "question.id = question_definition_answers.definition_question_id JOIN definition_answer ON " +
            "question_definition_answers.definition_answers_id = definition_answer.id JOIN user ON " +
            "definition_answer.user_id = user.id " +
            "WHERE question.id = ?1 AND user.id = ?2 AND definition_answer.block_id = ?3 " +
            "AND definition_answer.course_id = ?4", nativeQuery = true)
    List<DefinitionAnswer> findUserAnswers(long questionId, long userId, long blockId, long courseId);

}
