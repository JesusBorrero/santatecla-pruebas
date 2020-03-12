package com.question.test.test_question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestQuestionService {

    @Autowired
    private TestQuestionRepository testRepository;

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

    public List<Object> findUserAnswers(long userId, long questionId){
        return this.testRepository.findUserAnswers(userId, questionId);
    }

    public List<Object> findChosenWrongAnswersCount(long id){
        return this.testRepository.findChosenAnswers(id);
    }
}