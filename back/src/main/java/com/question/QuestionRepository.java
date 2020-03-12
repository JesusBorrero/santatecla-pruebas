package com.question;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findAll();

    @Query(value = "SELECT * from question join question_blocks on question.id = question_blocks.question_id where question_blocks.blocks_id = ?1", nativeQuery = true)
    List<Question> findByBlockId(long id);

    @Query(value = "SELECT COUNT(question.id) from question join question_blocks on question.id = question_blocks.question_id where question_blocks.blocks_id = ?1", nativeQuery = true)
    Integer findBlockQuestionCount(long id);

}