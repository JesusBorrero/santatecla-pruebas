package com.question.list.list_question;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ListQuestionService {
    @Autowired
    private ListQuestionRepository listRepository;

    public ListQuestion save(ListQuestion q) {
        return this.listRepository.save(q);
    }

    public void delete(long id) {
        this.listRepository.deleteById(id);
    }

    public List<ListQuestion> findAll() {
        return this.listRepository.findAll();
    }

    public Optional<ListQuestion> findOne(long id) {
        return this.listRepository.findById(id);
    }

    public List<Object> findUserAnswers(long userId, long questionId) {
        return this.listRepository.findUserAnswers(userId, questionId);
    }

    public List<Object> findChosenWrongAnswersCount(long id){
        return this.listRepository.findChosenAnswers(id);
    }
}