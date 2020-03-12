package com.course;

import com.course.items.StudentProgressItem;
import com.itinerary.block.Block;
import com.itinerary.module.Module;
import com.question.Question;
import com.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public Optional<Course> findOne(long id) {
        return courseRepository.findById(id);
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public void save(Course course) {
        courseRepository.save(course);
    }

    public void delete(long id) {
        courseRepository.deleteById(id);
    }

    public List<Course> findTeachingCourses(long teacherId){
        return this.courseRepository.findByTeacherId(teacherId);
    }

    public List<Long> findUserCourses(long id){
        return this.courseRepository.findUserCourses(id);
    }

    public List<Course> searchCourseByNameContaining(String name){
        return this.courseRepository.findByNameContaining(name);
    }

    public int findUserRealization(Long blockId, Long userId, Long courseId){
        return this.courseRepository.findUserListAnswerDistinctCount(blockId, userId, courseId) + this.courseRepository.findUserTestAnswerDistinctCount(blockId, userId, courseId) +
                this.courseRepository.findUserDefinitionAnswerDistinctCount(blockId, userId, courseId);
    }

    public Double findBlockRealization(List<User> students, int questionCount, long blockId, long courseId){
        double sumRealization = 0;
        for (User u : students){
            sumRealization += (double)findUserRealization(blockId, u.getId(), courseId) / questionCount;
        }
        return (sumRealization/students.size()) * 100;
    }

    private double findUserGrade(Long blockId, Long userId, Long courseId){
        double result = ((double) (this.courseRepository.findUserCorrectDefinitionAnswers(blockId, userId, courseId) + this.courseRepository.findUserCorrectListAnswers(blockId, userId, courseId) +
                this.courseRepository.findUserCorrectTestAnswers(blockId, userId, courseId))) / (this.courseRepository.findUserDefinitionAnswers(blockId, userId, courseId) +
                this.courseRepository.findUserListAnswers(blockId, userId, courseId) + this.courseRepository.findUserTestAnswers(blockId, userId, courseId));

        if(Double.isNaN(result)){
            return result;
        }

        return result*10;
    }

    public double findBlockGrade(List<User> students, long blockId, long courseId){
        double sumGrade = 0;
        int size = students.size();
        double userGrade;
        for (User u : students){
            userGrade = findUserGrade(blockId, u.getId(), courseId);
            if(Double.isNaN(userGrade)){
                size -= 1;
            }
            else {
                sumGrade += userGrade;
            }
        }
        double result = sumGrade/size;

        if (Double.isNaN(result)){
            return 0;
        }

        return result;
    }

    public double findUserQuestionGrade(Long userId, long blockId, long courseId, Question question){
        double result;
        if (question.getSubtype().equals("ListQuestion")){
            result = (double)this.courseRepository.findUserListQuestionCorrectAnswers(blockId, userId, courseId, question.getId()) /
                    this.courseRepository.findUserListQuestionAnswers(blockId, userId, courseId, question.getId());
        }
        else if(question.getSubtype().equals("TestQuestion")){
            result = (double)this.courseRepository.findUserTestQuestionCorrectAnswers(blockId, userId, courseId, question.getId()) /
                    this.courseRepository.findUserTestQuestionAnswers(blockId, userId, courseId, question.getId());
        }
        else {
            result = (double)this.courseRepository.findUserDefinitionQuestionCorrectAnswers(blockId, userId, courseId, question.getId()) /
                    this.courseRepository.findUserDefinitionQuestionAnswers(blockId, userId, courseId, question.getId());
        }

        if (Double.isNaN(result)){
            return result;
        }

        return result*10;
    }

    public void buildInitialStudentGradeGroupedResult(ArrayList<StudentProgressItem> result){
        StudentProgressItem item;
        for (int i = 0; i<11; i++){
            item = new StudentProgressItem("" + i);
            item.setAverage(0);
            result.add(item);
        }
    }

    public void buildStudentGradeGroupedResult(ArrayList<StudentProgressItem> result, ArrayList<StudentProgressItem> averages){
        for(StudentProgressItem studentProgressItem : averages){
            result.get((int) Math.floor(studentProgressItem.getAverage())).setAverage(result.get((int) Math.floor(studentProgressItem.getAverage())).getAverage() + 1);
        }
    }
}
