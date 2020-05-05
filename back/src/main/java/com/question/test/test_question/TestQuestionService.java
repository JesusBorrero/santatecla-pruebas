package com.question.test.test_question;

import com.question.test.test_answer.TestAnswer;
import com.question.test.test_answer.TestAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestQuestionService {

    @Autowired
    private TestQuestionRepository testRepository;

    @Autowired
    private TestAnswerRepository answerRepository;

    public TestQuestion save(TestQuestion q) {
        return this.testRepository.save(q);
    }

    public void delete(long id) {
        this.testRepository.deleteById(id);
    }

    public List<TestQuestion> findAll() {
        return this.testRepository.findAll();
    }

    public Optional<TestQuestion> findOne(long id){
        return this.testRepository.findById(id);
    }

    public List<TestAnswer> findUserAnswers(long questionId, long userId, long blockId, long courseId){
        return this.answerRepository.findUserAnswers(questionId, userId, blockId, courseId);
    }

    public List<Object> findChosenWrongAnswersCount(long id){
        return this.testRepository.findChosenAnswers(id);
    }
}