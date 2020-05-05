package com.question.list.list_question;

import java.util.List;
import java.util.Optional;

import com.question.list.list_answer.ListAnswer;
import com.question.list.list_answer.ListAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ListQuestionService {

    @Autowired
    private ListQuestionRepository listRepository;

    @Autowired
    private ListAnswerRepository answerRepository;

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

    public List<ListAnswer> findUserAnswers(long questionId, long userId, long blockId, long courseId) {
        return this.answerRepository.findUserAnswers(questionId, userId, blockId, courseId);
    }

    public List<Object> findChosenWrongAnswersCount(long id){
        return this.listRepository.findChosenAnswers(id);
    }
}