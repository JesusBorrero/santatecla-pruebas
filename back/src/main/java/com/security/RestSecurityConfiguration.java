package com.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(1)
public class RestSecurityConfiguration extends WebSecurityConfigurerAdapter{
   
    @Autowired
    protected UserRepositoryAuthProvider userAuthentication;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.antMatcher("/api/**");

		// User
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/login").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/register").permitAll();

        //Unit
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/units/").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/units/{id}").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/units/").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/units/").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/units/{id}").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/units/relations/{id}").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/units/search").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/units/{id}/absoluteName").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/units/{id}/parent").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/units/valid").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/units/{id}/name").hasRole("ADMIN");

        //Unit Cards
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/units/{unitId}/cards").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/units/{unitId}/cards").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/units/{unitId}/cards/{cardId}").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/units/{unitId}/cards/{cardId}").hasAnyRole("ADMIN", "USER");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/units/{unitId}/cards/{cardId}").hasRole("ADMIN");

        //Unit Lesson
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/units/{unitId}/lessons/{lessonId}/slides/{slideId}").hasAnyRole("ADMIN", "USER");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/units/{unitId}/lessons/").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/units/{unitId}/lessons/{lessonId}").hasRole("ADMIN");

        //Unit Module
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/units/{unitId}/modules").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/units/{unitId}/modules/{moduleId}").hasRole("ADMIN");

        //Question
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/units/{unitId}/question").hasAnyRole("ADMIN", "USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/units/{unitId}/question/{questionId}").hasAnyRole("ADMIN", "USER");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/units/{unitId}/question/{questionId}").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/units/{unitId}/question/{questionId}/correctCount").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/units/{unitId}/question/{questionId}/wrongCount").hasRole("ADMIN");

        //Definition question
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/units/{unitId}/question/definition").hasAnyRole("ADMIN", "USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/units/{unitId}/question/definition/{questionID}").hasAnyRole("ADMIN", "USER");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/units/{unitId}/question/definition").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/units/{unitId}/question/definition/{questionID}").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/units/{unitId}/question/definition/{questionID}").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/units/{unitId}/question/definition/{questionID}/answer").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/units/{unitId}/question/definition/{questionID}/answer").hasAnyRole("ADMIN", "USER");
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/units/{unitId}/question/definition/{questionID}/answer/{answerID}").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/units/{unitId}/question/definition/{id}/answer/user/{userId}").hasAnyRole("ADMIN", "USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/units/{unitId}/question/definition/{questionID}/uncorrectedCount").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/units/{unitId}/question/definition/{questionID}/answer/user/{userId}").hasAnyRole("USER", "ADMIN");

        //List question
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/units/{unitId}/question/list").hasAnyRole("ADMIN", "USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/units/{unitId}/question/list/{questionID}").hasAnyRole("ADMIN", "USER");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/units/{unitId}/question/list").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/units/{unitId}/question/list/{questionID}").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/units/{unitId}/question/list/{questionID}").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/units/{unitId}/question/list/{questionID}/answer").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/units/{unitId}/question/list/{questionID}/answer").hasAnyRole("ADMIN", "USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/units/{unitId}/question/list/{id}/answer/user/{userId}").hasAnyRole("ADMIN", "USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/units/{unitId}/question/list/{questionID}/chosenWrongAnswersCount").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/units/{unitId}/question/list/{questionID}/answer/user/{userId}").hasAnyRole("USER", "ADMIN");

        //Test question
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/units/{unitId}/question/test").hasAnyRole("ADMIN", "USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/units/{unitId}/question/test/{questionID}").hasAnyRole("ADMIN", "USER");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/units/{unitId}/question/test").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/units/{unitId}/question/test/{questionID}").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/units/{unitId}/question/test/{questionID}").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/units/{unitId}/question/test/{questionID}/answer").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/units/{unitId}/question/test/{questionID}/answer").hasAnyRole("ADMIN", "USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/units/{unitId}/question/test/{id}/answer/user/{userId}").hasAnyRole("ADMIN", "USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/units/{unitId}/question/test/{questionID}/chosenWrongAnswersCount").hasRole("ADMIN");

        //Course
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/course/").hasAnyRole("ADMIN", "USER");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/course/").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/course/{id}").hasAnyRole("ADMIN", "USER");
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/course/{id}").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/course/{id}").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/course/user/{id}").hasAnyRole("ADMIN", "USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/course/teacher/{id}").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/course/{id}/students/").hasAnyRole("ADMIN", "USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/course/search/{name}").hasAnyRole("ADMIN", "USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/course/{courseId}/module/progress").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/course/{courseId}/student/progress").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/course/{courseId}/module/{moduleId}/extended").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/course/{courseId}/module/format").hasRole("ADMIN");

        //Lesson
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/lessons/").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/lessons/{id}").hasAnyRole("ADMIN", "USER");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/lessons/{id}").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/lessons/{id}").hasRole("ADMIN");

        //Module
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/modules/").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/modules/{id}").hasAnyRole("ADMIN", "USER");
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/modules/{id}").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/modules/{moduleId}").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/modules/{moduleId}/blocks/{blockId}").hasRole("ADMIN");

        //Slide
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/slides/").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/slides/{id}").hasAnyRole("ADMIN", "USER");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/slides/{id}").hasRole("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/slides/{id}").hasRole("ADMIN");

        // Do not redirect when logout
		http.logout().logoutSuccessHandler((rq, rs, a) -> { });
		
		// Use HTTP basic authentication
		http.httpBasic();

        // Disable CSRF
        http.csrf().disable();

        http.logout().logoutUrl("/logout");
        
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(userAuthentication);
    }
    
}
