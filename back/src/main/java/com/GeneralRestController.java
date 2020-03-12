package com;

import com.card.CardService;
import com.course.CourseService;
import com.itinerary.block.BlockService;
import com.itinerary.lesson.LessonService;
import com.itinerary.module.ModuleService;
import com.question.QuestionService;
import com.question.definition.definition_question.DefinitionQuestionService;
import com.question.list.list_question.ListQuestionService;
import com.question.test.test_question.TestQuestionService;
import com.relation.RelationService;
import com.slide.*;
import com.unit.UnitService;
import com.user.UserComponent;
import com.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GeneralRestController {
    
    @Autowired
	protected UserComponent userComponent;
	
	@Autowired
	protected UserService userService;

	@Autowired
	protected SlideService slideService;

	@Autowired
	protected LessonService lessonService;

	@Autowired
	protected ModuleService moduleService;

	@Autowired
	protected BlockService blockService;

	@Autowired
	protected CardService cardService;

	@Autowired
	protected DefinitionQuestionService definitionQuestionService;

	@Autowired
	protected ListQuestionService listQuestionService;

	@Autowired
	protected TestQuestionService testQuestionService;

	@Autowired
	protected QuestionService questionService;

	@Autowired
	protected CourseService courseService;

	@Autowired
	protected UnitService unitService;

	@Autowired
	protected RelationService relationService;
}