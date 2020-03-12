package com.course;

import com.GeneralRestController;
import com.course.items.ProgressItem;
import com.course.items.ModuleFormat;
import com.course.items.StudentProgressItem;
import com.itinerary.block.Block;
import com.itinerary.lesson.Lesson;
import com.itinerary.module.Module;
import com.question.Question;
import com.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/course")
public class CourseRestController extends GeneralRestController {

    @GetMapping(value="/")
    public ResponseEntity<List<Course>> getCourses(){
        return new ResponseEntity<>(this.courseService.findAll(), HttpStatus.OK);
    }

    @PostMapping(value="/")
    public ResponseEntity<Course> createCourse(@RequestBody Course course){
        this.courseService.save(course);
        return new ResponseEntity<>(course, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Course> editCourse(@PathVariable long id, @RequestBody Course course){
        Optional<Course> optional = this.courseService.findOne(id);

        if(optional.isPresent()){
            optional.get().update(course);
            this.courseService.save(optional.get());
            return new ResponseEntity<>(optional.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Course> deleteCourse(@PathVariable long id){
        Optional<Course> optional = this.courseService.findOne(id);

        if (optional.isPresent()){
            this.courseService.delete(id);
            return new ResponseEntity<>(optional.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="/user/{id}")
    public ResponseEntity<List<Course>> getUserCourses(@PathVariable long id){
        List<Course> courses = new ArrayList<>();
        for(Long courseId : this.courseService.findUserCourses(id)){
            Optional<Course> optional = this.courseService.findOne(courseId);
            if(optional.isPresent()) {
                courses.add(optional.get());
            }
        }
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping(value="/teacher/{id}")
    public ResponseEntity<List<Course>> getTeacherCourses(@PathVariable long id){
        return new ResponseEntity<>(this.courseService.findTeachingCourses(id), HttpStatus.OK);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<Course> getCourse(@PathVariable long id){
        Optional<Course> optional = this.courseService.findOne(id);
        if(optional.isPresent()){
            return new ResponseEntity<>(optional.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value="/{id}/students/")
    public ResponseEntity<Course> addStudent(@PathVariable long id, @RequestBody User newStudent){
        Optional<Course> optional = this.courseService.findOne(id);
        if(optional.isPresent()){
            optional.get().addStudent(newStudent);
            this.courseService.save(optional.get());
            return new ResponseEntity<>(optional.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value="/search/{name}")
    public ResponseEntity<List<Course>> searchCourseByNameContaining(@PathVariable String name){
        List<Course> courses = this.courseService.searchCourseByNameContaining(name);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping(value="/{courseId}/module/progress")
    public ResponseEntity<List<ProgressItem>> getModuleProgress(@PathVariable long courseId){
        Optional<Course> optional = this.courseService.findOne(courseId);
        ArrayList<Block> questionBlocks = new ArrayList<>();
        ArrayList<ProgressItem> result = new ArrayList<>();
        ArrayList<Double> values;

        if(optional.isPresent()){
            findBlocksWithQuestionRecursive(optional.get().getModule(), questionBlocks);

            for (Block b : questionBlocks){
                values = new ArrayList<>();

                int questionCount = this.questionService.findBlockQuestionCount(b.getId());
                double moduleRealization = this.courseService.findBlockRealization(optional.get().getStudents(), questionCount, b.getId(), courseId);
                double blockGrade = this.courseService.findBlockGrade(optional.get().getStudents(), b.getId(), courseId);
                values.add(moduleRealization);
                values.add(blockGrade);


                result.add(new ProgressItem(b.getName(), values));
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private void findBlocksWithQuestionRecursive(Block block, List<Block> result){
        if(this.questionService.findQuestionsByBlockId(block.getId()).size() > 0){
            result.add(block);
        }

        if(block instanceof Module) {
            Module module = (Module) block;
            for (Block b : module.getBlocks()) {
                findBlocksWithQuestionRecursive(b, result);
            }
        }
    }

    @GetMapping(value = "/{courseId}/students/progress")
    public ResponseEntity<List<StudentProgressItem>> getStudentsProgress(@PathVariable long courseId){
        Optional<Course> optional = this.courseService.findOne(courseId);
        ArrayList<Block> questionBlocks = new ArrayList<>();
        double sumQuestionAux;
        double sumModuleAux;
        double average;
        double sumRealization;
        int size;
        double userGrade;
        double gradeAux;
        int answeredCount;
        int questionCount;

        List<Question> questions;
        StudentProgressItem item;
        ArrayList<StudentProgressItem> result = new ArrayList<>();

        if(optional.isPresent()){
            findBlocksWithQuestionRecursive(optional.get().getModule(), questionBlocks);

            for (User u : optional.get().getStudents()){
                item = new StudentProgressItem(u.getName());
                sumModuleAux = 0;
                sumRealization = 0;
                answeredCount = 0;
                questionCount = 0;
                for (Block b : questionBlocks){
                    questions = this.questionService.findQuestionsByBlockId(b.getId());
                    questionCount += questions.size();
                    size = questions.size();
                    sumQuestionAux = 0;
                    for (Question q: questions){
                        userGrade = this.courseService.findUserQuestionGrade(u.getId(), b.getId(), courseId, q);
                        if(!Double.isNaN(userGrade)) {
                            sumQuestionAux += this.courseService.findUserQuestionGrade(u.getId(), b.getId(), courseId, q);
                            answeredCount++;
                        }
                        else {
                            size -= 1;
                        }
                    }
                    gradeAux = sumQuestionAux / size;
                    if(!Double.isNaN(gradeAux)){
                        sumModuleAux += gradeAux;
                    }
                    sumRealization += this.courseService.findUserRealization(b.getId(), u.getId(), courseId);
                }
                average = sumModuleAux / answeredCount;

                if (Double.isNaN(average)){
                    average = 0;
                }

                item.setAverage(average);
                if(Double.isNaN(sumRealization/questionCount)){
                    item.addGrade(0.0);
                } else {
                    item.addGrade((sumRealization/questionCount) * 100);
                }
                result.add(item);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{courseId}/students/gradesGroup")
    public ResponseEntity<List<StudentProgressItem>> getStudentGradesGrouped(@PathVariable long courseId){
        Optional<Course> course = this.courseService.findOne(courseId);
        ArrayList<StudentProgressItem> result = new ArrayList<>();

        if(course.isPresent()){
            this.courseService.buildInitialStudentGradeGroupedResult(result);
            ArrayList<StudentProgressItem> averages = (ArrayList<StudentProgressItem>) getStudentsProgress(courseId).getBody();
            if(averages != null){
                this.courseService.buildStudentGradeGroupedResult(result, averages);
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
