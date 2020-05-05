package com.course;

import com.course.items.ProgressNode;
import com.course.items.StudentProgressItem;
import com.user.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api
public interface CourseController {
    @ApiOperation(value = "Return all the courses.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Courses")
    })
    ResponseEntity<List<Course>> getCourses();

    @ApiOperation(value = "Return the course with the requested id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Course"),
            @ApiResponse(code = 404, message = "Course Not Found")
    })
    ResponseEntity<Course> getCourse(long courseId);

    @ApiOperation(value = "Return all the courses with the requested name.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Courses with the requested name"),
            @ApiResponse(code = 404, message = "Courses Not Found")
    })
    ResponseEntity<List<Course>> searchCourseByNameContaining(String name);

    @ApiOperation(value = "Create a new Course.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The course has been successfully created")
    })
    ResponseEntity<Course> createCourse(Course course);

    @ApiOperation(value = "Update the course with the requested id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The course has been successfully updated"),
            @ApiResponse(code = 404, message = "Course Not Found")
    })
    ResponseEntity<Course> editCourse(long courseId, Course course);

    @ApiOperation(value = "Delete the course with the requested id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The course has been successfully deleted"),
            @ApiResponse(code = 404, message = "Course Not Found")
    })
    ResponseEntity<Course> deleteCourse(long courseId);

    @ApiOperation(value = "Return all the courses the student with the requested id is in.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Courses the student is in")
    })
    ResponseEntity<List<Course>> getUserCourses(long userId);

    @ApiOperation(value = "Return all the courses teached by the teacher with the requested id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Courses teached by the teacher")
    })
    ResponseEntity<List<Course>> getTeacherCourses(long teacherId);

    @ApiOperation(value = "Adds the student to the course with the requested id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The student has been successfully added"),
            @ApiResponse(code = 404, message = "Course Not Found")
    })
    ResponseEntity<Course> addStudent(long courseId, User student);

    @ApiOperation(value = "Return the unit the lesson with the requested lessonId is in.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Unit id"),
            @ApiResponse(code = 404, message = "Module Not Found")
    })
    ResponseEntity<Long> getModuleUnit(long lessonId);

    @ApiOperation(value = "Return the progress of the course module.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Module Progress"),
            @ApiResponse(code = 404, message = "Course Not Found")
    })
    ResponseEntity<ProgressNode> getModuleProgress(long courseId);

    @ApiOperation(value = "Return the progress of the requested course students.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Student Progress"),
            @ApiResponse(code = 404, message = "Course Not Found")
    })
    ResponseEntity<List<StudentProgressItem>> getStudentsProgress(long courseId);

    @ApiOperation(value = "Return the requested course student grades grouped.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Student grades grouped."),
            @ApiResponse(code = 404, message = "Course Not Found")
    })
    ResponseEntity<List<StudentProgressItem>> getStudentGradesGrouped(long courseId);
}
