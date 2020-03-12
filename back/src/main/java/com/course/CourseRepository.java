package com.course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findAll();
    List<Course> findByTeacherId(long id);
    List<Course> findByNameContaining(String name);

    @Query(value = "SELECT course.id FROM course JOIN course_students ON course.id = course_students.course_id JOIN user ON course_students.students_id = user.id WHERE user.id = ?1", nativeQuery = true)
    List<Long> findUserCourses(long userId);


    // DISTINCT COUNT queries are used to check how many questions the student has answered in a module

    @Query(value = "select COUNT( DISTINCT question.id) from question join question_blocks on question.id = question_blocks.question_id join question_list_answers on" +
            " question.id = question_list_answers.list_question_id join list_answer on question_list_answers.list_answers_id = list_answer.id join user on" +
            " list_answer.user_id = user.id where question_blocks.blocks_id = ?1 and user_id = ?2 and list_answer.block_id = ?1 and list_answer.course_id = ?3", nativeQuery = true)
    Integer findUserListAnswerDistinctCount(Long blockId, Long userId, Long courseId);

    @Query(value = "select COUNT( DISTINCT question.id) from question join question_blocks on question.id = question_blocks.question_id join question_test_answers on" +
            " question.id = question_test_answers.test_question_id join test_answer on question_test_answers.test_answers_id = test_answer.id join user on" +
            " test_answer.user_id = user.id where question_blocks.blocks_id = ?1 and user_id = ?2 and test_answer.course_id = ?3 and test_answer.block_id = ?1", nativeQuery = true)
    Integer findUserTestAnswerDistinctCount(Long blockId, Long userId, Long courseId);

    @Query(value = "select COUNT( DISTINCT question.id) from question join question_blocks on question.id = question_blocks.question_id join question_definition_answers on" +
            " question.id = question_definition_answers.definition_question_id join definition_answer on question_definition_answers.definition_answers_id = definition_answer.id join user on" +
            " definition_answer.user_id = user.id where question_blocks.blocks_id = ?1 and user_id = ?2 and definition_answer.block_id = ?1 and definition_answer.course_id = ?3", nativeQuery = true)
    Integer findUserDefinitionAnswerDistinctCount(Long blockId, Long userId, Long courseId);


    // Correct queries are used to check how many correct answers the student has in a module

    @Query(value = "select COUNT(question.id) from question join question_blocks on question.id = question_blocks.question_id join question_list_answers on" +
            " question.id = question_list_answers.list_question_id join list_answer on question_list_answers.list_answers_id = list_answer.id join user on" +
            " list_answer.user_id = user.id where user.id = ?2 and question_blocks.blocks_id = ?1 and list_answer.correct = true" +
            " and list_answer.block_id = ?1 and list_answer.course_id = ?3", nativeQuery = true)
    Integer findUserCorrectListAnswers(Long blockId, Long userId, Long courseId);

    @Query(value = "select COUNT(question.id) from question join question_blocks on question.id = question_blocks.question_id join question_test_answers on" +
            " question.id = question_test_answers.test_question_id join test_answer on question_test_answers.test_answers_id = test_answer.id join user on" +
            " test_answer.user_id = user.id where user.id = ?2 and question_blocks.blocks_id = ?1 and test_answer.correct = true and test_answer.course_id = ?3" +
            " and test_answer.block_id = ?1", nativeQuery = true)
    Integer findUserCorrectTestAnswers(Long blockId, Long userId, Long courseId);

    @Query(value = "select COUNT(question.id) from question join question_blocks on question.id = question_blocks.question_id join question_definition_answers on" +
            " question.id = question_definition_answers.definition_question_id join definition_answer on question_definition_answers.definition_answers_id = definition_answer.id" +
            " join user on definition_answer.user_id = user.id where user.id = ?2 and question_blocks.blocks_id = ?1 and definition_answer.correct = true and" +
            " definition_answer.block_id = ?1 and definition_answer.course_id = ?3", nativeQuery = true)
    Integer findUserCorrectDefinitionAnswers(Long blockId, Long userId, Long courseId);

    // Answers queries are used to check how many answers the student has in a module

    @Query(value = "select COUNT(question.id) from question join question_blocks on question.id = question_blocks.question_id join question_definition_answers on" +
            " question.id = question_definition_answers.definition_question_id join definition_answer on question_definition_answers.definition_answers_id = definition_answer.id" +
            " join user on definition_answer.user_id = user.id where user.id = ?2 and question_blocks.blocks_id = ?1 and definition_answer.block_id = ?1 and" +
            " definition_answer.course_id = ?3", nativeQuery = true)
    Integer findUserDefinitionAnswers(Long blockId, Long userId, Long courseId);

    @Query(value = "select COUNT(question.id) from question join question_blocks on question.id = question_blocks.question_id join question_list_answers on" +
            " question.id = question_list_answers.list_question_id join list_answer on question_list_answers.list_answers_id = list_answer.id join user on" +
            " list_answer.user_id = user.id where user.id = ?2 and question_blocks.blocks_id = ?1 and list_answer.block_id = ?1 and list_answer.course_id = ?3", nativeQuery = true)
    Integer findUserListAnswers(Long blockId, Long userId, Long courseId);

    @Query(value = "select COUNT(question.id) from question join question_blocks on question.id = question_blocks.question_id join question_test_answers on" +
            " question.id = question_test_answers.test_question_id join test_answer on question_test_answers.test_answers_id = test_answer.id join user on" +
            " test_answer.user_id = user.id where user.id = ?2 and question_blocks.blocks_id = ?1  and test_answer.course_id = ?3 and test_answer.block_id = ?1", nativeQuery = true)
    Integer findUserTestAnswers(Long blockId, Long userId, Long courseId);

    // Question correct queries are used to check how many correct answers the student has in a given question

    @Query(value = "select COUNT(question.id) from question join question_list_answers on" +
            " question.id = question_list_answers.list_question_id join list_answer on question_list_answers.list_answers_id = list_answer.id join user on" +
            " list_answer.user_id = user.id where user.id = ?2 and list_answer.correct = true" +
            " and list_answer.block_id = ?1 and list_answer.course_id = ?3 and question.id = ?4", nativeQuery = true)
    Integer findUserListQuestionCorrectAnswers(Long blockId, Long userId, Long courseId, Long questionId);

    @Query(value = "select COUNT(question.id) from question join question_test_answers on" +
            " question.id = question_test_answers.test_question_id join test_answer on question_test_answers.test_answers_id = test_answer.id join user on" +
            " test_answer.user_id = user.id where user.id = ?2 and test_answer.correct = true" +
            " and test_answer.block_id = ?1 and test_answer.course_id = ?3 and question.id = ?4", nativeQuery = true)
    Integer findUserTestQuestionCorrectAnswers(Long blockId, Long userId, Long courseId, Long questionId);

    @Query(value = "select COUNT(question.id) from question join question_definition_answers on" +
            " question.id = question_definition_answers.definition_question_id join definition_answer on question_definition_answers.definition_answers_id = definition_answer.id join user on" +
            " definition_answer.user_id = user.id where user.id = ?2 and definition_answer.correct = true" +
            " and definition_answer.block_id = ?1 and definition_answer.course_id = ?3 and question.id = ?4", nativeQuery = true)
    Integer findUserDefinitionQuestionCorrectAnswers(Long blockId, Long userId, Long courseId, Long questionId);

    // Question Answers are used to check how many answer the student has in a given question

    @Query(value = "select COUNT(question.id) from question join question_list_answers on" +
            " question.id = question_list_answers.list_question_id join list_answer on question_list_answers.list_answers_id = list_answer.id join user on" +
            " list_answer.user_id = user.id where user.id = ?2 and list_answer.block_id = ?1 and list_answer.course_id = ?3" +
            " and question.id = ?4", nativeQuery = true)
    Integer findUserListQuestionAnswers(Long blockId, Long userId, Long courseId, Long questionId);

    @Query(value = "select COUNT(question.id) from question join question_test_answers on" +
            " question.id = question_test_answers.test_question_id join test_answer on question_test_answers.test_answers_id = test_answer.id join user on" +
            " test_answer.user_id = user.id where user.id = ?2 and test_answer.block_id = ?1 and test_answer.course_id = ?3" +
            " and question.id = ?4", nativeQuery = true)
    Integer findUserTestQuestionAnswers(Long blockId, Long userId, Long courseId, Long questionId);

    @Query(value = "select COUNT(question.id) from question join question_definition_answers on" +
            " question.id = question_definition_answers.definition_question_id join definition_answer on question_definition_answers.definition_answers_id = definition_answer.id join user on" +
            " definition_answer.user_id = user.id where user.id = ?2" +
            " and definition_answer.block_id = ?1 and definition_answer.course_id = ?3 and question.id = ?4", nativeQuery = true)
    Integer findUserDefinitionQuestionAnswers(Long blockId, Long userId, Long courseId, Long questionId);
}
