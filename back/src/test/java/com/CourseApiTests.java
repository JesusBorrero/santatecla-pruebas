package com;

import com.course.Course;
import com.course.CourseService;
import com.google.gson.Gson;
import com.itinerary.module.Module;
import com.itinerary.module.ModuleService;
import com.question.Question;
import com.question.QuestionService;
import com.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles="ADMIN")
public class CourseApiTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CourseService courseService;

    @MockBean
    private QuestionService questionService;

    @MockBean
    private ModuleService moduleService;

    private Gson jsonParser = new Gson();

    @Before
    public void initialize() {
        Course course = new Course();
        course.setName("Test Course");
        course.setId(1);
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(course);

        Module module = new Module();
        module.setName("Test Module");
        module.setId(1);

        course.setModule(module);

        ArrayList<Long> userCourses = new ArrayList<>();
        userCourses.add((long)1);

        Question question = new Question();
        ArrayList<Question> questions = new ArrayList<>();
        questions.add(question);



        given(courseService.findAll()).willReturn(courses);
        given(courseService.findOne(1)).willReturn(Optional.of(course));
        given(courseService.findOne(2)).willReturn(Optional.empty());
        given(courseService.findUserCourses(1)).willReturn(userCourses);
        given(courseService.findTeachingCourses(1)).willReturn(courses);
        given(courseService.searchCourseByNameContaining("Tes")).willReturn(courses);
        given(courseService.findBlockRealization(course.getStudents(), 1, 1, 1)).willReturn((double)1);
        given(courseService.findBlockGrade(course.getStudents(), 1, 1)).willReturn((double)1);
        given(questionService.findQuestionsByBlockId(1)).willReturn(questions);
        given(questionService.findBlockQuestionCount(1)).willReturn(1);
    }

    @Test
    public void testGetAllCourses() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/course/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Test Course")));
    }

    @Test
    public void testGetCourse() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/course/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Course")));
    }

    @Test
    public void testNotFoundGetCourse() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/course/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testCreateCourse() throws Exception{
        Module module = new Module();
        Course course = new Course();
        course.setName("Test Course");
        course.setModule(module);

        mvc.perform(MockMvcRequestBuilders.post("/api/course/")
                .content(jsonParser.toJson(course))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201));
    }

    @Test
    public void testEditCourse() throws Exception{
        Course course2 = new Course();
        course2.setName("Test Edit Course");

        mvc.perform(MockMvcRequestBuilders.put("/api/course/1")
                .content(jsonParser.toJson(course2))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Test Edit Course")))
                .andExpect(status().is(200));
    }

    @Test
    public void testNotFoundEditCourse() throws Exception{

        Course course2 = new Course();
        course2.setName("Test Edit Course");

        mvc.perform(MockMvcRequestBuilders.put("/api/course/2")
                .content(jsonParser.toJson(course2))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testDeleteCourse() throws Exception{
        mvc.perform(MockMvcRequestBuilders.delete("/api/course/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testNotFoundDeleteCourse() throws Exception{
        mvc.perform(MockMvcRequestBuilders.delete("/api/course/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testGetUserCourses() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/course/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Test Course")));
    }

    @Test
    public void testGetTeacherCourses() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/course/teacher/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Test Course")));
    }

    @Test
    public void testAddStudent() throws Exception{
        Course course = new Course();
        course.setName("Test Course");

        User user = new User();
        user.setName("Test");

        given(courseService.findOne(1)).willReturn(Optional.of(course));

        mvc.perform(MockMvcRequestBuilders.post("/api/course/1/students/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(user)))
                .andExpect(status().isOk());

        assertThat(course.getStudents().size(), is(1));
    }

    @Test
    public void testNotFoundAddStudent() throws Exception{
        User user = new User();
        user.setName("Test");

        mvc.perform(MockMvcRequestBuilders.post("/api/course/2/students/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(user)))
                .andExpect(status().is(404));
    }

    @Test
    public void testSearchByNameContaining() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/course/search/Tes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Test Course")));
    }

    @Test
    public void testGetModuleProgress() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/course/1/module/progress")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Test Module")));
    }

    @Test
    public void testNotFoundGetModuleProgress() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/course/2/module/progress")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testGetStudentProgress() throws Exception{
        Question question = new Question();
        ArrayList<Question> questions = new ArrayList<>();
        questions.add(question);

        Course course = new Course();
        course.setName("Test Course");

        User student = new User();
        student.setName("Test User");
        student.setId((long)(1));

        course.addStudent(student);

        Module module = new Module();
        module.setName("Test Module");
        module.setId(1);

        course.setModule(module);

        given(courseService.findOne(1)).willReturn(Optional.of(course));
        given(courseService.findUserQuestionGrade((long)1, 1, 1, question)).willReturn((double)1);
        given(courseService.findUserRealization((long)1, (long)1, (long)1)).willReturn(1);
        given(questionService.findQuestionsByBlockId(1)).willReturn(questions);
        given(questionService.findBlockQuestionCount(1)).willReturn(1);

        mvc.perform(MockMvcRequestBuilders.get("/api/course/1/students/progress")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].studentName", is("Test User")))
                .andExpect(jsonPath("$[0].average", is(1.0)));
    }

    @Test
    public void testNotFoundGetStudentProgress() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/course/2/students/progress")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testGetStudentGradesGrouped() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/course/1/students/gradesGroup")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
