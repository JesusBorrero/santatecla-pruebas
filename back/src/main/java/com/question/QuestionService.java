package com.question;

import java.util.List;
import java.util.Optional;

import com.question.definition.definition_question.DefinitionQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public Question save(Question q) {
        return this.questionRepository.save(q);
    }

    public List<Question> findAll() {
        return this.questionRepository.findAll();
    }

    public Optional<Question> findOne(long id){ return this.questionRepository.findById(id);}

    public void delete(long id) {
        this.questionRepository.deleteById(id);
    }

    public List<Question> findQuestionsByBlockId(long id){
        return this.questionRepository.findByBlockId(id);
    }

    public Integer findBlockQuestionCount(long id){
        return this.questionRepository.findBlockQuestionCount(id);
    }

}
